package com.choose;

import java.text.Normalizer;

public class TextPreprocessor {
    // 修改预处理方法，不要移除空格
    public static String preprocess(String text) {
        if (text == null) {
            return "";
        }
        // 全角转半角
        text = toDBC(text);
        // 小写
        text = text.toLowerCase();
        // 移除 HTML 标签
        text = text.replaceAll("<[^>]+>", "");
        // Unicode 标准化
        text = Normalizer.normalize(text, Normalizer.Form.NFKC);
        // 保留空格，只移除标点符号
        text = text.replaceAll("[\\p{Punct}]+", "");
        // 可选：过滤 emoji、控制字符
        text = text.replaceAll("[\\p{C}\\p{So}]", "");
        // 处理拼音转汉字
        text = PinyinUtils.toPinyin(text);
        return text;
    }

    public static String toDBC(String input) {
        StringBuilder output = new StringBuilder(input.length());
        for (char c : input.toCharArray()) {
            if (c == 12288) {
                output.append(' '); // 全角空格转半角空格
            } else if (c >= 65281 && c <= 65374) {
                output.append((char) (c - 65248)); // 其他全角字符转半角
            } else {
                output.append(c);
            }
        }
        return output.toString();
    }


}
