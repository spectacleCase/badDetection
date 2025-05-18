package com.choose.risk;

import org.ahocorasick.trie.Emit;

import java.util.Collection;

/**
 * @author lizhentao
 */
public class RiskResult {

    /**
     * 分数
     */
    private int score;

    /**
     * 风险类型
     */
    private String riskLevel;

    /**
     * 词库
     */
    private Collection<TypedEmit> matches;

    public RiskResult(int score, String riskLevel, Collection<TypedEmit> matches) {
        this.score = score;
        this.riskLevel = riskLevel;
        this.matches = matches;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getRiskLevel() {
        return riskLevel;
    }

    public void setRiskLevel(String riskLevel) {
        this.riskLevel = riskLevel;
    }

    public Collection<TypedEmit> getMatches() {
        return matches;
    }

    public void setMatches(Collection<TypedEmit> matches) {
        this.matches = matches;
    }
}