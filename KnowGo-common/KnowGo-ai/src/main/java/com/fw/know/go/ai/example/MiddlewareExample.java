package com.fw.know.go.ai.example;

import com.fw.know.go.ai.middleware.AIMiddleware;
import com.fw.know.go.ai.middleware.AIMiddlewareChain;
import com.fw.know.go.ai.middleware.impl.RetryMiddleware;
import com.fw.know.go.ai.middleware.impl.CircuitBreakerMiddleware;
import com.fw.know.go.ai.middleware.impl.MonitoringMiddleware;
import com.fw.know.go.ai.middleware.impl.RateLimitMiddleware;
import com.fw.know.go.ai.core.AIRequest;
import com.fw.know.go.ai.core.AIResponse;

import java.time.Duration;
import java.util.Map;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.CompletableFuture;

/**
 * AI中间件使用示例
 */
public class MiddlewareExample {
    
    private final AIMiddlewareChain middlewareChain;
    private final AtomicInteger requestCounter = new AtomicInteger(0);
    
    public MiddlewareExample() {
        this.middlewareChain = new AIMiddlewareChain();
        setupMiddleware();
    }
    
    /**
     * 配置中间件链
     */
    private void setupMiddleware() {
        // 1. 重试中间件 - 处理临时失败
        RetryMiddleware retryMiddleware = new RetryMiddleware(
            3,                      // 最大重试次数
            Duration.ofSeconds(1),  // 初始间隔
            Duration.ofSeconds(10), // 最大间隔
            2.0,                    // 退避乘数
            true                    // 启用抖动
        );
        
        // 2. 熔断中间件 - 防止级联失败
        CircuitBreakerMiddleware circuitBreaker = new CircuitBreakerMiddleware(
            5,                      // 失败阈值
            Duration.ofSeconds(30), // 熔断持续时间
            0.5,                    // 失败率阈值
            3                       // 最小调用次数
        );
        
        // 3. 监控中间件 - 收集性能指标
        MonitoringMiddleware monitoring = new MonitoringMiddleware(
            true,                   // 启用请求指标
            true,                   // 启用响应指标
            true,                   // 启用错误指标
            Duration.ofMinutes(1)    // 指标导出间隔
        );
        
        // 4. 限流中间件 - 控制请求频率
        RateLimitMiddleware rateLimit = new RateLimitMiddleware(
            100,                    // 每分钟最大请求数
            Duration.ofMinutes(1),  // 时间窗口
            true                    // 公平分发
        );
        
        // 按优先级添加中间件（优先级高的先执行）
        middlewareChain.addMiddleware(monitoring, 100);      // 最高优先级 - 监控所有请求
        middlewareChain.addMiddleware(rateLimit, 80);        // 第二优先级 - 限流保护
        middlewareChain.addMiddleware(circuitBreaker, 60);     // 第三优先级 - 熔断保护
        middlewareChain.addMiddleware(retryMiddleware, 40);    // 最低优先级 - 重试机制
    }
    
    /**
     * 示例1：基本中间件使用
     */
    public void basicMiddlewareExample() {
        System.out.println("=== 基本中间件使用示例 ===");
        
        // 创建AI请求
        AIRequest request = createAIRequest("请解释一下机器学习的基本概念", "gpt-3.5-turbo");
        
        // 通过中间件链处理请求
        CompletableFuture<AIResponse> future = middlewareChain.process(request, this::simulateAIResponse);
        
        // 处理响应
        future.thenAccept(response -> {
            System.out.println("请求成功！");
            System.out.println("响应内容: " + response.getContent());
            System.out.println("响应时间: " + response.getResponseTime() + "ms");
            System.out.println("使用的令牌数: " + response.getTokenUsage());
        }).exceptionally(throwable -> {
            System.err.println("请求失败: " + throwable.getMessage());
            return null;
        });
        
        // 等待完成
        future.join();
        System.out.println();
    }
    
