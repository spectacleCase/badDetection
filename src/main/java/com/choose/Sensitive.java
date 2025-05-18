package com.choose;

/**
 * <p>
 *  敏感词
 * </p>
 *
 * @author 桌角的眼镜
 * @version 1.0
 * @since 2025/5/18 15:54
 */
public class Sensitive {
    /**
     * 词
     */
    private String word;

    /**
     * 分数
     */
    private Integer scorer;


    /**
     * 类型
     */
    private String type;

    public Sensitive(String word, String type, Integer scorer) {
        this.word = word;
        this.scorer = scorer;
        this.type = type;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public Integer getScorer() {
        return scorer;
    }

    public void setScorer(Integer scorer) {
        this.scorer = scorer;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
