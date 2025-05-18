package com.choose;

import com.choose.risk.*;
import org.ahocorasick.trie.Emit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class SensitiveWordDetector {
    public static void main(String[] args) throws IOException {
        // 加载词库
        SensitiveWordMatcher matcher = new SensitiveWordMatcher("src/main/resources/sensitive_words.txt");

        String inputText = "用户caonima的文本内容nihao";
        // 预处理
        String preprocessedText = TextPreprocessor.preprocess(inputText);

        // ca自动机处理
        Collection<Emit> match = matcher.match(preprocessedText);
        Collection<TypedEmit> matches = new ArrayList<>();
        match.forEach((emit) -> {
            matches.add(new TypedEmit(emit.getStart(), emit.getEnd(),emit.getKeyword(),matcher.getType(emit.getKeyword())));
        });


        //todo NLP处理

        // ai处理
        Boolean b = DeepSeekChatExample.aiDetector(inputText);
        if (b) {
            System.out.println("拦截/封禁（高风险）");
            return;
        }

        // 初始化配置（实际可从文件/数据库读取）
        Map<String, RiskRule> rules = Map.of(
                "辱骂", new RiskRule("辱骂", 10, false),
                "其他", new RiskRule("其他", 1, true)
        );

        List<RiskStrategy> strategies = List.of(
                new SumStrategy(),
                new CompositeStrategy()
        );

        // 创建打分器
        RiskScorer scorer = new RiskScorer(matcher, rules, strategies);
        RiskResult result = scorer.score(matches);

        // 输出结果
        System.out.println("风险等级: " + result.getRiskLevel());

        switch (result.getRiskLevel()) {
            case "NORMAL":
                System.out.println("正常放行");
                break;
            case "REVIEW":
                System.out.println("加入人工审核队列");
                break;
            case "HIGH":
                System.out.println("拦截/封禁（高风险）");
                break;
        }
    }
}
