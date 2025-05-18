package com.choose.risk;

import java.util.List;
import java.util.Map;

/**
 * 复合条件（如辱骂词权重翻倍）
 * @author lizhentao
 */
public class CompositeStrategy implements RiskStrategy {
    @Override
    public int evaluate(List<TypedEmit> matches, Map<String, RiskRule> rules) {
        int score = matches.stream().mapToInt(m -> {
            RiskRule rule = rules.get(m.getKeywordType());
            return "辱骂".equals(rule.getKeywordType()) ?
                rule.getBaseScore() * 2 : rule.getBaseScore();
        }).sum();
        return score > 50 ? 100 : score;
    }
}