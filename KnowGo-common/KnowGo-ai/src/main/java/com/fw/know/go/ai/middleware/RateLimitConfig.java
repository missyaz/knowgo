package com.fw.know.go.ai.middleware;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description 限流配置
 * @Date 24/11/2025 下午4:49
 * @Author Leo
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RateLimitConfig {
    
    /**
     * 每秒最大请求数
     */
    private Integer requestsPerSecond;
    
    /**
     * 每分钟最大请求数
     */
    private Integer requestsPerMinute;
    
    /**
     * 每小时最大请求数
     */
    private Integer requestsPerHour;
    
    /**
     * 每天最大请求数
     */
    private Integer requestsPerDay;
    
    /**
     * 是否按模型限流
     */
    private Boolean rateLimitByModel;
    
    /**
     * 是否按用户限流
     */
    private Boolean rateLimitByUser;
    
    /**
     * 是否按IP限流
     */
    private Boolean rateLimitByIp;
    
    /**
     * 默认配置
     */
    public static RateLimitConfig defaultConfig() {
        return RateLimitConfig.builder()
                .requestsPerSecond(10)
                .requestsPerMinute(100)
                .requestsPerHour(1000)
                .requestsPerDay(10000)
                .rateLimitByModel(true)
                .rateLimitByUser(true)
                .rateLimitByIp(false)
                .build();
    }
}