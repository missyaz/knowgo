package com.fw.know.go.ai.prompt;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * @Description 提示词模板执行请求
 * @Date 24/11/2025 下午5:20
 * @Author Leo
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PromptExecutionRequest {
    
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
     * 模板参数
     */
    private Map<String, Object> parameters;
    
    /**
     * 用户ID
     */
    private String userId;
    
    /**
     * 会话ID
     */
    private String sessionId;
    
    /**
     * 模型类型
     */
    private String modelType;
    
    /**
     * 模型名称
     */
    private String modelName;
    
    /**
     * 系统提示词（覆盖模板）
     */
    private String systemPrompt;
    
    /**
     * 用户提示词（覆盖模板）
     */
    private String userPrompt;
    
    /**
     * 助手提示词（覆盖模板）
     */
    private String assistantPrompt;
    
    /**
     * 温度参数
     */
    private Double temperature;
    
    /**
     * 最大令牌数
     */
    private Integer maxTokens;
    
    /**
     * 顶部P参数
     */
    private Double topP;
    
    /**
     * 频率惩罚参数
     */
    private Double frequencyPenalty;
    
    /**
     * 存在惩罚参数
     */
    private Double presencePenalty;
    
    /**
     * 停止序列
     */
    private String stopSequences;
    
    /**
     * 是否流式响应
     */
    private Boolean stream;
    
    /**
     * 上下文信息
     */
    private String context;
    
    /**
     * 历史消息
     */
    private String messageHistory;
    
    /**
     * 元数据
     */
    private Map<String, Object> metadata;
    
    /**
     * 创建时间
     */
    private LocalDateTime createdTime;
    
    /**
     * 执行选项
     */
    private ExecutionOptions executionOptions;
    
    /**
     * 执行选项
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ExecutionOptions {
        
        /**
         * 是否启用缓存
         */
        private Boolean enableCache;
        
        /**
         * 缓存键
         */
        private String cacheKey;
        
        /**
         * 缓存过期时间（秒）
         */
        private Integer cacheExpiration;
        
        /**
         * 是否启用重试
         */
        private Boolean enableRetry;
        
        /**
         * 最大重试次数
         */
        private Integer maxRetries;
        
        /**
         * 是否启用熔断
         */
        private Boolean enableCircuitBreaker;
        
        /**
         * 是否启用监控
         */
        private Boolean enableMonitoring;
        
        /**
         * 超时时间（毫秒）
         */
        private Long timeout;
        
        /**
         * 优先级
         */
        private Integer priority;
        
        /**
         * 标签
         */
        private String tags;
        
        /**
         * 自定义头部
         */
        private Map<String, String> customHeaders;
        
        /**
         * 自定义参数
         */
        private Map<String, Object> customParameters;
    }
    
    /**
     * 获取最终系统提示词
     */
    public String getFinalSystemPrompt() {
        return systemPrompt != null ? systemPrompt : "";
    }
    
    /**
     * 获取最终用户提示词
     */
    public String getFinalUserPrompt() {
        return userPrompt != null ? userPrompt : "";
    }
    
    /**
     * 获取最终助手提示词
     */
    public String getFinalAssistantPrompt() {
        return assistantPrompt != null ? assistantPrompt : "";
    }
    
    /**
     * 验证请求
     */
    public boolean validate() {
        // 验证模板ID或名称
        if ((templateId == null || templateId.isEmpty()) && (templateName == null || templateName.isEmpty())) {
            return false;
        }
        
        // 验证用户ID
        if (userId == null || userId.isEmpty()) {
            return false;
        }
        
        // 验证模型类型
        if (modelType == null || modelType.isEmpty()) {
            return false;
        }
        
        return true;
    }
    
    /**
     * 获取缓存键
     */
    public String getCacheKey() {
        if (executionOptions != null && executionOptions.getCacheKey() != null) {
            return executionOptions.getCacheKey();
        }
        
        // 生成默认缓存键
        StringBuilder cacheKeyBuilder = new StringBuilder();
        cacheKeyBuilder.append("prompt:");
        
        if (templateId != null) {
            cacheKeyBuilder.append("template:").append(templateId);
        } else if (templateName != null) {
            cacheKeyBuilder.append("template:").append(templateName);
        }
        
        if (parameters != null && !parameters.isEmpty()) {
            cacheKeyBuilder.append(":params:").append(parameters.hashCode());
        }
        
        if (systemPrompt != null) {
            cacheKeyBuilder.append(":sys:").append(systemPrompt.hashCode());
        }
        
        if (userPrompt != null) {
            cacheKeyBuilder.append(":user:").append(userPrompt.hashCode());
        }
        
        return cacheKeyBuilder.toString();
    }
    
    /**
     * 获取执行选项（确保不为null）
     */
    public ExecutionOptions getExecutionOptionsOrDefault() {
        if (executionOptions == null) {
            executionOptions = ExecutionOptions.builder()
                    .enableCache(true)
                    .enableRetry(true)
                    .enableCircuitBreaker(true)
                    .enableMonitoring(true)
                    .timeout(30000L) // 30秒
                    .priority(5)
                    .build();
        }
        return executionOptions;
    }
}