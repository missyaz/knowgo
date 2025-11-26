package com.fw.know.go.ai.middleware.impl;

import com.fw.know.go.ai.middleware.AIMiddleware;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @Description 监控中间件
 * @Date 24/11/2025 下午4:45
 * @Author Leo
 */
@Slf4j
@Component
public class MonitoringMiddleware implements AIMiddleware {
    
    private final Map<String, MetricData> metrics = new ConcurrentHashMap<>();
    private final AtomicLong totalRequests = new AtomicLong(0);
    private final AtomicLong totalErrors = new AtomicLong(0);
    private boolean enabled = true;
    
    @Override
    public <T, R> R execute(AIRequest<T> request, AIHandler<T, R> next) {
        if (!enabled) {
            return next.handle(request);
        }
        
        long startTime = System.currentTimeMillis();
        String modelName = request.getModelName();
        String requestId = request.getRequestId();
        
        totalRequests.incrementAndGet();
        
        // 记录请求开始
        log.info("AI request started - ID: {}, Model: {}, Type: {}", 
                requestId, modelName, request.getData().getClass().getSimpleName());
        
        try {
            R result = next.handle(request);
            long duration = System.currentTimeMillis() - startTime;
            
            // 记录成功指标
            recordSuccess(modelName, duration);
            
            log.info("AI request completed - ID: {}, Model: {}, Duration: {}ms", 
                    requestId, modelName, duration);
            
            return result;
        } catch (Exception e) {
            long duration = System.currentTimeMillis() - startTime;
            totalErrors.incrementAndGet();
            
            // 记录错误指标
            recordError(modelName, duration, e);
            
            log.error("AI request failed - ID: {}, Model: {}, Duration: {}ms, Error: {}", 
                    requestId, modelName, duration, e.getMessage(), e);
            
            throw e;
        }
    }
    
    private void recordSuccess(String modelName, long duration) {
        MetricData metric = metrics.computeIfAbsent(modelName, k -> new MetricData());
        metric.recordSuccess(duration);
    }
    
    private void recordError(String modelName, long duration, Exception error) {
        MetricData metric = metrics.computeIfAbsent(modelName, k -> new MetricData());
        metric.recordError(duration, error);
    }
    
    @Override
    public String getName() {
        return "monitoring";
    }
    
    @Override
    public int getPriority() {
        return 50;
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
    
    /**
     * 获取监控数据
     */
    public MonitoringData getMonitoringData() {
        Map<String, ModelMetric> modelMetrics = new ConcurrentHashMap<>();
        
        metrics.forEach((modelName, metricData) -> {
            modelMetrics.put(modelName, ModelMetric.builder()
                    .totalRequests(metricData.totalRequests.get())
                    .successfulRequests(metricData.successfulRequests.get())
                    .failedRequests(metricData.failedRequests.get())
                    .averageResponseTime(metricData.getAverageResponseTime())
                    .minResponseTime(metricData.minResponseTime.get())
                    .maxResponseTime(metricData.maxResponseTime.get())
                    .errorRate(metricData.getErrorRate())
                    .lastErrorTime(metricData.lastErrorTime.get())
                    .lastErrorMessage(metricData.lastErrorMessage)
                    .build());
        });
        
        return MonitoringData.builder()
                .totalRequests(totalRequests.get())
                .totalErrors(totalErrors.get())
                .overallErrorRate(totalRequests.get() > 0 ? 
                        (double) totalErrors.get() / totalRequests.get() * 100 : 0.0)
                .modelMetrics(modelMetrics)
                .build();
    }
    
    /**
     * 重置监控数据
     */
    public void resetMetrics() {
        metrics.clear();
        totalRequests.set(0);
        totalErrors.set(0);
        log.info("Monitoring metrics reset");
    }
    
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MonitoringData {
        private Long totalRequests;
        private Long totalErrors;
        private Double overallErrorRate;
        private Map<String, ModelMetric> modelMetrics;
    }
    
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ModelMetric {
        private Long totalRequests;
        private Long successfulRequests;
        private Long failedRequests;
        private Double averageResponseTime;
        private Long minResponseTime;
        private Long maxResponseTime;
        private Double errorRate;
        private Long lastErrorTime;
        private String lastErrorMessage;
    }
    
    private static class MetricData {
        private final AtomicLong totalRequests = new AtomicLong(0);
        private final AtomicLong successfulRequests = new AtomicLong(0);
        private final AtomicLong failedRequests = new AtomicLong(0);
        private final AtomicLong totalResponseTime = new AtomicLong(0);
        private final AtomicLong minResponseTime = new AtomicLong(Long.MAX_VALUE);
        private final AtomicLong maxResponseTime = new AtomicLong(0);
        private final AtomicLong lastErrorTime = new AtomicLong(0);
        private volatile String lastErrorMessage;
        
        void recordSuccess(long duration) {
            totalRequests.incrementAndGet();
            successfulRequests.incrementAndGet();
            totalResponseTime.addAndGet(duration);
            updateMinMaxTime(duration);
        }
        
        void recordError(long duration, Exception error) {
            totalRequests.incrementAndGet();
            failedRequests.incrementAndGet();
            totalResponseTime.addAndGet(duration);
            updateMinMaxTime(duration);
            lastErrorTime.set(System.currentTimeMillis());
            lastErrorMessage = error.getMessage();
        }
        
        private void updateMinMaxTime(long duration) {
            long currentMin = minResponseTime.get();
            while (duration < currentMin && !minResponseTime.compareAndSet(currentMin, duration)) {
                currentMin = minResponseTime.get();
            }
            
            long currentMax = maxResponseTime.get();
            while (duration > currentMax && !maxResponseTime.compareAndSet(currentMax, duration)) {
                currentMax = maxResponseTime.get();
            }
        }
        
        double getAverageResponseTime() {
            long totalTime = totalResponseTime.get();
            long requests = totalRequests.get();
            return requests > 0 ? (double) totalTime / requests : 0.0;
        }
        
        double getErrorRate() {
            long requests = totalRequests.get();
            return requests > 0 ? (double) failedRequests.get() / requests * 100 : 0.0;
        }
    }
}