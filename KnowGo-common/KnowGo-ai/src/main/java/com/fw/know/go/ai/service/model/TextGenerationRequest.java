package com.fw.know.go.ai.service.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @Description 文本生成请求
 * @Date 24/11/2025 下午3:35
 * @Author Leo
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TextGenerationRequest {
    
    /**
     * 提示词模板名称
     */
    private String templateName;
    
    /**
     * 提示词参数
     */
    private Map<String, Object> templateParams;
    
    /**
     * 直接文本（当不使用模板时）
     */
    private String text;
    
    /**
     * 模型名称
     */
    private String model;
    
    /**
     * 温度参数（0-2）
     */
    private Double temperature;
    
    /**
     * 最大token数
     */
    private Integer maxTokens;
    
    /**
     * 生成数量
     */
    private Integer n;
    
    /**
     * 是否流式输出
     */
    private Boolean stream;
    
    /**
     * 额外参数
     */
    private Map<String, Object> extraParams;
}