package com.fw.know.go.ai.middleware;

/**
 * @Description AI调用中间件接口
 * @Date 24/11/2025 下午4:35
 * @Author Leo
 */
public interface AIMiddleware {
    
    /**
     * 执行AI调用
     * @param request 请求
     * @param next 下一个处理器
     * @return 响应
     */
    <T, R> R execute(AIRequest<T> request, AIHandler<T, R> next);
    
    /**
     * 获取中间件名称
     * @return 名称
     */
    String getName();
    
    /**
     * 获取中间件优先级
     * @return 优先级
     */
    int getPriority();
    
    /**
     * 是否启用
     * @return 是否启用
     */
    boolean isEnabled();
    
    /**
     * 启用中间件
     */
    void enable();
    
    /**
     * 禁用中间件
     */
    void disable();
    
    /**
     * AI请求包装器
     */
    interface AIRequest<T> {
        T getData();
        String getRequestId();
        String getModelName();
        Long getTimestamp();
        java.util.Map<String, Object> getMetadata();
    }
    
    /**
     * AI处理器接口
     */
    interface AIHandler<T, R> {
        R handle(AIRequest<T> request) throws Exception;
    }
}