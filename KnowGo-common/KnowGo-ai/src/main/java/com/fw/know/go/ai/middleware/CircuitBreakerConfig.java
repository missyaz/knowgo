package com.fw.know.go.ai.middleware;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description 熔断器配置
 * @Date 24/11/2025 下午4:39
 * @Author Leo
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CircuitBreakerConfig {
    
    /**
     * 失败率阈值（百分比）
     */
    private Double failureRateThreshold;
    
    /**
     * 滑动窗口大小
     */
    private Integer slidingWindowSize;
    
    /**
     * 最小请求数量
     */
    private Integer minimumNumberOfCalls;
    
    /**
     * 熔断持续时间（毫秒）
     */
    private Long openDuration;
    
    /**
     * 半开状态允许的最大请求数
     */
    private Integer permittedNumberOfCallsInHalfOpenState;
    
    /**
     * 半开状态持续时间（毫秒）
     */
    private Long halfOpenDuration;
    
    /**
     * 慢调用阈值（毫秒）
     */
    private Long slowCallThreshold;
    
    /**
     * 慢调用率阈值（百分比）
     */
    private Double slowCallRateThreshold;
    
    /**
     * 是否启用自动熔断
     */
    private Boolean enableAutoCircuitBreaker;
    
    /**
     * 默认配置
     */
    public static CircuitBreakerConfig defaultConfig() {
        return CircuitBreakerConfig.builder()
                .failureRateThreshold(50.0)
                .slidingWindowSize(10)
                .minimumNumberOfCalls(5)
                .openDuration(60000L)
                .permittedNumberOfCallsInHalfOpenState(3)
                .halfOpenDuration(30000L)
                .slowCallThreshold(5000L)
                .slowCallRateThreshold(50.0)
                .enableAutoCircuitBreaker(true)
                .build();
    }
}