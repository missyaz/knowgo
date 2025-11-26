package com.fw.know.go.ai.service.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * @Description 文本生成响应
 * @Date 24/11/2025 下午3:38
 * @Author Leo
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TextGenerationResponse {
    
    /**
     * 生成的文本列表
     */
    private List<GeneratedText> generatedTexts;
    
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
    public static class GeneratedText {
        /**
         * 生成的文本
         */
        private String text;
        
        /**
         * 索引
         */
        private Integer index;
        
        /**
         * 完成原因
         */
        private String finishReason;
        
        /**
         * 对数概率
         */
        private Double logprobs;
    }
    
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