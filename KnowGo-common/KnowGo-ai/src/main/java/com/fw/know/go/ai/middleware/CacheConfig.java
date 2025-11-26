package com.fw.know.go.ai.middleware;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description 缓存配置
 * @Date 24/11/2025 下午4:50
 * @Author Leo
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CacheConfig {
    
    /**
     * 是否启用缓存
     */
    private Boolean enabled;
    
    /**
     * 缓存过期时间（秒）
     */
    private Long expirationSeconds;
    
    /**
     * 最大缓存条目数
     */
    private Integer maxSize;
    
    /**
     * 是否按模型缓存
     */
    private Boolean cacheByModel;
    
    /**
     * 是否按用户缓存
     */
    private Boolean cacheByUser;
    
    /**
     * 缓存键前缀
     */
    private String cacheKeyPrefix;
    
    /**
     * 默认配置
     */
    public static CacheConfig defaultConfig() {
        return CacheConfig.builder()
                .enabled(true)
                .expirationSeconds(3600L) // 1小时
                .maxSize(1000)
                .cacheByModel(true)
                .cacheByUser(false)
                .cacheKeyPrefix("ai:response:")
                .build();
    }
}