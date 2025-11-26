package com.fw.know.go.ai.middleware;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;

import com.fw.know.go.ai.prompt.PromptExecutionRequest;
import com.fw.know.go.ai.prompt.PromptExecutionResponse;

import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * AI中间件测试类
 */
@DisplayName("AI中间件测试")
class AiMiddlewareTest {
    
    private AiMiddleware mockNextMiddleware;
    private PromptExecutionRequest mockRequest;
    private PromptExecutionResponse mockResponse;
    
    @BeforeEach
    void setUp() {
        // 创建模拟对象
        mockNextMiddleware = Mockito.mock(AiMiddleware.class);
        mockRequest = Mockito.mock(PromptExecutionRequest.class);
        mockResponse = Mockito.mock(PromptExecutionResponse.class);
        
        // 设置模拟行为
        when(mockNextMiddleware.execute(mockRequest)).thenReturn(mockResponse);
    }
    
    @Nested
    @DisplayName("中间件接口测试")
    class MiddlewareInterfaceTest {
        
        @Test
        @DisplayName("应该正确定义中间件接口")
        void shouldDefineMiddlewareInterfaceCorrectly() {
            // Given
            AiMiddleware middleware = new AiMiddleware() {
                @Override
                public PromptExecutionResponse execute(PromptExecutionRequest request) {
                    return mockResponse;
                }
            };
            
            // When
            PromptExecutionResponse response = middleware.execute(mockRequest);
            
            // Then
            assertThat(response).isEqualTo(mockResponse);
        }
        
        @Test
        @DisplayName("应该正确处理请求")
        void shouldProcessRequestCorrectly() {
            // Given
            AiMiddleware middleware = new AiMiddleware() {
                @Override
                public PromptExecutionResponse execute(PromptExecutionRequest request) {
                    // 简单处理请求
                    return mockNextMiddleware.execute(request);
                }
            };
            
            // When
            PromptExecutionResponse response = middleware.execute(mockRequest);
            
            // Then
            assertThat(response).isEqualTo(mockResponse);
            verify(mockNextMiddleware, times(1)).execute(mockRequest);
        }
    }
    
    @Nested
    @DisplayName("重试中间件测试")
    class RetryMiddlewareTest {
        
        @Test
        @DisplayName("应该成功创建重试中间件")
        void shouldCreateRetryMiddleware() {
            // When
            RetryMiddleware middleware = new RetryMiddleware(3, 100);
            
            // Then
            assertThat(middleware).isNotNull();
            assertThat(middleware.getMaxRetries()).isEqualTo(3);
            assertThat(middleware.getRetryDelay()).isEqualTo(100);
        }
        
        @Test
        @DisplayName("应该在成功时直接返回响应")
        void shouldReturnResponseOnSuccess() {
            // Given
            RetryMiddleware middleware = new RetryMiddleware(3, 100);
            middleware.setNext(mockNextMiddleware);
            
            // When
            PromptExecutionResponse response = middleware.execute(mockRequest);
            
            // Then
            assertThat(response).isEqualTo(mockResponse);
            verify(mockNextMiddleware, times(1)).execute(mockRequest);
        }
        
        @Test
        @DisplayName("应该在失败时重试指定次数")
        void shouldRetryOnFailure() {
            // Given
            when(mockNextMiddleware.execute(mockRequest))
                .thenThrow(new RuntimeException("First attempt failed"))
                .thenThrow(new RuntimeException("Second attempt failed"))
                .thenReturn(mockResponse);
            
            RetryMiddleware middleware = new RetryMiddleware(3, 10);
            middleware.setNext(mockNextMiddleware);
            
            // When
            PromptExecutionResponse response = middleware.execute(mockRequest);
            
            // Then
            assertThat(response).isEqualTo(mockResponse);
            verify(mockNextMiddleware, times(3)).execute(mockRequest);
        }
        
        @Test
        @DisplayName("应该在超过最大重试次数时抛出异常")
        void shouldThrowExceptionWhenMaxRetriesExceeded() {
            // Given
            when(mockNextMiddleware.execute(mockRequest))
                .thenThrow(new RuntimeException("Attempt failed"));
            
            RetryMiddleware middleware = new RetryMiddleware(2, 10);
            middleware.setNext(mockNextMiddleware);
            
            // When & Then
            assertThatThrownBy(() -> middleware.execute(mockRequest))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Attempt failed");
            
            verify(mockNextMiddleware, times(3)).execute(mockRequest); // 1次初始 + 2次重试
        }
        
        @Test
        @DisplayName("应该正确设置重试参数")
        void shouldSetRetryParametersCorrectly() {
            // Given
            RetryMiddleware middleware = new RetryMiddleware(5, 200);
            
            // When
            middleware.setMaxRetries(10);
            middleware.setRetryDelay(500);
            
            // Then
            assertThat(middleware.getMaxRetries()).isEqualTo(10);
            assertThat(middleware.getRetryDelay()).isEqualTo(500);
        }
    }
    
