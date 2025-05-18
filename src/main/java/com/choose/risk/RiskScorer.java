package com.choose.risk;

import com.choose.SensitiveWordMatcher;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author lizhentao
 */
public class RiskScorer {
    private final SensitiveWordMatcher matcher;

    /**
     * 规则配置表
     */
    private final Map<String, RiskRule> rules;
    /**
     * 策略链
     */
    private final List<RiskStrategy> strategies;

    public RiskScorer(SensitiveWordMatcher matcher, 
                     Map<String, RiskRule> rules, 
                     List<RiskStrategy> strategies) {
        this.matcher = matcher;
        this.rules = rules;
        this.strategies = strategies;
    }

    public RiskResult score(Collection<TypedEmit> matches) {
        int rawScore = 0;
        String riskType = "NORMAL";

        // 依次执行策略链
        for (RiskStrategy strategy : strategies) {
            rawScore = strategy.evaluate((List<TypedEmit>) matches, rules);
            // 动态决策
            if (rawScore > 10) {
                riskType = "HIGH";
                break;
            }
        }

        // 返回结构化结果
        return new RiskResult(rawScore, riskType, matches);
    }


}