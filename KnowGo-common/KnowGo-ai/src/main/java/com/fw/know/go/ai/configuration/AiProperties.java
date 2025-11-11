package com.fw.know.go.ai.configuration;

import com.fw.know.go.ai.EnableMode;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Description 大模型配置属性类
 * @Date 5/11/2025 下午2:47
 * @Author Leo
 */
@Data
@ConfigurationProperties(prefix = AiProperties.PREFIX)
public class AiProperties {

    public static final String PREFIX = "know.go.ai";

    /**
     * 聊天模型的启用模式，可选值为"openai"和"dashscope"
     */
    private String chatEnableMode = EnableMode.OPENAI.getValue();

    /**
     * 嵌入模型的启用模式，可选值为"openai"和"dashscope"
     */
    private String embeddingEnableMode = EnableMode.OPENAI.getValue();

    /**
     * 大模型的API基础URL
     */
    private String chatApiBaseUrl;

    /**
     * 大模型的API密钥
     */
    private String chatApiKey;

    /**
     * 大模型的名称
     */
    private String chatModel;

    /**
     * 嵌入模型的API基础URL
     */
    private String embeddingApiBaseUrl;

    /**
     * 嵌入模型的API密钥
     */
    private String embeddingApiKey;

    /**
      * 嵌入模型的名称
      */
    private String embeddingModel;
}
