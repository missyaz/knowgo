package com.fw.know.go.limiter;

import java.util.concurrent.TimeUnit;

/**
 * @Description 限流服务
 * @Date 23/3/2026 上午9:45
 * @Author Leo
 */
public interface RateLimiter {


    /**
     * 判断一个key是否可以通过
     *
     * @param key 用于标识资源的键
     * @param limit 限流的数量
     * @param windowSize 限流的时间窗口大小，单位为秒
     * @return 如果成功获取许可返回true，否则返回false
     */
    Boolean tryAcquire(String key, int limit, int windowSize);
}
