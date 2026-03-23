package com.fw.know.go.limiter;

import org.redisson.api.RRateLimiter;
import org.redisson.api.RateIntervalUnit;
import org.redisson.api.RateType;
import org.redisson.api.RedissonClient;

/**
 * @Description 滑动窗口限流服务
 * @Date 23/3/2026 上午9:47
 * @Author Leo
 */
public class SlidingWindowRateLimiter implements RateLimiter{

    private final RedissonClient redissonClient;

    public static final String LIMIT_KEY_PREFIX = "know:go:limit:";

    public SlidingWindowRateLimiter(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    @Override
    public Boolean tryAcquire(String key, int limit, int windowSize) {
        RRateLimiter rateLimiter = redissonClient.getRateLimiter(LIMIT_KEY_PREFIX + key);

        if (!rateLimiter.isExists()){
            rateLimiter.trySetRate(RateType.OVERALL, limit, windowSize, RateIntervalUnit.SECONDS);
        }
        return rateLimiter.tryAcquire();
    }
}
