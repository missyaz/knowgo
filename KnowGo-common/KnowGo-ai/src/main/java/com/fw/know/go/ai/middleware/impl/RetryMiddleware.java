package com.fw.know.go.ai.middleware.impl;

import com.fw.know.go.ai.middleware.AIMiddleware;
import com.fw.know.go.ai.middleware.RetryConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Random;

/**
 * @Description 重试中间件
 * @Date 24/11/2025 下午4:41
 * @Author Leo
 */
@Slf4j
@Component
public class RetryMiddleware implements AIMiddleware {
    
    private final RetryConfig config;
    private final Random random = new Random();
    private boolean enabled = true;
    
    public RetryMiddleware() {
        this.config = RetryConfig.defaultConfig();
    }
    
    public RetryMiddleware(RetryConfig config) {
        this.config = config != null ? config : RetryConfig.defaultConfig();
    }
    
    @Override
    public <T, R> R execute(AIRequest<T> request, AIHandler<T, R> next) {
        int attempt = 0;
        Exception lastException = null;
        
        while (attempt <= config.getMaxRetries()) {
            try {
                log.debug("Attempt {} for request {}", attempt + 1, request.getRequestId());
                R result = next.handle(request);
                
                if (attempt > 0) {
                    log.info("Request {} succeeded after {} attempts", request.getRequestId(), attempt + 1);
                }
                
                return result;
            } catch (Exception e) {
                lastException = e;
                
                if (shouldNotRetry(e) || attempt >= config.getMaxRetries()) {
                    log.error("Request {} failed permanently after {} attempts", request.getRequestId(), attempt + 1, e);
                    throw new RuntimeException("Request failed after " + (attempt + 1) + " attempts", e);
                }
                
                long waitTime = calculateWaitTime(attempt);
                log.warn("Request {} failed on attempt {}, retrying in {} ms. Error: {}", 
                        request.getRequestId(), attempt + 1, waitTime, e.getMessage());
                
                try {
                    Thread.sleep(waitTime);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException("Retry interrupted", ie);
                }
                
                attempt++;
            }
        }
        
        throw new RuntimeException("Request failed after " + (attempt + 1) + " attempts", lastException);
    }
    
    private boolean shouldNotRetry(Exception e) {
        if (config.getNonRetryableExceptions() != null) {
            for (Class<? extends Exception> nonRetryable : config.getNonRetryableExceptions()) {
                if (nonRetryable.isInstance(e)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    private long calculateWaitTime(int attempt) {
        long waitTime = config.getInitialInterval();
        
        if (config.getEnableExponentialBackoff()) {
            waitTime = (long) (waitTime * Math.pow(config.getMultiplier(), attempt));
        }
        
        if (waitTime > config.getMaxInterval()) {
            waitTime = config.getMaxInterval();
        }
        
        if (config.getEnableJitter()) {
            waitTime = waitTime / 2 + random.nextInt((int) (waitTime / 2));
        }
        
        return waitTime;
    }
    
    @Override
    public String getName() {
        return "retry";
    }
    
    @Override
    public int getPriority() {
        return 100;
    }
    
    @Override
    public boolean isEnabled() {
        return enabled;
    }
    
    @Override
    public void enable() {
        this.enabled = true;
    }
    
    @Override
    public void disable() {
        this.enabled = false;
    }
}