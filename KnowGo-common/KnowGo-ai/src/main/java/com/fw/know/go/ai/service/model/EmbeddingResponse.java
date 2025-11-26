package com.fw.know.go.ai.service.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * @Description 嵌入响应
 * @Date 24/11/2025 下午3:54
 * @Author Leo
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmbeddingResponse {
    
    /**
     * 嵌入向量列表
     */
    private List<Embedding> embeddings;
    
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
    public static class Embedding {
        /**
         * 索引
         */
        private Integer index;
        
        /**
         * 嵌入向量
         */
        private List<Double> embedding;
        
        /**
         * 输入文本
         */
        private String input;
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
         * 总token数
         */
        private Integer totalTokens;
    }
}