    @Nested
    @DisplayName("熔断器中间件测试")
    class CircuitBreakerMiddlewareTest {
        
        @Test
        @DisplayName("应该成功创建熔断器中间件")
        void shouldCreateCircuitBreakerMiddleware() {
            // When
            CircuitBreakerMiddleware middleware = new CircuitBreakerMiddleware(10, 0.5, 5000);
            
            // Then
            assertThat(middleware).isNotNull();
            assertThat(middleware.getFailureThreshold()).isEqualTo(10);
            assertThat(middleware.getFailureRateThreshold()).isEqualTo(0.5);
            assertThat(middleware.getResetTimeout()).isEqualTo(5000);
        }
        
        @Test
        @DisplayName("应该在成功时直接返回响应")
        void shouldReturnResponseOnSuccess() {
            // Given
            CircuitBreakerMiddleware middleware = new CircuitBreakerMiddleware(3, 0.5, 1000);
            middleware.setNext(mockNextMiddleware);
            
            // When
            PromptExecutionResponse response = middleware.execute(mockRequest);
            
            // Then
            assertThat(response).isEqualTo(mockResponse);
            verify(mockNextMiddleware, times(1)).execute(mockRequest);
        }
        
        @Test
        @DisplayName("应该在失败时记录失败次数")
        void shouldRecordFailureCountOnFailure() {
            // Given
            when(mockNextMiddleware.execute(mockRequest))
                .thenThrow(new RuntimeException("Attempt failed"));
            
            CircuitBreakerMiddleware middleware = new CircuitBreakerMiddleware(3, 0.5, 1000);
            middleware.setNext(mockNextMiddleware);
            
            // When & Then
            assertThatThrownBy(() -> middleware.execute(mockRequest))
                .isInstanceOf(RuntimeException.class);
            
            assertThatThrownBy(() -> middleware.execute(mockRequest))
                .isInstanceOf(RuntimeException.class);
            
            verify(mockNextMiddleware, times(2)).execute(mockRequest);
        }
        
        @Test
        @DisplayName("应该正确设置熔断器参数")
        void shouldSetCircuitBreakerParametersCorrectly() {
            // Given
            CircuitBreakerMiddleware middleware = new CircuitBreakerMiddleware(5, 0.3, 2000);
            
            // When
            middleware.setFailureThreshold(8);
            middleware.setFailureRateThreshold(0.6);
            middleware.setResetTimeout(3000);
            
            // Then
            assertThat(middleware.getFailureThreshold()).isEqualTo(8);
            assertThat(middleware.getFailureRateThreshold()).isEqualTo(0.6);
            assertThat(middleware.getResetTimeout()).isEqualTo(3000);
        }
    }
    
    @Nested
    @DisplayName("限流中间件测试")
    class RateLimiterMiddlewareTest {
        
        @Test
        @DisplayName("应该成功创建限流中间件")
        void shouldCreateRateLimiterMiddleware() {
            // When
            RateLimiterMiddleware middleware = new RateLimiterMiddleware(100, 60);
            
            // Then
            assertThat(middleware).isNotNull();
            assertThat(middleware.getMaxRequests()).isEqualTo(100);
            assertThat(middleware.getWindowSeconds()).isEqualTo(60);
        }
        
        @Test
        @DisplayName("应该在请求数未达到限制时允许请求")
        void shouldAllowRequestWhenUnderLimit() {
            // Given
            RateLimiterMiddleware middleware = new RateLimiterMiddleware(5, 1);
            middleware.setNext(mockNextMiddleware);
            
            // When
            PromptExecutionResponse response = middleware.execute(mockRequest);
            
            // Then
            assertThat(response).isEqualTo(mockResponse);
            verify(mockNextMiddleware, times(1)).execute(mockRequest);
        }
        
        @Test
        @DisplayName("应该正确设置限流参数")
        void shouldSetRateLimiterParametersCorrectly() {
            // Given
            RateLimiterMiddleware middleware = new RateLimiterMiddleware(50, 30);
            
            // When
            middleware.setMaxRequests(200);
            middleware.setWindowSeconds(120);
            
            // Then
            assertThat(middleware.getMaxRequests()).isEqualTo(200);
            assertThat(middleware.getWindowSeconds()).isEqualTo(120);
        }
    }
    
    @Nested
    @DisplayName("监控中间件测试")
    class MonitoringMiddlewareTest {
        
        @Test
        @DisplayName("应该成功创建监控中间件")
        void shouldCreateMonitoringMiddleware() {
            // When
            MonitoringMiddleware middleware = new MonitoringMiddleware();
            
            // Then
            assertThat(middleware).isNotNull();
        }
        
