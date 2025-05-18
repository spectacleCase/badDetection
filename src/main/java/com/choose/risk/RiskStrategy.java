package com.choose.risk;

import java.util.List;
import java.util.Map;

// 策略接口
public interface RiskStrategy {
    int evaluate(List<TypedEmit> matches, Map<String, RiskRule> rules);
}



