package com.fw.know.go.order.sharding.id;

import org.redisson.api.RAtomicLong;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;

/**
 * @Description
 * @Date 2/4/2026 下午1:32
 * @Author Leo
 */
public class WorkerIdHolder implements CommandLineRunner {

    private final RedissonClient redissonClient;

    @Value("${order.client.name:workerId}")
    private String clientName;

    public static long WORKER_ID;

    public WorkerIdHolder(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    @Override
    public void run(String... args) throws Exception {
        RAtomicLong atomicLong = redissonClient.getAtomicLong(clientName);
        WORKER_ID = atomicLong.incrementAndGet() % 32;
    }
}
