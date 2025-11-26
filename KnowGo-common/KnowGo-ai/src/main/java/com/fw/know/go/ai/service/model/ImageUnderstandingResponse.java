package com.fw.know.go.ai.service.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @Description 图像理解响应
 * @Date 24/11/2025 下午4:03
 * @Author Leo
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImageUnderstandingResponse {
    
    /**
     * 理解结果
     */
    private String content;
    
    /**
     * 使用的模型
     */
    private String model;
    
    /**
     * 消耗的token数
     */
    private TokenUsage tokenUsage;
    
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
    public static class TokenUsage {
        /**
         * 提示词token数
         */
        private Integer promptTokens;
        
        /**
         * 生成token数
         */
        private Integer completionTokens;
        
        /**
         * 总token数
         */
        private Integer totalTokens;
    }
}