package com.choose.risk;

import java.util.List;
import java.util.Map;

// 示例策略：简单累加
public class SumStrategy implements RiskStrategy {
    @Override
    public int evaluate(List<TypedEmit> matches, Map<String, RiskRule> rules) {
        return matches.stream()
            .mapToInt(m -> rules.get(m.getKeywordType()).getBaseScore())
            .sum();
    }
}