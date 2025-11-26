package com.fw.know.go.ai.service.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * @Description 图像生成响应
 * @Date 24/11/2025 下午3:58
 * @Author Leo
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImageGenerationResponse {
    
    /**
     * 生成的图像列表
     */
    private List<GeneratedImage> images;
    
    /**
     * 使用的模型
     */
    private String model;
    
    /**
     * 响应时间（毫秒）
     */
    private Long responseTime;
    
    /**
     * 额外信息
     */
    private Map<String, Object> extraInfo;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GeneratedImage {
        /**
         * 图像URL
         */
        private String url;
        
        /**
         * 图像base64编码
         */
        private String base64Data;
        
        /**
         * 修订版本
         */
        private String revisedPrompt;
    }
}