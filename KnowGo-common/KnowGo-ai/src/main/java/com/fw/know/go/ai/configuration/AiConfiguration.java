package com.fw.know.go.ai.configuration;

import com.alibaba.cloud.ai.dashscope.api.DashScopeApi;
import com.alibaba.cloud.ai.dashscope.embedding.DashScopeEmbeddingModel;
import lombok.Data;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description 大模型配置类
 * @Date 5/11/2025 下午2:50
 * @Author Leo
 */
@Configuration
@EnableConfigurationProperties(AiProperties.class)
public class AiConfiguration {

    private final AiProperties aiProperties;

    public AiConfiguration(AiProperties aiProperties) {
        this.aiProperties = aiProperties;
    }

    @Bean
    @ConditionalOnMissingBean
    public DashScopeApi dashScopeApi() {
        return DashScopeApi.builder().apiKey(aiProperties.getApiKey()).build();
    }

    @Bean
    @ConditionalOnMissingBean
    public EmbeddingModel embeddingModel(DashScopeApi dashScopeApi){
        return new DashScopeEmbeddingModel(dashScopeApi);
    }
}
