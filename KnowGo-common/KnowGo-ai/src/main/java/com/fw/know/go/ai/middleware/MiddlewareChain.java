package com.fw.know.go.ai.middleware;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @Description 中间件链
 * @Date 24/11/2025 下午4:51
 * @Author Leo
 */
public class MiddlewareChain {
    
    private final List<AIMiddleware> middlewares = new CopyOnWriteArrayList<>();
    private final MiddlewareChainConfig config;
    
    public MiddlewareChain() {
        this(MiddlewareChainConfig.defaultConfig());
    }
    
    public MiddlewareChain(MiddlewareChainConfig config) {
        this.config = config != null ? config : MiddlewareChainConfig.defaultConfig();
        initializeDefaultMiddlewares();
    }
    
    /**
     * 执行中间件链
     */
    public <T, R> R execute(AIRequest<T> request, AIHandler<T, R> finalHandler) {
        if (middlewares.isEmpty()) {
            try {
                return finalHandler.handle(request);
            } catch (Exception e) {
                throw new RuntimeException("Handler execution failed", e);
            }
        }
        
        return executeMiddleware(request, finalHandler, 0);
    }
    
    private <T, R> R executeMiddleware(AIRequest<T> request, AIHandler<T, R> finalHandler, int index) {
        if (index >= middlewares.size()) {
            try {
                return finalHandler.handle(request);
            } catch (Exception e) {
                throw new RuntimeException("Handler execution failed", e);
            }
        }
        
        AIMiddleware middleware = middlewares.get(index);
        if (!middleware.isEnabled()) {
            return executeMiddleware(request, finalHandler, index + 1);
        }
        
        return middleware.execute(request, new AIHandler<T, R>() {
            @Override
            public R handle(AIRequest<T> request) throws Exception {
                return executeMiddleware(request, finalHandler, index + 1);
            }
        });
    }
    
    /**
     * 添加中间件
     */
    public void addMiddleware(AIMiddleware middleware) {
        middlewares.add(middleware);
        sortMiddlewares();
    }
    
    /**
     * 移除中间件
     */
    public void removeMiddleware(String name) {
        middlewares.removeIf(middleware -> middleware.getName().equals(name));
    }
    
    /**
     * 获取中间件
     */
    public AIMiddleware getMiddleware(String name) {
        return middlewares.stream()
                .filter(middleware -> middleware.getName().equals(name))
                .findFirst()
                .orElse(null);
    }
    
    /**
     * 启用中间件
     */
    public void enableMiddleware(String name) {
        middlewares.stream()
                .filter(middleware -> middleware.getName().equals(name))
                .forEach(AIMiddleware::enable);
    }
    
    /**
     * 禁用中间件
     */
    public void disableMiddleware(String name) {
        middlewares.stream()
                .filter(middleware -> middleware.getName().equals(name))
                .forEach(AIMiddleware::disable);
    }
    
    /**
     * 获取所有中间件名称
     */
    public List<String> getMiddlewareNames() {
        return middlewares.stream()
                .map(AIMiddleware::getName)
                .collect(java.util.stream.Collectors.toList());
    }
    
    /**
     * 清空所有中间件
     */
    public void clear() {
        middlewares.clear();
    }
    
    /**
     * 初始化默认中间件
     */
    private void initializeDefaultMiddlewares() {
        // 监控中间件（最高优先级）
        if (config.getEnableMonitoring() != null && config.getEnableMonitoring()) {
            addMiddleware(new com.fw.know.go.ai.middleware.impl.MonitoringMiddleware());
        }
        
        // 重试中间件
        if (config.getEnableRetry() != null && config.getEnableRetry()) {
            RetryConfig retryConfig = config.getRetryConfig() != null ? 
                    config.getRetryConfig() : RetryConfig.defaultConfig();
            addMiddleware(new com.fw.know.go.ai.middleware.impl.RetryMiddleware(retryConfig));
        }
        
        // 熔断器中间件
        if (config.getEnableCircuitBreaker() != null && config.getEnableCircuitBreaker()) {
            CircuitBreakerConfig cbConfig = config.getCircuitBreakerConfig() != null ? 
                    config.getCircuitBreakerConfig() : CircuitBreakerConfig.defaultConfig();
            addMiddleware(new com.fw.know.go.ai.middleware.impl.CircuitBreakerMiddleware(cbConfig));
        }
    }
    
    /**
     * 按优先级排序中间件
     */
    private void sortMiddlewares() {
        middlewares.sort((m1, m2) -> Integer.compare(m1.getPriority(), m2.getPriority()));
    }
    
    /**
     * 创建默认请求
     */
    public static <T> AIRequest<T> createRequest(T data, String requestId, String modelName) {
        return new AIRequest<T>() {
            @Override
            public T getData() {
                return data;
            }
            
            @Override
            public String getRequestId() {
                return requestId;
            }
            
            @Override
            public String getModelName() {
                return modelName;
            }
            
            @Override
            public Long getTimestamp() {
                return System.currentTimeMillis();
            }
            
            @Override
            public java.util.Map<String, Object> getMetadata() {
                return new java.util.HashMap<>();
            }
        };
    }
}