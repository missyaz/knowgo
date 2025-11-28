package com.fw.know.go.document.infrastructure.configuration;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description 大模型配置
 * @Date 12/11/2025 下午3:27
 * @Author Leo
 */
@Configuration
public class LLMConfiguration {

//    @Value("${spring.ai.openai.base-url}")
//    private String embeddingApiBaseUrl;
//
//    @Value("${spring.ai.openai.api-key}")
//    private String embeddingApiKey;
//
//    @Value("${spring.ai.openai.base-url}")
//    private String chatApiBaseUrl;
//
//    @Value("${spring.ai.openai.api-key}")
//    private String chatApiKey;

//    /**
//     * OpenAI嵌入模型的Bean定义
//     */
//    @Bean
//    @Qualifier("embeddingOpenAiApi")
//    public OpenAiApi embeddingOpenAiApi(){
//        return OpenAiApi.builder()
//                .baseUrl(embeddingApiBaseUrl)
//                .apiKey(embeddingApiKey)
//                .build();
//    }
//
//    /**
//     * OpenAI嵌入模型的Bean定义
//     */
//    @Bean
//    @Qualifier("embeddingModel")
//    public EmbeddingModel embeddingModel(){
//        return new OpenAiEmbeddingModel(embeddingOpenAiApi());
//    }
//
//    @Bean
//    @Qualifier("chatOpenAiApi")
//    public OpenAiApi chatOpenAiApi(){
//        return OpenAiApi.builder()
//                .baseUrl(chatApiBaseUrl)
//                .apiKey(chatApiKey)
//                .build();
//    }
//
//    @Bean
//    @Qualifier("chatOpenAiApi")
//    public ChatModel chatOpenAiModel(){
//        return OpenAiChatModel.builder().openAiApi(chatOpenAiApi()).build();
//    }

    @Bean
    public ChatClient chatClient(ChatModel dashScopeApi){
        return ChatClient.builder(dashScopeApi).defaultAdvisors(new SimpleLoggerAdvisor()).build();
    }
}
