package com.fw.know.go.cache.configuration;

import com.alicp.jetcache.anno.config.EnableMethodCache;
import org.springframework.context.annotation.Configuration;

/**
 * @Description 缓存配置类
 * @Date 28/11/2025 下午2:55
 * @Author Leo
 */
@Configuration
@EnableMethodCache(basePackages = "com.fw.know.go")
public class CacheConfiguration {
}
