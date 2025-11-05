package com.fw.know.go.ai.configuration;

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
     * 大模型的API密钥
     */
    private String apiKey;

     /**
      * 大模型的名称
      */
    private String model;
}
