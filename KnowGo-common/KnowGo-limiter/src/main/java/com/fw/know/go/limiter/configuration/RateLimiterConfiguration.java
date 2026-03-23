package com.fw.know.go.limiter.configuration;

import com.fw.know.go.limiter.SlidingWindowRateLimiter;
import org.redisson.api.RedissonClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description
 * @Date 23/3/2026 上午9:45
 * @Author Leo
 */
@Configuration
public class RateLimiterConfiguration {

    @Bean
    public SlidingWindowRateLimiter slidingWindowRateLimiter(RedissonClient redissonClient){
        return new SlidingWindowRateLimiter(redissonClient);
    }
}
