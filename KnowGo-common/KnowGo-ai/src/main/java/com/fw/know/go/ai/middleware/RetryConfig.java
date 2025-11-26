package com.fw.know.go.ai.middleware;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description 重试配置
 * @Date 24/11/2025 下午4:37
 * @Author Leo
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RetryConfig {
    
    /**
     * 最大重试次数
     */
    private Integer maxRetries;
    
    /**
     * 初始重试间隔（毫秒）
     */
    private Long initialInterval;
    
    /**
     * 最大重试间隔（毫秒）
     */
    private Long maxInterval;
    
    /**
     * 重试间隔倍数
     */
    private Double multiplier;
    
    /**
     * 需要重试的异常类型
     */
    private java.util.List<Class<? extends Exception>> retryableExceptions;
    
    /**
     * 不重试的异常类型
     */
    private java.util.List<Class<? extends Exception>> nonRetryableExceptions;
    
    /**
     * 是否启用指数退避
     */
    private Boolean enableExponentialBackoff;
    
    /**
     * 是否启用随机抖动
     */
    private Boolean enableJitter;
    
    /**
     * 默认配置
     */
    public static RetryConfig defaultConfig() {
        return RetryConfig.builder()
                .maxRetries(3)
                .initialInterval(1000L)
                .maxInterval(30000L)
                .multiplier(2.0)
                .enableExponentialBackoff(true)
                .enableJitter(true)
                .retryableExceptions(java.util.Arrays.asList(
                        java.net.SocketTimeoutException.class,
                        java.net.ConnectException.class,
                        java.io.IOException.class
                ))
                .nonRetryableExceptions(java.util.Arrays.asList(
                        IllegalArgumentException.class,
                        SecurityException.class
                ))
                .build();
    }
}