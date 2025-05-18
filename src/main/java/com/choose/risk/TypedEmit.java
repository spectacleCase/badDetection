package com.choose.risk;

import org.ahocorasick.trie.Emit;

public class TypedEmit extends Emit {
    /**
     * 敏感词类型：政治/广告/辱骂
     */
    private final String keywordType;

    public TypedEmit(int start, int end, String keyword, String keywordType) {
        super(start, end, keyword);
        this.keywordType = keywordType;
    }

    public String getKeywordType() {
        return keywordType;
    }
}