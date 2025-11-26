package com.fw.know.go.ai.middleware.impl;

import com.fw.know.go.ai.middleware.AIMiddleware;
import com.fw.know.go.ai.middleware.CircuitBreakerConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @Description 熔断器中间件
 * @Date 24/11/2025 下午4:43
 * @Author Leo
 */
@Slf4j
@Component
public class CircuitBreakerMiddleware implements AIMiddleware {
    
    private enum State {
        CLOSED, OPEN, HALF_OPEN
    }
    
    private final CircuitBreakerConfig config;
    private State state = State.CLOSED;
    private final AtomicInteger failureCount = new AtomicInteger(0);
    private final AtomicInteger successCount = new AtomicInteger(0);
    private final AtomicLong lastFailureTime = new AtomicLong(0);
    private final AtomicInteger halfOpenRequests = new AtomicInteger(0);
    private boolean enabled = true;
    
    public CircuitBreakerMiddleware() {
        this.config = CircuitBreakerConfig.defaultConfig();
    }
    
    public CircuitBreakerMiddleware(CircuitBreakerConfig config) {
        this.config = config != null ? config : CircuitBreakerConfig.defaultConfig();
    }
    
    @Override
    public <T, R> R execute(AIRequest<T> request, AIHandler<T, R> next) {
        if (!enabled) {
            return next.handle(request);
        }
        
        if (state == State.OPEN) {
            if (shouldAttemptReset()) {
                transitionToHalfOpen();
            } else {
                throw new RuntimeException("Circuit breaker is OPEN for request: " + request.getRequestId());
            }
        }
        
        long startTime = System.currentTimeMillis();
        
        try {
            R result = next.handle(request);
            long duration = System.currentTimeMillis() - startTime;
            
            onSuccess(duration);
            return result;
        } catch (Exception e) {
            long duration = System.currentTimeMillis() - startTime;
            onFailure(duration, e);
            throw e;
        }
    }
    
    private synchronized void onSuccess(long duration) {
        if (state == State.HALF_OPEN) {
            int requests = halfOpenRequests.incrementAndGet();
            if (requests >= config.getPermittedNumberOfCallsInHalfOpenState()) {
                if (successCount.get() >= config.getPermittedNumberOfCallsInHalfOpenState()) {
                    transitionToClosed();
                } else {
                    transitionToOpen();
                }
            }
        } else if (state == State.CLOSED) {
            successCount.incrementAndGet();
            checkThreshold();
        }
    }
    
    private synchronized void onFailure(long duration, Exception e) {
        lastFailureTime.set(System.currentTimeMillis());
        
        if (state == State.HALF_OPEN) {
            transitionToOpen();
        } else if (state == State.CLOSED) {
            failureCount.incrementAndGet();
            checkThreshold();
        }
    }
    
    private void checkThreshold() {
        int totalCalls = successCount.get() + failureCount.get();
        
        if (totalCalls >= config.getMinimumNumberOfCalls()) {
            double failureRate = (double) failureCount.get() / totalCalls * 100;
            
            if (failureRate >= config.getFailureRateThreshold()) {
                transitionToOpen();
            }
        }
    }
    
    private boolean shouldAttemptReset() {
        long timeSinceLastFailure = System.currentTimeMillis() - lastFailureTime.get();
        return timeSinceLastFailure >= config.getOpenDuration();
    }
    
    private void transitionToOpen() {
        state = State.OPEN;
        failureCount.set(0);
        successCount.set(0);
        halfOpenRequests.set(0);
        lastFailureTime.set(System.currentTimeMillis());
        log.warn("Circuit breaker transitioned to OPEN state");
    }
    
    private void transitionToHalfOpen() {
        state = State.HALF_OPEN;
        halfOpenRequests.set(0);
        log.info("Circuit breaker transitioned to HALF_OPEN state");
    }
    
    private void transitionToClosed() {
        state = State.CLOSED;
        failureCount.set(0);
        successCount.set(0);
        halfOpenRequests.set(0);
        log.info("Circuit breaker transitioned to CLOSED state");
    }
    
    @Override
    public String getName() {
        return "circuit-breaker";
    }
    
    @Override
    public int getPriority() {
        return 200;
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
    
    public State getState() {
        return state;
    }
    
    public double getFailureRate() {
        int totalCalls = successCount.get() + failureCount.get();
        if (totalCalls == 0) {
            return 0.0;
        }
        return (double) failureCount.get() / totalCalls * 100;
    }
}