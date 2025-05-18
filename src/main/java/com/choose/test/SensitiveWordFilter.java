package com.choose.test;

import java.util.HashMap;
import java.util.Map;

/**
 * DFA算法 确定有限状态自动机
 * @author lizhentao
 */
public class SensitiveWordFilter {

    // 使用一个特殊的字符作为结束标志
    private static final Character LAST_CHAR = '\0';


    /**
     * 检查敏感词是否存在
     * @param sensitiveMap 敏感词字典树
     * @param txt 待检查文本
     * @param index 开始检查的索引位置
     * @return 检查结果，包含是否匹配和匹配到的敏感词
     */
    private static MatchResult checkSensitiveWord(Map<Character, Object> sensitiveMap, String txt, int index) {
        Map<Character, Object> currentMap = sensitiveMap;
        boolean flag = false;
        int wordNum = 0; // 记录过滤
        StringBuilder sensitiveWord = new StringBuilder(); // 记录过滤出来的敏感词

        for (int i = index; i < txt.length(); i++) {
            char word = txt.charAt(i);
            currentMap = (Map<Character, Object>) currentMap.get(word);
            if (currentMap != null) {
                wordNum++;
                sensitiveWord.append(word);
                if (Boolean.TRUE.equals(currentMap.get(LAST_CHAR))) {
                    // 表示已到词的结尾
                    flag = true;
                    break;
                }
            } else {
                break;
            }
        }
        // 两字成词
        if (wordNum < 2) {
            flag = false;
        }
        return new MatchResult(flag, sensitiveWord.toString());
    }

    /**
     * 判断文本中是否存在敏感词
     * @param txt 待检查文本
     * @param sensitiveMap 敏感词字典树
     * @return 检查结果，包含是否匹配和匹配到的敏感词
     */
    public static MatchResult filterSensitiveWord(String txt, Map<Character, Object> sensitiveMap) {
        MatchResult matchResult = new MatchResult(false, "");
        // 过滤掉除了中文、英文、数字之外的
        String txtTrim = txt.replaceAll("[^\\u4e00-\\u9fa5\\u0030-\\u0039\\u0061-\\u007a\\u0041-\\u005a]+", "");
        for (int i = 0; i < txtTrim.length(); i++) {
            matchResult = checkSensitiveWord(sensitiveMap, txtTrim, i);
            if (matchResult.flag) {
                System.out.println("sensitiveWord:" + matchResult.sensitiveWord);
                break;
            }
        }
        return matchResult;
    }

    /**
     * 构造敏感词map
     * @param sensitiveWordList 敏感词列表
     * @return 构建好的字典树
     */
    public static Map<Character, Object> makeSensitiveMap(String[] sensitiveWordList) {
        // 构造根节点
        Map<Character, Object> result = new HashMap<>();
        for (String word : sensitiveWordList) {
            Map<Character, Object> map = result;
            for (int i = 0; i < word.length(); i++) {
                // 依次获取字
                char ch = word.charAt(i);
                // 判断是否存在
                if (map.containsKey(ch)) {
                    // 获取下一层节点
                    map = (Map<Character, Object>) map.get(ch);
                } else {
                    // 将当前节点设置为非结尾节点
                    if (Boolean.TRUE.equals(map.get(LAST_CHAR))) {
                        if (map.containsKey(LAST_CHAR)) {
                            map.put(LAST_CHAR, false);
                        }
                    }
                    Map<Character, Object> item = new HashMap<>();
                    // 新增节点默认为结尾节点
                    item.put(LAST_CHAR, true);
                    map.put(ch, item);
                    map = (Map<Character, Object>) map.get(ch);
                }
            }
        }
        return result;
    }

    // 匹配结果类
    public static class MatchResult {
        public boolean flag;
        public String sensitiveWord;

        public MatchResult(boolean flag, String sensitiveWord) {
            this.flag = flag;
            this.sensitiveWord = sensitiveWord;
        }
    }

    // 测试代码
    public static void main(String[] args) {
        // 构建敏感词库
        String[] sensitiveWords = {"暴力", "色情", "赌博", "毒品", "fuck", "shit"};
        Map<Character, Object> sensitiveMap = makeSensitiveMap(sensitiveWords);

        // 测试用例
        String[] testCases = {
                "这是一段正常的文本",
                "这里包含暴力内容",
                "请远离毒品",
                "what the fuck!",
                "shi t这是一个测试",
                "赌 博是不好的行为"  // 注意中间有空格会被过滤掉
        };

        for (String testCase : testCases) {
            System.out.println("\n测试文本: " + testCase);
            MatchResult result = filterSensitiveWord(testCase, sensitiveMap);
            if (result.flag) {
                System.out.println("发现敏感词: " + result.sensitiveWord);
            } else {
                System.out.println("未发现敏感词");
            }
        }
    }
}