    /**
     * 示例2：重试机制演示
     */
    public void retryMechanismExample() {
        System.out.println("=== 重试机制演示 ===");
        
        // 创建会失败的请求（模拟临时故障）
        AIRequest failingRequest = createAIRequest("这个请求会失败几次", "gpt-3.5-turbo");
        failingRequest.getMetadata().put("simulate_failure", "true");
        failingRequest.getMetadata().put("failure_count", "2"); // 前2次失败，第3次成功
        
        // 通过中间件链处理（会自动重试）
        CompletableFuture<AIResponse> future = middlewareChain.process(failingRequest, this::simulateAIResponseWithFailure);
        
        future.thenAccept(response -> {
            System.out.println("请求最终成功！");
            System.out.println("响应内容: " + response.getContent());
            System.out.println("重试次数: " + response.getMetadata().get("retry_count"));
        }).exceptionally(throwable -> {
            System.err.println("请求最终失败: " + throwable.getMessage());
            return null;
        });
        
        future.join();
        System.out.println();
    }
    
    /**
     * 示例3：熔断器演示
     */
    public void circuitBreakerExample() {
        System.out.println("=== 熔断器演示 ===");
        
        // 连续发送多个会失败的请求
        System.out.println("发送多个失败请求以触发熔断器...");
        
        for (int i = 0; i < 10; i++) {
            final int requestId = i;
            AIRequest request = createAIRequest("熔断测试请求 " + requestId, "gpt-3.5-turbo");
            request.getMetadata().put("simulate_failure", "true");
            request.getMetadata().put("always_fail", "true"); // 总是失败
            
            CompletableFuture<AIResponse> future = middlewareChain.process(request, this::simulateAIResponseWithFailure);
            
            int finalRequestId = requestId;
            future.thenAccept(response -> {
                System.out.println("请求 " + finalRequestId + " 成功");
            }).exceptionally(throwable -> {
                System.err.println("请求 " + finalRequestId + " 失败: " + throwable.getMessage());
                return null;
            });
            
            // 小延迟以观察熔断器状态变化
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        
        System.out.println();
    }
    
    /**
     * 示例4：限流演示
     */
    public void rateLimitExample() {
        System.out.println("=== 限流演示 ===");
        
        // 快速发送多个请求以触发限流
        System.out.println("快速发送多个请求...");
        
        for (int i = 0; i < 15; i++) {
            final int requestId = i;
            AIRequest request = createAIRequest("限流测试请求 " + requestId, "gpt-3.5-turbo");
            
            CompletableFuture<AIResponse> future = middlewareChain.process(request, this::simulateAIResponse);
            
            int finalRequestId = requestId;
            future.thenAccept(response -> {
                System.out.println("请求 " + finalRequestId + " 成功");
            }).exceptionally(throwable -> {
                System.err.println("请求 " + finalRequestId + " 被拒绝: " + throwable.getMessage());
                return null;
            });
            
            // 非常小的延迟，快速发送请求
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        
        // 等待一段时间让限流器重置
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        System.out.println("等待限流重置后再次发送请求...");
        AIRequest resetRequest = createAIRequest("限流重置后的请求", "gpt-3.5-turbo");
        CompletableFuture<AIResponse> resetFuture = middlewareChain.process(resetRequest, this::simulateAIResponse);
        
        resetFuture.thenAccept(response -> {
            System.out.println("限流重置后的请求成功");
        }).exceptionally(throwable -> {
            System.err.println("限流重置后的请求失败: " + throwable.getMessage());
            return null;
        });
        
        resetFuture.join();
        System.out.println();
    }
    
    /**
     * 示例5：监控指标演示
     */
    public void monitoringExample() {
        System.out.println("=== 监控指标演示 ===");
        
        // 发送多个不同类型的请求以生成监控数据
        System.out.println("发送多个请求以生成监控数据...");
        
        for (int i = 0; i < 20; i++) {
            final int requestId = i;
            AIRequest request = createAIRequest("监控测试请求 " + requestId, "gpt-3.5-turbo");
            
            // 模拟一些请求失败
            if (requestId % 5 == 0) {
                request.getMetadata().put("simulate_failure", "true");
            }
            
            CompletableFuture<AIResponse> future = middlewareChain.process(request, this::simulateAIResponseWithFailure);
            
            future.thenAccept(response -> {
                System.out.println("请求 " + requestId + " 完成");
            }).exceptionally(throwable -> {
                System.err.println("请求 " + requestId + " 失败");
                return null;
            });
            
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        
        // 等待所有请求完成
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // 获取监控统计
        printMonitoringStats();
        System.out.println();
    }
    
    /**
     * 示例6：自定义中间件
     */
    public void customMiddlewareExample() {
        System.out.println("=== 自定义中间件示例 ===");
        
        // 创建自定义日志中间件
        AIMiddleware loggingMiddleware = (request, next) -> {
            long startTime = System.currentTimeMillis();
            System.out.println("[日志] 开始处理请求: " + request.getRequestId());
            
            return next.process(request).whenComplete((response, throwable) -> {
                long duration = System.currentTimeMillis() - startTime;
                if (throwable != null) {
                    System.err.println("[日志] 请求失败: " + request.getRequestId() + ", 耗时: " + duration + "ms");
                } else {
                    System.out.println("[日志] 请求成功: " + request.getRequestId() + ", 耗时: " + duration + "ms");
                }
            });
        };
        
        // 创建自定义认证中间件
        AIMiddleware authMiddleware = (request, next) -> {
            String authToken = request.getMetadata().get("auth_token");
            if (authToken == null || !authToken.startsWith("valid_token_")) {
                return CompletableFuture.failedFuture(
                    new RuntimeException("认证失败：无效的身份验证令牌")
                );
            }
            
            System.out.println("[认证] 请求已通过认证: " + request.getRequestId());
            return next.process(request);
        };
        
        // 创建新的中间件链
        AIMiddlewareChain customChain = new AIMiddlewareChain();
        customChain.addMiddleware(authMiddleware, 90);    // 高优先级 - 先认证
        customChain.addMiddleware(loggingMiddleware, 50);   // 中等优先级 - 记录日志
        
        // 测试带认证的请求
        AIRequest authRequest = createAIRequest("需要认证的请求", "gpt-3.5-turbo");
        authRequest.getMetadata().put("auth_token", "valid_token_12345");
        
        CompletableFuture<AIResponse> authFuture = customChain.process(authRequest, this::simulateAIResponse);
        authFuture.thenAccept(response -> {
            System.out.println("认证请求成功！");
        }).exceptionally(throwable -> {
            System.err.println("认证请求失败: " + throwable.getMessage());
            return null;
        });
        
        authFuture.join();
        
        // 测试无认证的请求
        AIRequest noAuthRequest = createAIRequest("无认证的请求", "gpt-3.5-turbo");
        CompletableFuture<AIResponse> noAuthFuture = customChain.process(noAuthRequest, this::simulateAIResponse);
        noAuthFuture.thenAccept(response -> {
            System.out.println("无认证请求成功！");
        }).exceptionally(throwable -> {
            System.err.println("无认证请求失败: " + throwable.getMessage());
            return null;
        });
        
        noAuthFuture.join();
        System.out.println();
    }
    
    /**
     * 示例7：中间件链动态管理
     */
    public void dynamicMiddlewareManagementExample() {
        System.out.println("=== 动态中间件管理示例 ===");
        
        // 创建可动态管理的中间件链
        AIMiddlewareChain dynamicChain = new AIMiddlewareChain();
        
        // 添加基础中间件
        RetryMiddleware baseRetry = new RetryMiddleware(2, Duration.ofSeconds(1), Duration.ofSeconds(5), 2.0, false);
        dynamicChain.addMiddleware(baseRetry, 50);
        
        System.out.println("初始中间件数量: " + dynamicChain.getMiddlewareCount());
        
        // 动态添加监控中间件
        MonitoringMiddleware dynamicMonitoring = new MonitoringMiddleware(true, true, true, Duration.ofMinutes(1));
        dynamicChain.addMiddleware(dynamicMonitoring, 100);
        System.out.println("添加监控中间件后数量: " + dynamicChain.getMiddlewareCount());
        
        // 测试请求
        AIRequest testRequest = createAIRequest("动态中间件测试", "gpt-3.5-turbo");
        CompletableFuture<AIResponse> future = dynamicChain.process(testRequest, this::simulateAIResponse);
        future.join();
        
        // 动态移除监控中间件
        dynamicChain.removeMiddleware(dynamicMonitoring);
        System.out.println("移除监控中间件后数量: " + dynamicChain.getMiddlewareCount());
        
        // 再次测试
        AIRequest testRequest2 = createAIRequest("移除监控后的测试", "gpt-3.5-turbo");
        CompletableFuture<AIResponse> future2 = dynamicChain.process(testRequest2, this::simulateAIResponse);
        future2.join();
        
        System.out.println();
    }
    
    // 辅助方法
    private AIRequest createAIRequest(String prompt, String modelType) {
        AIRequest request = new AIRequest();
        request.setRequestId("req_" + System.currentTimeMillis() + "_" + requestCounter.incrementAndGet());
        request.setPrompt(prompt);
        request.setModelType(modelType);
        request.setTemperature(0.7);
        request.setMaxTokens(1000);
        request.setMetadata(new HashMap<>());
        return request;
    }
    
    private CompletableFuture<AIResponse> simulateAIResponse(AIRequest request) {
        return CompletableFuture.supplyAsync(() -> {
            // 模拟处理延迟
            try {
                Thread.sleep(100 + (long) (Math.random() * 200));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            
            AIResponse response = new AIResponse();
            response.setRequestId(request.getRequestId());
            response.setContent("这是AI对 \"" + request.getPrompt() + "\" 的响应内容。");
            response.setModelType(request.getModelType());
            response.setTokenUsage(50 + (int) (Math.random() * 100));
            response.setResponseTime(100 + (long) (Math.random() * 200));
            response.setSuccess(true);
            response.setMetadata(new HashMap<>());
            
            return response;
        });
    }
    
    private CompletableFuture<AIResponse> simulateAIResponseWithFailure(AIRequest request) {
        return CompletableFuture.supplyAsync(() -> {
            Map<String, String> metadata = request.getMetadata();
            
            // 检查是否应该模拟失败
            if ("true".equals(metadata.get("simulate_failure"))) {
                String alwaysFail = metadata.get("always_fail");
                String failureCountStr = metadata.get("failure_count");
                String retryCountStr = metadata.get("retry_count");
                
                int retryCount = retryCountStr != null ? Integer.parseInt(retryCountStr) : 0;
                int failureCount = failureCountStr != null ? Integer.parseInt(failureCountStr) : 1;
                
                boolean shouldFail = "true".equals(alwaysFail) || retryCount < failureCount;
                
                if (shouldFail) {
                    // 更新重试计数
                    metadata.put("retry_count", String.valueOf(retryCount + 1));
                    throw new RuntimeException("模拟AI服务故障 - 重试次数: " + retryCount);
                }
            }
            
            // 成功响应
            return simulateAIResponse(request).join();
        });
    }
    
    private void printMonitoringStats() {
        // 这里应该从实际的监控中间件获取统计信息
        // 为了演示，我们模拟一些统计数据
        System.out.println("\n=== 监控统计信息 ===");
        System.out.println("总请求数: 20");
        System.out.println("成功请求数: 16");
        System.out.println("失败请求数: 4");
        System.out.println("成功率: 80.00%");
        System.out.println("平均响应时间: 150ms");
        System.out.println("最小响应时间: 100ms");
        System.out.println("最大响应时间: 290ms");
        System.out.println("95百分位响应时间: 250ms");
    }
    
    /**
     * 运行所有示例
     */
    public void runAllExamples() {
        System.out.println("=== 开始运行AI中间件示例 ===\n");
        
        basicMiddlewareExample();
        retryMechanismExample();
        circuitBreakerExample();
        rateLimitExample();
        monitoringExample();
        customMiddlewareExample();
        dynamicMiddlewareManagementExample();
        
        System.out.println("=== 所有中间件示例运行完成 ===");
    }
}