        @Test
        @DisplayName("应该在执行请求时收集指标")
        void shouldCollectMetricsWhenExecutingRequest() {
            // Given
            MonitoringMiddleware middleware = new MonitoringMiddleware();
            middleware.setNext(mockNextMiddleware);
            
            // When
            PromptExecutionResponse response = middleware.execute(mockRequest);
            
            // Then
            assertThat(response).isEqualTo(mockResponse);
            verify(mockNextMiddleware, times(1)).execute(mockRequest);
        }
    }
    
    @Nested
    @DisplayName("中间件链测试")
    class MiddlewareChainTest {
        
        @Test
        @DisplayName("应该成功创建中间件链")
        void shouldCreateMiddlewareChain() {
            // When
            MiddlewareChain chain = new MiddlewareChain();
            
            // Then
            assertThat(chain).isNotNull();
            assertThat(chain.isEmpty()).isTrue();
        }
        
        @Test
        @DisplayName("应该成功添加中间件到链")
        void shouldAddMiddlewareToChain() {
            // Given
            MiddlewareChain chain = new MiddlewareChain();
            AiMiddleware middleware1 = new RetryMiddleware(3, 100);
            AiMiddleware middleware2 = new CircuitBreakerMiddleware(10, 0.5, 5000);
            
            // When
            chain.add(middleware1);
            chain.add(middleware2);
            
            // Then
            assertThat(chain.isEmpty()).isFalse();
        }
        
        @Test
        @DisplayName("应该按顺序执行中间件链")
        void shouldExecuteMiddlewareInOrder() {
            // Given
            MiddlewareChain chain = new MiddlewareChain();
            
            // 创建简单的中间件来跟踪执行顺序
            StringBuilder executionOrder = new StringBuilder();
            
            AiMiddleware middleware1 = new AiMiddleware() {
                @Override
                public PromptExecutionResponse execute(PromptExecutionRequest request) {
                    executionOrder.append("1");
                    return mockNextMiddleware.execute(request);
                }
            };
            
            AiMiddleware middleware2 = new AiMiddleware() {
                @Override
                public PromptExecutionResponse execute(PromptExecutionRequest request) {
                    executionOrder.append("2");
                    return mockNextMiddleware.execute(request);
                }
            };
            
            AiMiddleware middleware3 = new AiMiddleware() {
                @Override
                public PromptExecutionResponse execute(PromptExecutionRequest request) {
                    executionOrder.append("3");
                    return mockResponse;
                }
            };
            
            // When
            chain.add(middleware1);
            chain.add(middleware2);
            chain.add(middleware3);
            
            PromptExecutionResponse response = chain.execute(mockRequest);
            
            // Then
            assertThat(response).isEqualTo(mockResponse);
            assertThat(executionOrder.toString()).isEqualTo("123");
        }
        
        @Test
        @DisplayName("应该成功执行空的中间件链")
        void shouldExecuteEmptyMiddlewareChain() {
            // Given
            MiddlewareChain chain = new MiddlewareChain();
            
            // When
            PromptExecutionResponse response = chain.execute(mockRequest);
            
            // Then
            assertThat(response).isNull();
        }
        
        @Test
        @DisplayName("应该成功清除中间件链")
        void shouldClearMiddlewareChain() {
            // Given
            MiddlewareChain chain = new MiddlewareChain();
            AiMiddleware middleware = new RetryMiddleware(3, 100);
            chain.add(middleware);
            
            // When
            chain.clear();
            
            // Then
            assertThat(chain.isEmpty()).isTrue();
        }
    }
    
    @Nested
    @DisplayName("中间件动态管理测试")
    class MiddlewareDynamicManagementTest {
        
        @Test
        @DisplayName("应该能够动态添加中间件")
        void shouldAddMiddlewareDynamically() {
            // Given
            MiddlewareChain chain = new MiddlewareChain();
            AiMiddleware middleware1 = new RetryMiddleware(3, 100);
            AiMiddleware middleware2 = new CircuitBreakerMiddleware(10, 0.5, 5000);
            
            // When
            chain.add(middleware1);
            chain.add(middleware2);
            
            // Then
            assertThat(chain.isEmpty()).isFalse();
        }
        
        @Test
        @DisplayName("应该能够动态移除中间件")
        void shouldRemoveMiddlewareDynamically() {
            // Given
            MiddlewareChain chain = new MiddlewareChain();
            RetryMiddleware middleware = new RetryMiddleware(3, 100);
            chain.add(middleware);
            
            // When
            chain.remove(middleware);
            
            // Then
            assertThat(chain.isEmpty()).isTrue();
        }
        
