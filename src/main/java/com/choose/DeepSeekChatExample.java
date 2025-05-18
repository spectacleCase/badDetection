package com.choose;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;

import java.io.IOException;

/**
 * @author 桌角的眼镜
 */
public class DeepSeekChatExample {

    private static final String API_KEY = "替换成你的 API Key"; //
    private static final String API_URL = "https://api.deepseek.com/chat/completions";

    public static Boolean aiDetector(String comment) throws IOException {
        OkHttpClient client = new OkHttpClient();
        ObjectMapper mapper = new ObjectMapper();

        // 构建 JSON 请求体
        String jsonBody = """
                {
                  "model": "deepseek-chat",
                  "messages": [
                    {
                      "role": "system",
                      "content": "你是一名内容审核助手。你的任务是判断用户输入的句子中是否包含辱骂、色情、暴力或其他敏感内容。如果包含，返回格式严格为：{\\"result\\": true}，否则返回 {\\"result\\": false}。不要添加多余说明或解释，只返回严格的 JSON 数据，确保可以被 JSON.parse 正确解析。"
                    },
                    {
                      "role": "user",
                      "content": "%s"
                    }
                  ],
                  "stream": false
                }
                """.formatted(comment);


        Request request = new Request.Builder()
                .url(API_URL)
                .addHeader("Authorization", "Bearer " + API_KEY)
                .addHeader("Content-Type", "application/json")
                .post(RequestBody.create(jsonBody, MediaType.get("application/json")))
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                String body = response.body().string();
                JsonNode root = mapper.readTree(body);
                String content = root.at("/choices/0/message/content").asText().trim();
                JsonNode resultNode = mapper.readTree(content);
                System.out.println("是否违规内容: " + resultNode.get("result").asBoolean());
                return resultNode.get("result").asBoolean();
            } else {
                System.err.println("请求失败，状态码: " + response.code());
                System.err.println("响应内容: " + response.body().string());
                throw new IOException();
            }
        } catch (IOException e) {
            System.err.println("AI 回复不是有效");
            e.printStackTrace();
        }
        return false;
    }
}
