package com.fw.know.go.document.infrastructure.configuration;

import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.openai.OpenAiEmbeddingModel;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description 大模型配置
 * @Date 12/11/2025 下午3:27
 * @Author Leo
 */
@Configuration
public class LLMConfiguration {

    @Value("${spring.ai.openai.base-url}")
    private String embeddingApiBaseUrl;

    @Value("${spring.ai.openai.api-key}")
    private String embeddingApiKey;

    /**
     * OpenAI嵌入模型的Bean定义
     */
    @Bean
    @Qualifier("embeddingOpenAiApi")
    public OpenAiApi embeddingOpenAiApi(){
        return OpenAiApi.builder()
                .baseUrl(embeddingApiBaseUrl)
                .apiKey(embeddingApiKey)
                .build();
    }

    /**
     * OpenAI嵌入模型的Bean定义
     */
    @Bean
    @Qualifier("embeddingModel")
    public EmbeddingModel embeddingModel(OpenAiApi openAiApi){
        return new OpenAiEmbeddingModel(openAiApi);
    }
}
