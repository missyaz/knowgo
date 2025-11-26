package com.fw.know.go.ai.service.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * @Description 图像理解请求
 * @Date 24/11/2025 下午4:00
 * @Author Leo
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImageUnderstandingRequest {
    
    /**
     * 图像数据（base64编码或URL）
     */
    private List<ImageContent> images;
    
    /**
     * 问题或提示词
     */
    private String prompt;
    
    /**
     * 模型名称
     */
    private String model;
    
    /**
     * 温度参数
     */
    private Double temperature;
    
    /**
     * 最大token数
     */
    private Integer maxTokens;
    
    /**
     * 额外参数
     */
    private Map<String, Object> extraParams;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ImageContent {
        /**
         * 图像类型（url或base64）
         */
        private String type;
        
        /**
         * 图像URL
         */
        private String url;
        
        /**
         * 图像base64数据
         */
        private String base64Data;
        
        /**
         * 媒体类型
         */
        private String mediaType;
        
        /**
         * 详细程度（auto, low, high）
         */
        private String detail;
    }
}