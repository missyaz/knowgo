package com.fw.know.go.ai.prompt;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * @Description 提示词模板执行响应
 * @Date 24/11/2025 下午5:20
 * @Author Leo
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PromptExecutionResponse {
    
    /**
     * 响应ID
     */
    private String responseId;
    
    /**
     * 请求ID
     */
    private String requestId;
    
    /**
     * 模板ID
     */
    private String templateId;
    
    /**
     * 模板名称
     */
    private String templateName;
    
    /**
     * 渲染后的提示词
     */
    private String renderedPrompt;
    
    /**
     * 系统提示词
     */
    private String systemPrompt;
    
    /**
     * 用户提示词
     */
    private String userPrompt;
    
    /**
     * 助手提示词
     */
    private String assistantPrompt;
    
    /**
     * 使用的参数
     */
    private Map<String, Object> usedParameters;
    
    /**
     * 模型响应
     */
    private String modelResponse;
    
    /**
     * 使用的模型
     */
    private String modelName;
    
    /**
     * 使用的模型类型
     */
    private String modelType;
    
    /**
     * 令牌使用信息
     */
    private TokenUsage tokenUsage;
    
    /**
     * 执行时间（毫秒）
     */
    private Long executionTime;
    
    /**
     * 执行状态
     */
    private ExecutionStatus status;
    
    /**
     * 错误信息
     */
    private String errorMessage;
    
    /**
     * 错误代码
     */
    private String errorCode;
    
    /**
     * 缓存命中
     */
    private Boolean cacheHit;
    
    /**
     * 缓存键
     */
    private String cacheKey;
    
    /**
     * 元数据
     */
    private Map<String, Object> metadata;
    
    /**
     * 创建时间
     */
    private LocalDateTime createdTime;
    
    /**
     * 完成时间
     */
    private LocalDateTime completedTime;
    
    /**
     * 执行状态枚举
     */
    public enum ExecutionStatus {
        SUCCESS,     // 成功
        FAILED,      // 失败
        CANCELLED,   // 取消
        TIMEOUT,     // 超时
        RATE_LIMITED, // 限流
        CACHE_HIT,    // 缓存命中
        RETRY,       // 重试
        PARTIAL_SUCCESS // 部分成功
    }
    
    /**
     * 令牌使用信息
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TokenUsage {
        
        /**
         * 提示词令牌数
         */
        private Integer promptTokens;
        
        /**
         * 完成令牌数
         */
        private Integer completionTokens;
        
        /**
         * 总令牌数
         */
        private Integer totalTokens;
        
        /**
         * 成本（美元）
         */
        private Double cost;
        
        /**
         * 模型定价信息
         */
        private ModelPricing pricing;
    }
    
    /**
     * 模型定价信息
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ModelPricing {
        
        /**
         * 提示词价格（每1K令牌）
         */
        private Double promptPricePer1K;
        
        /**
         * 完成价格（每1K令牌）
         */
        private Double completionPricePer1K;
        
        /**
         * 货币单位
         */
        private String currency;
    }
    
    /**
     * 是否成功
     */
    public boolean isSuccess() {
        return ExecutionStatus.SUCCESS.equals(status);
    }
    
    /**
     * 是否失败
     */
    public boolean isFailed() {
        return ExecutionStatus.FAILED.equals(status) || 
               ExecutionStatus.TIMEOUT.equals(status) ||
               ExecutionStatus.RATE_LIMITED.equals(status);
    }
    
    /**
     * 是否缓存命中
     */
    public boolean isCacheHit() {
        return Boolean.TRUE.equals(cacheHit);
    }
    
    /**
     * 获取总成本
     */
    public Double getTotalCost() {
        if (tokenUsage != null && tokenUsage.getCost() != null) {
            return tokenUsage.getCost();
        }
        return 0.0;
    }
    
    /**
     * 获取总令牌数
     */
    public Integer getTotalTokens() {
        if (tokenUsage != null && tokenUsage.getTotalTokens() != null) {
            return tokenUsage.getTotalTokens();
        }
        return 0;
    }
    
    /**
     * 获取错误信息（如果有）
     */
    public String getErrorInfo() {
        if (errorMessage != null) {
            return String.format("[%s] %s", errorCode != null ? errorCode : "UNKNOWN", errorMessage);
        }
        return null;
    }
    
    /**
     * 创建成功响应
     */
    public static PromptExecutionResponse success(String requestId, String renderedPrompt, String modelResponse) {
        return PromptExecutionResponse.builder()
                .responseId(UUID.randomUUID().toString())
                .requestId(requestId)
                .renderedPrompt(renderedPrompt)
                .modelResponse(modelResponse)
                .status(ExecutionStatus.SUCCESS)
                .createdTime(LocalDateTime.now())
                .completedTime(LocalDateTime.now())
                .build();
    }
    
    /**
     * 创建失败响应
     */
    public static PromptExecutionResponse failure(String requestId, String errorMessage, String errorCode) {
        return PromptExecutionResponse.builder()
                .responseId(UUID.randomUUID().toString())
                .requestId(requestId)
                .errorMessage(errorMessage)
                .errorCode(errorCode)
                .status(ExecutionStatus.FAILED)
                .createdTime(LocalDateTime.now())
                .completedTime(LocalDateTime.now())
                .build();
    }
    
    /**
     * 创建缓存命中响应
     */
    public static PromptExecutionResponse cacheHit(String requestId, String cacheKey, String cachedResponse) {
        return PromptExecutionResponse.builder()
                .responseId(UUID.randomUUID().toString())
                .requestId(requestId)
                .modelResponse(cachedResponse)
                .cacheHit(true)
                .cacheKey(cacheKey)
                .status(ExecutionStatus.CACHE_HIT)
                .createdTime(LocalDateTime.now())
                .completedTime(LocalDateTime.now())
                .build();
    }
}