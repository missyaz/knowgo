package com.fw.know.go.ai.configuration;

import com.alibaba.cloud.ai.dashscope.api.DashScopeApi;
import com.alibaba.cloud.ai.dashscope.embedding.DashScopeEmbeddingModel;
import com.alibaba.cloud.ai.dashscope.embedding.DashScopeEmbeddingOptions;
import io.micrometer.observation.ObservationRegistry;
import org.springframework.ai.document.MetadataMode;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.model.tool.ToolCallingManager;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.OpenAiEmbeddingModel;
import org.springframework.ai.openai.OpenAiEmbeddingOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.support.RetryTemplate;

/**
 * @Description 大模型配置类
 * @Date 5/11/2025 下午2:50
 * @Author Leo
 */
@Configuration
@EnableConfigurationProperties(AiProperties.class)
public class AiConfiguration {

    private final AiProperties aiProperties;

    /**
     * openai默认的嵌入模型名称
     */
    public static final String OPEN_AI_EMBEDDING_MODEL = "text-embedding-ada-002";

     /**
     * openai默认的聊天模型名称
     */
    public static final String OPEN_AI_CHAT_MODEL = "gpt-3.5-turbo";

    public AiConfiguration(AiProperties aiProperties) {
        this.aiProperties = aiProperties;
    }

    /**
     * OpenAI聊天模型的Bean定义
     */
    @Bean
    @Qualifier("chatOpenAiApi")
    @ConditionalOnProperty(prefix = AiProperties.PREFIX, name = "chatEnableMode", havingValue = "openai")
    public OpenAiApi chatOpenAiApi(){
        return OpenAiApi.builder().baseUrl(aiProperties.getChatApiBaseUrl())
                .apiKey(aiProperties.getChatApiKey()).build();
    }

    /**
     * OpenAI嵌入模型的Bean定义
     */
    @Bean
    @Qualifier("embeddingOpenAiApi")
    @ConditionalOnProperty(prefix = AiProperties.PREFIX, name = "embeddingEnableMode", havingValue = "openai")
    public OpenAiApi embeddingOpenAiApi(){
        return OpenAiApi.builder().baseUrl(aiProperties.getEmbeddingApiBaseUrl())
                .apiKey(aiProperties.getEmbeddingApiKey()).build();
    }

    /**
     * DashScope聊天模型的Bean定义
     */
    @Bean
    @Qualifier("chatDashScopeApi")
    @ConditionalOnProperty(prefix = AiProperties.PREFIX, name = "chatEnableMode", havingValue = "dashscope")
    public DashScopeApi chatDashScopeApi() {
        return DashScopeApi.builder().apiKey(aiProperties.getChatApiKey()).build();
    }

    /**
     * DashScope嵌入模型的Bean定义
     */
    @Bean
    @Qualifier("embeddingDashScopeApi")
    @ConditionalOnProperty(prefix = AiProperties.PREFIX, name = "embeddingEnableMode", havingValue = "dashscope")
    public DashScopeApi embeddingDashScopeApi() {
        return DashScopeApi.builder().apiKey(aiProperties.getEmbeddingApiKey()).build();
    }
}
