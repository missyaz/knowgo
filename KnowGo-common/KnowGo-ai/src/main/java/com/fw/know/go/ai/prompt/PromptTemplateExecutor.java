package com.fw.know.go.ai.prompt;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * @Description 提示词模板执行器
 * @Date 24/11/2025 下午5:20
 * @Author Leo
 */
public interface PromptTemplateExecutor {
    
    /**
     * 执行模板
     */
    PromptExecutionResponse execute(PromptExecutionRequest request);
    
    /**
     * 异步执行模板
     */
    CompletableFuture<PromptExecutionResponse> executeAsync(PromptExecutionRequest request);
    
    /**
     * 执行模板并渲染
     */
    String executeAndRender(PromptExecutionRequest request);
    
    /**
     * 批量执行模板
     */
    Map<String, PromptExecutionResponse> executeBatch(Map<String, PromptExecutionRequest> requests);
    
    /**
     * 验证执行请求
     */
    boolean validateRequest(PromptExecutionRequest request);
    
    /**
     * 获取执行统计信息
     */
    ExecutionStatistics getExecutionStatistics();
    
    /**
     * 取消执行
     */
    boolean cancelExecution(String requestId);
    
    /**
     * 获取执行状态
     */
    ExecutionStatus getExecutionStatus(String requestId);
    
    /**
     * 执行统计信息
     */
    class ExecutionStatistics {
        private long totalExecutions;
        private long successfulExecutions;
        private long failedExecutions;
        private long cachedExecutions;
        private long averageExecutionTime;
        private long totalExecutionTime;
        private double successRate;
        private double cacheHitRate;
        
        // Getters and setters
        public long getTotalExecutions() {
            return totalExecutions;
        }
        
        public void setTotalExecutions(long totalExecutions) {
            this.totalExecutions = totalExecutions;
        }
        
        public long getSuccessfulExecutions() {
            return successfulExecutions;
        }
        
        public void setSuccessfulExecutions(long successfulExecutions) {
            this.successfulExecutions = successfulExecutions;
        }
        
        public long getFailedExecutions() {
            return failedExecutions;
        }
        
        public void setFailedExecutions(long failedExecutions) {
            this.failedExecutions = failedExecutions;
        }
        
        public long getCachedExecutions() {
            return cachedExecutions;
        }
        
        public void setCachedExecutions(long cachedExecutions) {
            this.cachedExecutions = cachedExecutions;
        }
        
        public long getAverageExecutionTime() {
            return averageExecutionTime;
        }
        
        public void setAverageExecutionTime(long averageExecutionTime) {
            this.averageExecutionTime = averageExecutionTime;
        }
        
        public long getTotalExecutionTime() {
            return totalExecutionTime;
        }
        
        public void setTotalExecutionTime(long totalExecutionTime) {
            this.totalExecutionTime = totalExecutionTime;
        }
        
        public double getSuccessRate() {
            return successRate;
        }
        
        public void setSuccessRate(double successRate) {
            this.successRate = successRate;
        }
        
        public double getCacheHitRate() {
            return cacheHitRate;
        }
        
        public void setCacheHitRate(double cacheHitRate) {
            this.cacheHitRate = cacheHitRate;
        }
    }
    
    /**
     * 执行状态
     */
    enum ExecutionStatus {
        PENDING,     // 待执行
        RUNNING,       // 执行中
        COMPLETED,     // 已完成
        FAILED,        // 失败
        CANCELLED,     // 已取消
        TIMEOUT        // 超时
    }
}