        @Test
        @DisplayName("应该能够动态替换中间件")
        void shouldReplaceMiddlewareDynamically() {
            // Given
            MiddlewareChain chain = new MiddlewareChain();
            AiMiddleware oldMiddleware = new RetryMiddleware(3, 100);
            AiMiddleware newMiddleware = new RetryMiddleware(5, 200);
            chain.add(oldMiddleware);
            
            // When
            chain.remove(oldMiddleware);
            chain.add(newMiddleware);
            
            // Then
            assertThat(chain.isEmpty()).isFalse();
        }
    }
    
    @Nested
    @DisplayName("异步执行测试")
    class AsyncExecutionTest {
        
        @Test
        @DisplayName("应该支持异步执行中间件链")
        void shouldSupportAsyncExecution() {
            // Given
            MiddlewareChain chain = new MiddlewareChain();
            AiMiddleware middleware = new MonitoringMiddleware();
            middleware.setNext(mockNextMiddleware);
            chain.add(middleware);
            
            // When
            CompletableFuture<PromptExecutionResponse> future = CompletableFuture.supplyAsync(
                () -> chain.execute(mockRequest)
            );
            
            // Then
            assertThatCode(() -> future.get())
                .doesNotThrowAnyException();
        }
        
        @Test
        @DisplayName("应该正确处理异步执行中的异常")
        void shouldHandleExceptionInAsyncExecution() {
            // Given
            MiddlewareChain chain = new MiddlewareChain();
            AiMiddleware middleware = new AiMiddleware() {
                @Override
                public PromptExecutionResponse execute(PromptExecutionRequest request) {
                    throw new RuntimeException("Async execution failed");
                }
            };
            chain.add(middleware);
            
            // When
            CompletableFuture<PromptExecutionResponse> future = CompletableFuture.supplyAsync(
                () -> chain.execute(mockRequest)
            );
            
            // Then
            assertThatCode(() -> future.get())
                .isInstanceOf(Exception.class);
        }
    }
    
    @Nested
    @DisplayName("参数验证测试")
    class ParameterValidationTest {
        
        @Test
        @DisplayName("应该正确验证重试中间件参数")
        void shouldValidateRetryMiddlewareParameters() {
            // When & Then
            assertThatThrownBy(() -> new RetryMiddleware(-1, 100))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("重试次数不能为负数");
            
            assertThatThrownBy(() -> new RetryMiddleware(3, -1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("重试延迟不能为负数");
        }
        
        @Test
        @DisplayName("应该正确验证限流中间件参数")
        void shouldValidateRateLimiterMiddlewareParameters() {
            // When & Then
            assertThatThrownBy(() -> new RateLimiterMiddleware(-1, 60))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("最大请求数不能为负数");
            
            assertThatThrownBy(() -> new RateLimiterMiddleware(100, 0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("时间窗口必须大于0");
        }
        
        @Test
        @DisplayName("应该正确验证熔断器中间件参数")
        void shouldValidateCircuitBreakerMiddlewareParameters() {
            // When & Then
            assertThatThrownBy(() -> new CircuitBreakerMiddleware(-1, 0.5, 5000))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("失败阈值不能为负数");
            
            assertThatThrownBy(() -> new CircuitBreakerMiddleware(10, 0, 5000))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("失败率阈值必须在0到1之间");
            
            assertThatThrownBy(() -> new CircuitBreakerMiddleware(10, 1.1, 5000))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("失败率阈值必须在0到1之间");
            
            assertThatThrownBy(() -> new CircuitBreakerMiddleware(10, 0.5, -1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("重置超时时间不能为负数");
        }
    }
    
    @Nested
    @DisplayName("错误处理测试")
    class ErrorHandlingTest {
        
        @Test
        @DisplayName("应该正确处理中间件链中的异常")
        void shouldHandleExceptionInMiddlewareChain() {
            // Given
            MiddlewareChain chain = new MiddlewareChain();
            AiMiddleware middleware = new AiMiddleware() {
                @Override
                public PromptExecutionResponse execute(PromptExecutionRequest request) {
                    throw new RuntimeException("Middleware exception");
                }
            };
            chain.add(middleware);
            
            // When & Then
            assertThatThrownBy(() -> chain.execute(mockRequest))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Middleware exception");
        }
        
        @Test
        @DisplayName("应该正确处理空请求")
        void shouldHandleNullRequest() {
            // Given
            MiddlewareChain chain = new MiddlewareChain();
            AiMiddleware middleware = new MonitoringMiddleware();
            chain.add(middleware);
            
            // When & Then
            assertThatThrownBy(() -> chain.execute(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("请求不能为空");
        }
        
        @Test
        @DisplayName("应该正确处理空下一个中间件")
        void shouldHandleNullNextMiddleware() {
            // Given
            AiMiddleware middleware = new RetryMiddleware(3, 100);
            // 不设置下一个中间件
            
            // When & Then
            assertThatThrownBy(() -> middleware.execute(mockRequest))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("下一个中间件未设置");
        }
    }
}