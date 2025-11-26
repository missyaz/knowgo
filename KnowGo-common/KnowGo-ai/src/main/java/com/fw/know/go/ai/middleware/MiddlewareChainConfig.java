package com.fw.know.go.ai.middleware;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Description 中间件链配置
 * @Date 24/11/2025 下午4:47
 * @Author Leo
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MiddlewareChainConfig {
    
    /**
     * 是否启用重试
     */
    private Boolean enableRetry;
    
    /**
     * 重试配置
     */
    private RetryConfig retryConfig;
    
    /**
     * 是否启用熔断器
     */
    private Boolean enableCircuitBreaker;
    
    /**
     * 熔断器配置
     */
    private CircuitBreakerConfig circuitBreakerConfig;
    
    /**
     * 是否启用监控
     */
    private Boolean enableMonitoring;
    
    /**
     * 是否启用限流
     */
    private Boolean enableRateLimiting;
    
    /**
     * 限流配置
     */
    private RateLimitConfig rateLimitConfig;
    
    /**
     * 是否启用缓存
     */
    private Boolean enableCaching;
    
    /**
     * 缓存配置
     */
    private CacheConfig cacheConfig;
    
    /**
     * 自定义中间件类名
     */
    private List<String> customMiddlewareClasses;
    
    /**
     * 默认配置
     */
    public static MiddlewareChainConfig defaultConfig() {
        return MiddlewareChainConfig.builder()
                .enableRetry(true)
                .retryConfig(RetryConfig.defaultConfig())
                .enableCircuitBreaker(true)
                .circuitBreakerConfig(CircuitBreakerConfig.defaultConfig())
                .enableMonitoring(true)
                .enableRateLimiting(false)
                .enableCaching(false)
                .build();
    }
}