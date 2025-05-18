package com.choose.risk;

// 规则配置类
public class RiskRule {
    /**
     * 类型：政治/广告/辱骂等
     */
    private String keywordType;
    /**
     * 基础分
     */
    private int baseScore;

    /**
     * 是否累加（如多次命中同一词是否重复计分）
     */
    private boolean cumulative;

    public RiskRule(String keywordType, int baseScore, boolean cumulative) {
        this.keywordType = keywordType;
        this.baseScore = baseScore;
        this.cumulative = cumulative;
    }

    public String getKeywordType() {
        return keywordType;
    }

    public void setKeywordType(String keywordType) {
        this.keywordType = keywordType;
    }

    public int getBaseScore() {
        return baseScore;
    }

    public void setBaseScore(int baseScore) {
        this.baseScore = baseScore;
    }

    public boolean isCumulative() {
        return cumulative;
    }

    public void setCumulative(boolean cumulative) {
        this.cumulative = cumulative;
    }
}