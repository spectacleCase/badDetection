package com.choose.test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class SensitiveWordsFilter {
    // Trie 树节点
    private static class TrieNode {
        Map<Character, TrieNode> children = new HashMap<>();
        TrieNode fail;
        boolean isEnd = false;
    }

    private static final TrieNode root = new TrieNode();
    private static boolean isBuilt = false;

    // 可扩展的干扰字符
    private static final Set<Character> ignoreChars = new HashSet<>(Arrays.asList('-', '_', '*', '#', ' '));

    // 加载敏感词列表并构建 Trie 树
    public static void loadSensitiveWords(InputStream inputStream) throws IOException {
        try {
            String jsonString = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            inputStream.close();

            List<String> sensitiveWords = parseSensitiveWords(jsonString);
            buildTrie(sensitiveWords);
            System.out.println("Sensitive words loaded successfully.");
        } catch (Exception e) {
            System.out.println("Load error: " + e.getMessage());
            throw e;
        }
    }

    // 解析 JSON 获取敏感词列表
    private static List<String> parseSensitiveWords(String jsonString) {
        List<String> words = new ArrayList<>();
        int start = jsonString.indexOf("\"list\":") + 7;
        if (start < 7) {
            return words;
        }

        int arrayStart = jsonString.indexOf('[', start);
        int arrayEnd = jsonString.indexOf(']', arrayStart);
        if (arrayStart == -1 || arrayEnd == -1) {
            return words;
        }

        String arrayContent = jsonString.substring(arrayStart + 1, arrayEnd);
        String[] wordArray = arrayContent.split(",");

        for (String word : wordArray) {
            word = word.trim().replace("\"", "");
            if (!word.isEmpty()) {
                words.add(word);
            }
        }

        return words;
    }

    // 构建 Trie 树 + fail 指针
    private static void buildTrie(List<String> words) {
        // 构建 Trie 树结构
        for (String word : words) {
            TrieNode node = root;
            for (char c : word.toLowerCase().toCharArray()) {
                node = node.children.computeIfAbsent(c, k -> new TrieNode());
            }
            node.isEnd = true;
        }

        // 构建 fail 指针（BFS）
        Queue<TrieNode> queue = new LinkedList<>();
        for (Map.Entry<Character, TrieNode> entry : root.children.entrySet()) {
            entry.getValue().fail = root;
            queue.add(entry.getValue());
            System.out.println(entry.getKey());
        }

        while (!queue.isEmpty()) {
            TrieNode current = queue.poll();
            for (Map.Entry<Character, TrieNode> entry : current.children.entrySet()) {
                char c = entry.getKey();
                TrieNode child = entry.getValue();
                TrieNode failNode = current.fail;

                while (failNode != null && !failNode.children.containsKey(c)) {
                    failNode = failNode.fail;
                }

                if (failNode != null) {
                    child.fail = failNode.children.get(c);
                } else {
                    child.fail = root;
                }

                // 如果 fail 指向的节点是结束节点，则当前也标记为结束
                if (child.fail != null && child.fail.isEnd) {
                    child.isEnd = true;
                }

                queue.add(child);
            }
        }

        isBuilt = true;
    }

    // 检查消息是否包含敏感词
    public static boolean containsSensitiveWords(String message) {
        if (!isBuilt) {
            throw new IllegalStateException("敏感词列表未初始化");
        }

        String lowerMessage = message.toLowerCase();
        TrieNode node = root;

        for (int i = 0; i < lowerMessage.length(); i++) {
            char c = lowerMessage.charAt(i);

            if (ignoreChars.contains(c)) {
                continue;
            }

            while (node != root && !node.children.containsKey(c)) {
                node = node.fail;
            }

            if (node.children.containsKey(c)) {
                node = node.children.get(c);
            }

            if (node.isEnd) {
                return true;
            }
        }

        return false;
    }

    // 测试代码
    public static void main(String[] args) {
        try {
            String jsonContent = "{\"words\": {\"list\": [\"暴力\", \"色情\", \"赌博\", \"毒品\", \"fuck\", \"shit\"]}}";
            InputStream inputStream = new ByteArrayInputStream(jsonContent.getBytes(StandardCharsets.UTF_8));

            loadSensitiveWords(inputStream);

            String[] testCases = {
                    "这是一段正常的文本",
                    "这里包含暴力内容",
                    "请远离毒品",
                    "what the fuck!",
                    "shi t这是一个测试",
                    "赌 博是不好的行为",
                    "测试干扰字符: 色_情",
                    "色*情",
                    "f_u_c_k"
            };

            for (String testCase : testCases) {
                boolean contains = containsSensitiveWords(testCase);
                System.out.println("测试文本: \"" + testCase + "\" -> 包含敏感词: " + contains);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
