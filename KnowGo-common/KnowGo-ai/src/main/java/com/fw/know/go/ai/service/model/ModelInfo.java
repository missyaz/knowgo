package com.fw.know.go.ai.service.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description 模型信息
 * @Date 24/11/2025 下午4:13
 * @Author Leo
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ModelInfo {
    
    /**
     * 模型ID
     */
    private String id;
    
    /**
     * 模型名称
     */
    private String name;
    
    /**
     * 模型类型（chat, embedding, image, audio）
     */
    private String type;
    
    /**
     * 提供商（openai, dashscope）
     */
    private String provider;
    
    /**
     * 最大上下文长度
     */
    private Integer maxContextLength;
    
    /**
     * 是否支持流式输出
     */
    private Boolean supportsStreaming;
    
    /**
     * 是否支持工具调用
     */
    private Boolean supportsTools;
    
    /**
     * 是否支持多模态
     */
    private Boolean supportsMultimodal;
    
    /**
     * 模型描述
     */
    private String description;
    
    /**
     * 是否可用
     */
    private Boolean available;
}