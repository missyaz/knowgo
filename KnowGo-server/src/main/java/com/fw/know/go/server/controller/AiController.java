package com.fw.know.go.server.controller;

import com.alibaba.cloud.ai.dashscope.api.DashScopeApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Classname AiController
 * @Description TODO
 * @Date 27/10/2025 下午1:55
 * @Author Leo
 */
@Slf4j
@RestController
@RequestMapping("/ai")
public class AiController {

    @Value("${spring.ai.dashscope.api-key}")
    private String apiKey;

    private final ChatClient chatClient;

    private final ChatModel chatModel;

    public AiController(ChatClient chatClient, ChatModel chatModel) {
        this.chatClient = chatClient;
        this.chatModel = chatModel;
    }

    @GetMapping("/chat")
    public String chat(@RequestParam(value = "message", defaultValue = "你是谁") String message) {
        String system = """
               你是一个专业的知识问答助手，你的回答必须符合中文的语法规范，不能使用英文。
               回答不能超过200字
               """;
        return chatModel.call(new Prompt(message)).getResult().getOutput().getText();
    }
}
