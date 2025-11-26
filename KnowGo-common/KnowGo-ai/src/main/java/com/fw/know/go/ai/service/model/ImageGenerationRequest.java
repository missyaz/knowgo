package com.fw.know.go.ai.service.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @Description 图像生成请求
 * @Date 24/11/2025 下午3:56
 * @Author Leo
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImageGenerationRequest {
    
    /**
     * 提示词
     */
    private String prompt;
    
    /**
     * 模型名称
     */
    private String model;
    
    /**
     * 图像数量
     */
    private Integer n;
    
    /**
     * 图像大小
     */
    private String size;
    
    /**
     * 图像质量
     */
    private String quality;
    
    /**
     * 图像风格
     */
    private String style;
    
    /**
     * 响应格式
     */
    private String responseFormat;
    
    /**
     * 额外参数
     */
    private Map<String, Object> extraParams;
}