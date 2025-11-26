package com.fw.know.go.ai.prompt;

import com.fw.know.go.ai.multimodal.MultimodalContent;
import com.fw.know.go.ai.middleware.AIMiddleware;
import com.fw.know.go.ai.middleware.AIMiddlewareChain;
import com.fw.know.go.ai.middleware.AIMiddlewareContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * 提示词模板执行器测试类
 */
@DisplayName("提示词模板执行器测试")
@ExtendWith(MockitoExtension.class)
class PromptTemplateExecutorTest {
    
    private PromptTemplateExecutor executor;
    
    @Mock
    private AIMiddleware mockMiddleware1;
    
    @Mock
    private AIMiddleware mockMiddleware2;
    
    @Mock
    private AIMiddlewareChain mockMiddlewareChain;
    
    @BeforeEach
    void setUp() {
        executor = new PromptTemplateExecutor();
        // 设置默认的中间件链
        executor.setMiddlewareChain(mockMiddlewareChain);
    }
    
    @Nested
    @DisplayName("基本执行测试")
    class BasicExecutionTest {
        
        @Test
        @DisplayName("应该成功执行简单模板")
        void shouldExecuteSimpleTemplate() {
            // Given
            PromptTemplate template = PromptTemplate.builder()
                .id("simple-template")
                .name("Simple Template")
                .content("Hello {{name}}, welcome to {{place}}!")
                .build();
            
            PromptExecutionRequest request = PromptExecutionRequest.builder()
                .templateId("simple-template")
                .parameters(Map.of("name", "Alice", "place", "Wonderland"))
                .build();
            
            PromptExecutionResponse expectedResponse = PromptExecutionResponse.builder()
                .requestId(request.getRequestId())
                .templateId("simple-template")
                .content("Hello Alice, welcome to Wonderland!")
                .status(ExecutionStatus.SUCCESS)
                .executionTime(Duration.ofMillis(100))
                .build();
            
            when(mockMiddlewareChain.execute(any(PromptTemplate.class), any(PromptExecutionRequest.class)))
                .thenReturn(expectedResponse);
            
            // When
            PromptExecutionResponse response = executor.execute(template, request);
            
            // Then
            assertThat(response).isNotNull();
            assertThat(response.getContent()).isEqualTo("Hello Alice, welcome to Wonderland!");
            assertThat(response.getStatus()).isEqualTo(ExecutionStatus.SUCCESS);
            assertThat(response.getRequestId()).isEqualTo(request.getRequestId());
            assertThat(response.getTemplateId()).isEqualTo("simple-template");
            
            verify(mockMiddlewareChain).execute(template, request);
        }
        
        @Test
        @DisplayName("应该成功执行包含条件逻辑的模板")
        void shouldExecuteTemplateWithConditionalLogic() {
            // Given
            PromptTemplate template = PromptTemplate.builder()
                .id("conditional-template")
                .name("Conditional Template")
                .content("{{#if isAdmin}}Welcome Admin{{else}}Welcome User{{/if}}")
                .build();
            
            PromptExecutionRequest adminRequest = PromptExecutionRequest.builder()
                .templateId("conditional-template")
                .parameters(Map.of("isAdmin", true))
                .build();
            
            PromptExecutionRequest userRequest = PromptExecutionRequest.builder()
                .templateId("conditional-template")
                .parameters(Map.of("isAdmin", false))
                .build();
            
            PromptExecutionResponse adminResponse = PromptExecutionResponse.builder()
                .requestId(adminRequest.getRequestId())
                .templateId("conditional-template")
                .content("Welcome Admin")
                .status(ExecutionStatus.SUCCESS)
                .build();
            
            PromptExecutionResponse userResponse = PromptExecutionResponse.builder()
                .requestId(userRequest.getRequestId())
                .templateId("conditional-template")
                .content("Welcome User")
                .status(ExecutionStatus.SUCCESS)
                .build();
            
            when(mockMiddlewareChain.execute(any(PromptTemplate.class), any(PromptExecutionRequest.class)))
                .thenReturn(adminResponse)
                .thenReturn(userResponse);
            
            // When
            PromptExecutionResponse adminResult = executor.execute(template, adminRequest);
            PromptExecutionResponse userResult = executor.execute(template, userRequest);
            
            // Then
            assertThat(adminResult.getContent()).isEqualTo("Welcome Admin");
            assertThat(userResult.getContent()).isEqualTo("Welcome User");
        }
        
        @Test
        @DisplayName("应该成功执行包含循环的模板")
        void shouldExecuteTemplateWithLoop() {
            // Given
            PromptTemplate template = PromptTemplate.builder()
                .id("loop-template")
                .name("Loop Template")
                .content("{{#each items}}{{this}} {{/each}}")
                .build();
            
            PromptExecutionRequest request = PromptExecutionRequest.builder()
                .templateId("loop-template")
                .parameters(Map.of("items", Arrays.asList("apple", "banana", "orange")))
                .build();
            
            PromptExecutionResponse expectedResponse = PromptExecutionResponse.builder()
                .requestId(request.getRequestId())
                .templateId("loop-template")
                .content("apple banana orange ")
                .status(ExecutionStatus.SUCCESS)
                .build();
            
            when(mockMiddlewareChain.execute(any(PromptTemplate.class), any(PromptExecutionRequest.class)))
                .thenReturn(expectedResponse);
            
            // When
            PromptExecutionResponse response = executor.execute(template, request);
            
            // Then
            assertThat(response.getContent()).isEqualTo("apple banana orange ");
        }
        
        @Test
        @DisplayName("应该成功执行包含嵌套参数的模板")
        void shouldExecuteTemplateWithNestedParameters() {
            // Given
            PromptTemplate template = PromptTemplate.builder()
                .id("nested-template")
                .name("Nested Template")
                .content("User {{user.name}} ({{user.age}}) from {{user.address.city}}")
                .build();
            
            Map<String, Object> nestedParams = Map.of(
                "user", Map.of(
                    "name", "Alice",
                    "age", 25,
                    "address", Map.of("city", "New York")
                )
            );
            
            PromptExecutionRequest request = PromptExecutionRequest.builder()
                .templateId("nested-template")
                .parameters(nestedParams)
                .build();
            
            PromptExecutionResponse expectedResponse = PromptExecutionResponse.builder()
                .requestId(request.getRequestId())
                .templateId("nested-template")
                .content("User Alice (25) from New York")
                .status(ExecutionStatus.SUCCESS)
                .build();
            
            when(mockMiddlewareChain.execute(any(PromptTemplate.class), any(PromptExecutionRequest.class)))
                .thenReturn(expectedResponse);
            
            // When
            PromptExecutionResponse response = executor.execute(template, request);
            
            // Then
            assertThat(response.getContent()).isEqualTo("User Alice (25) from New York");
        }
        
        @Test
        @DisplayName("应该成功执行包含多模态内容的模板")
        void shouldExecuteTemplateWithMultimodalContent() {
            // Given
            MultimodalContent multimodalContent = MultimodalContent.builder()
                .addText("Analyze this image: {{description}}")
                .addImage("base64:image/png:{{imageData}}")
                .build();
            
            PromptTemplate template = PromptTemplate.builder()
                .id("multimodal-template")
                .name("Multimodal Template")
                .multimodalContent(multimodalContent)
                .build();
            
            PromptExecutionRequest request = PromptExecutionRequest.builder()
                .templateId("multimodal-template")
                .parameters(Map.of("description", "A beautiful sunset", "imageData", "base64encodedimagedata"))
                .build();
            
            PromptExecutionResponse expectedResponse = PromptExecutionResponse.builder()
                .requestId(request.getRequestId())
                .templateId("multimodal-template")
                .content("This image shows a beautiful sunset with vibrant colors.")
                .status(ExecutionStatus.SUCCESS)
                .build();
            
            when(mockMiddlewareChain.execute(any(PromptTemplate.class), any(PromptExecutionRequest.class)))
                .thenReturn(expectedResponse);
            
            // When
            PromptExecutionResponse response = executor.execute(template, request);
            
            // Then
            assertThat(response).isNotNull();
            assertThat(response.getContent()).isEqualTo("This image shows a beautiful sunset with vibrant colors.");
            assertThat(response.getStatus()).isEqualTo(ExecutionStatus.SUCCESS);
        }
    }
    
    @Nested
    @DisplayName("异步执行测试")
    class AsyncExecutionTest {
        
        @Test
        @DisplayName("应该成功异步执行模板")
        void shouldExecuteTemplateAsync() throws Exception {
            // Given
            PromptTemplate template = PromptTemplate.builder()
                .id("async-template")
                .name("Async Template")
                .content("Hello {{name}}!")
                .build();
            
            PromptExecutionRequest request = PromptExecutionRequest.builder()
                .templateId("async-template")
                .parameters(Map.of("name", "Alice"))
                .build();
            
            PromptExecutionResponse expectedResponse = PromptExecutionResponse.builder()
                .requestId(request.getRequestId())
                .templateId("async-template")
                .content("Hello Alice!")
                .status(ExecutionStatus.SUCCESS)
                .build();
            
            when(mockMiddlewareChain.executeAsync(any(PromptTemplate.class), any(PromptExecutionRequest.class)))
                .thenReturn(CompletableFuture.completedFuture(expectedResponse));
            
            // When
            CompletableFuture<PromptExecutionResponse> future = executor.executeAsync(template, request);
            PromptExecutionResponse response = future.get(5, TimeUnit.SECONDS);
            
            // Then
            assertThat(response).isNotNull();
            assertThat(response.getContent()).isEqualTo("Hello Alice!");
            assertThat(response.getStatus()).isEqualTo(ExecutionStatus.SUCCESS);
            
            verify(mockMiddlewareChain).executeAsync(template, request);
        }
        
        @Test
        @DisplayName("应该正确处理异步执行异常")
        void shouldHandleAsyncExecutionException() {
            // Given
            PromptTemplate template = PromptTemplate.builder()
                .id("async-error-template")
                .name("Async Error Template")
                .content("Content")
                .build();
            
            PromptExecutionRequest request = PromptExecutionRequest.builder()
                .templateId("async-error-template")
                .parameters(Map.of("name", "Alice"))
                .build();
            
            CompletableFuture<PromptExecutionResponse> errorFuture = new CompletableFuture<>();
            errorFuture.completeExceptionally(new RuntimeException("Async execution failed"));
            
            when(mockMiddlewareChain.executeAsync(any(PromptTemplate.class), any(PromptExecutionRequest.class)))
                .thenReturn(errorFuture);
            
            // When
            CompletableFuture<PromptExecutionResponse> future = executor.executeAsync(template, request);
            
            // Then
            assertThatThrownBy(() -> future.get(5, TimeUnit.SECONDS))
                .isInstanceOf(Exception.class)
                .hasMessageContaining("Async execution failed");
        }
        
        @Test
        @DisplayName("应该成功批量异步执行模板")
        void shouldExecuteTemplatesAsyncInBatch() throws Exception {
            // Given
            PromptTemplate template1 = PromptTemplate.builder().id("batch-async-1").name("Batch 1").content("Hello {{name}}!").build();
            PromptTemplate template2 = PromptTemplate.builder().id("batch-async-2").name("Batch 2").content("Goodbye {{name}}!").build();
            
            PromptExecutionRequest request1 = PromptExecutionRequest.builder().templateId("batch-async-1").parameters(Map.of("name", "Alice")).build();
            PromptExecutionRequest request2 = PromptExecutionRequest.builder().templateId("batch-async-2").parameters(Map.of("name", "Bob")).build();
            
            PromptExecutionResponse response1 = PromptExecutionResponse.builder()
                .requestId(request1.getRequestId())
                .templateId("batch-async-1")
                .content("Hello Alice!")
                .status(ExecutionStatus.SUCCESS)
                .build();
            
            PromptExecutionResponse response2 = PromptExecutionResponse.builder()
                .requestId(request2.getRequestId())
                .templateId("batch-async-2")
                .content("Goodbye Bob!")
                .status(ExecutionStatus.SUCCESS)
                .build();
            
            when(mockMiddlewareChain.executeAsync(any(PromptTemplate.class), any(PromptExecutionRequest.class)))
                .thenReturn(CompletableFuture.completedFuture(response1))
                .thenReturn(CompletableFuture.completedFuture(response2));
            
            // When
            List<CompletableFuture<PromptExecutionResponse>> futures = executor.executeAsync(Arrays.asList(
                new AbstractMap.SimpleEntry<>(template1, request1),
                new AbstractMap.SimpleEntry<>(template2, request2)
            ));
            
            // Then
            assertThat(futures).hasSize(2);
            
            PromptExecutionResponse result1 = futures.get(0).get(5, TimeUnit.SECONDS);
            PromptExecutionResponse result2 = futures.get(1).get(5, TimeUnit.SECONDS);
            
            assertThat(result1.getContent()).isEqualTo("Hello Alice!");
            assertThat(result2.getContent()).isEqualTo("Goodbye Bob!");
            
            verify(mockMiddlewareChain, times(2)).executeAsync(any(PromptTemplate.class), any(PromptExecutionRequest.class));
        }
    }
    
    @Nested
    @DisplayName("错误处理测试")
    class ErrorHandlingTest {
        
        @Test
        @DisplayName("应该正确处理模板渲染错误")
        void shouldHandleTemplateRenderingError() {
            // Given
            PromptTemplate template = PromptTemplate.builder()
                .id("render-error-template")
                .name("Render Error Template")
                .content("Hello {{name}}!")
                .build();
            
            PromptExecutionRequest request = PromptExecutionRequest.builder()
                .templateId("render-error-template")
                .parameters(Map.of()) // 缺少必需的参数
                .build();
            
            when(mockMiddlewareChain.execute(any(PromptTemplate.class), any(PromptExecutionRequest.class)))
                .thenThrow(new RuntimeException("Template rendering failed: missing parameter 'name'"));
            
            // When & Then
            assertThatThrownBy(() -> executor.execute(template, request))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Template rendering failed");
        }
        
        @Test
        @DisplayName("应该正确处理执行超时")
        void shouldHandleExecutionTimeout() {
            // Given
            PromptTemplate template = PromptTemplate.builder()
                .id("timeout-template")
                .name("Timeout Template")
                .content("Content")
                .build();
            
            PromptExecutionRequest request = PromptExecutionRequest.builder()
                .templateId("timeout-template")
                .parameters(Map.of("name", "Alice"))
                .timeout(Duration.ofSeconds(1))
                .build();
            
            when(mockMiddlewareChain.execute(any(PromptTemplate.class), any(PromptExecutionRequest.class)))
                .thenAnswer(invocation -> {
                    Thread.sleep(2000); // 模拟超时
                    return PromptExecutionResponse.builder()
                        .requestId(request.getRequestId())
                        .templateId("timeout-template")
                        .content("Content")
                        .status(ExecutionStatus.SUCCESS)
                        .build();
                });
            
            // When & Then
            assertThatThrownBy(() -> executor.execute(template, request))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Execution timeout");
        }
        
        @Test
        @DisplayName("应该正确处理空参数")
        void shouldHandleNullParameters() {
            // When & Then
            assertThatThrownBy(() -> executor.execute(null, mock(PromptExecutionRequest.class)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("模板不能为空");
            
            assertThatThrownBy(() -> executor.execute(mock(PromptTemplate.class), null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("执行请求不能为空");
            
            assertThatThrownBy(() -> executor.executeAsync(null, mock(PromptExecutionRequest.class)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("模板不能为空");
            
            assertThatThrownBy(() -> executor.executeAsync(mock(PromptTemplate.class), null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("执行请求不能为空");
        }
        
        @Test
        @DisplayName("应该正确处理AI服务错误")
        void shouldHandleAIServiceError() {
            // Given
            PromptTemplate template = PromptTemplate.builder()
                .id("ai-error-template")
                .name("AI Error Template")
                .content("Content")
                .build();
            
            PromptExecutionRequest request = PromptExecutionRequest.builder()
                .templateId("ai-error-template")
                .parameters(Map.of("name", "Alice"))
                .build();
            
            PromptExecutionResponse errorResponse = PromptExecutionResponse.builder()
                .requestId(request.getRequestId())
                .templateId("ai-error-template")
                .content("")
                .status(ExecutionStatus.ERROR)
                .errorMessage("AI service is unavailable")
                .build();
            
            when(mockMiddlewareChain.execute(any(PromptTemplate.class), any(PromptExecutionRequest.class)))
                .thenReturn(errorResponse);
            
            // When
            PromptExecutionResponse response = executor.execute(template, request);
            
            // Then
            assertThat(response).isNotNull();
            assertThat(response.getStatus()).isEqualTo(ExecutionStatus.ERROR);
            assertThat(response.getErrorMessage()).isEqualTo("AI service is unavailable");
        }
    }
    
    @Nested
    @DisplayName("配置测试")
    class ConfigurationTest {
        
        @Test
        @DisplayName("应该正确设置和获取默认超时时间")
        void shouldSetAndGetDefaultTimeout() {
            // Given
            Duration defaultTimeout = Duration.ofSeconds(30);
            
            // When
            executor.setDefaultTimeout(defaultTimeout);
            Duration retrievedTimeout = executor.getDefaultTimeout();
            
            // Then
            assertThat(retrievedTimeout).isEqualTo(defaultTimeout);
        }
        
        @Test
        @DisplayName("应该正确设置和获取最大重试次数")
        void shouldSetAndGetMaxRetries() {
            // Given
            int maxRetries = 3;
            
            // When
            executor.setMaxRetries(maxRetries);
            int retrievedMaxRetries = executor.getMaxRetries();
            
            // Then
            assertThat(retrievedMaxRetries).isEqualTo(maxRetries);
        }
        
        @Test
        @DisplayName("应该正确设置和获取重试延迟")
        void shouldSetAndGetRetryDelay() {
            // Given
            Duration retryDelay = Duration.ofSeconds(2);
            
            // When
            executor.setRetryDelay(retryDelay);
            Duration retrievedRetryDelay = executor.getRetryDelay();
            
            // Then
            assertThat(retrievedRetryDelay).isEqualTo(retryDelay);
        }
        
        @Test
        @DisplayName("应该正确设置和获取中间件链")
        void shouldSetAndGetMiddlewareChain() {
            // Given
            AIMiddlewareChain newMiddlewareChain = mock(AIMiddlewareChain.class);
            
            // When
            executor.setMiddlewareChain(newMiddlewareChain);
            AIMiddlewareChain retrievedMiddlewareChain = executor.getMiddlewareChain();
            
            // Then
            assertThat(retrievedMiddlewareChain).isEqualTo(newMiddlewareChain);
        }
    }
    
    @Nested
    @DisplayName("性能测试")
    class PerformanceTest {
        
        @Test
        @DisplayName("应该正确测量执行时间")
        void shouldMeasureExecutionTime() {
            // Given
            PromptTemplate template = PromptTemplate.builder()
                .id("performance-template")
                .name("Performance Template")
                .content("Hello {{name}}!")
                .build();
            
            PromptExecutionRequest request = PromptExecutionRequest.builder()
                .templateId("performance-template")
                .parameters(Map.of("name", "Alice"))
                .build();
            
            LocalDateTime startTime = LocalDateTime.now();
            
            PromptExecutionResponse expectedResponse = PromptExecutionResponse.builder()
                .requestId(request.getRequestId())
                .templateId("performance-template")
                .content("Hello Alice!")
                .status(ExecutionStatus.SUCCESS)
                .executionTime(Duration.ofMillis(150))
                .startTime(startTime)
                .endTime(startTime.plus(Duration.ofMillis(150)))
                .build();
            
            when(mockMiddlewareChain.execute(any(PromptTemplate.class), any(PromptExecutionRequest.class)))
                .thenReturn(expectedResponse);
            
            // When
            PromptExecutionResponse response = executor.execute(template, request);
            
            // Then
            assertThat(response.getExecutionTime()).isEqualTo(Duration.ofMillis(150));
            assertThat(response.getStartTime()).isEqualTo(startTime);
            assertThat(response.getEndTime()).isEqualTo(startTime.plus(Duration.ofMillis(150)));
        }
        
        @Test
        @DisplayName("应该正确处理并发执行")
        void shouldHandleConcurrentExecution() throws Exception {
            // Given
            PromptTemplate template = PromptTemplate.builder()
                .id("concurrent-template")
                .name("Concurrent Template")
                .content("Hello {{name}}!")
                .build();
            
            int concurrentRequests = 10;
            List<CompletableFuture<PromptExecutionResponse>> futures = new ArrayList<>();
            
            // When
            for (int i = 0; i < concurrentRequests; i++) {
                PromptExecutionRequest request = PromptExecutionRequest.builder()
                    .templateId("concurrent-template")
                    .parameters(Map.of("name", "User" + i))
                    .build();
                
                PromptExecutionResponse response = PromptExecutionResponse.builder()
                    .requestId(request.getRequestId())
                    .templateId("concurrent-template")
                    .content("Hello User" + i + "!")
                    .status(ExecutionStatus.SUCCESS)
                    .build();
                
                when(mockMiddlewareChain.executeAsync(any(PromptTemplate.class), any(PromptExecutionRequest.class)))
                    .thenReturn(CompletableFuture.completedFuture(response));
                
                futures.add(executor.executeAsync(template, request));
            }
            
            // Then
            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).get(10, TimeUnit.SECONDS);
            
            for (int i = 0; i < concurrentRequests; i++) {
                PromptExecutionResponse response = futures.get(i).get();
                assertThat(response.getContent()).isEqualTo("Hello User" + i + "!");
                assertThat(response.getStatus()).isEqualTo(ExecutionStatus.SUCCESS);
            }
            
            verify(mockMiddlewareChain, times(concurrentRequests)).executeAsync(any(PromptTemplate.class), any(PromptExecutionRequest.class));
        }
    }
    
    @Nested
    @DisplayName("缓存测试")
    class CachingTest {
        
        @Test
        @DisplayName("应该正确使用缓存键")
        void shouldUseCacheKey() {
            // Given
            PromptTemplate template = PromptTemplate.builder()
                .id("cache-template")
                .name("Cache Template")
                .content("Hello {{name}}!")
                .build();
            
            PromptExecutionRequest request = PromptExecutionRequest.builder()
                .templateId("cache-template")
                .parameters(Map.of("name", "Alice"))
                .useCache(true)
                .build();
            
            String expectedCacheKey = request.getCacheKey();
            
            PromptExecutionResponse expectedResponse = PromptExecutionResponse.builder()
                .requestId(request.getRequestId())
                .templateId("cache-template")
                .content("Hello Alice!")
                .status(ExecutionStatus.SUCCESS)
                .cacheKey(expectedCacheKey)
                .build();
            
            when(mockMiddlewareChain.execute(any(PromptTemplate.class), any(PromptExecutionRequest.class)))
                .thenReturn(expectedResponse);
            
            // When
            PromptExecutionResponse response = executor.execute(template, request);
            
            // Then
            assertThat(response.getCacheKey()).isEqualTo(expectedCacheKey);
            assertThat(request.getCacheKey()).isEqualTo(expectedCacheKey);
        }
        
        @Test
        @DisplayName("应该正确处理缓存命中和未命中")
        void shouldHandleCacheHitAndMiss() {
            // Given
            PromptTemplate template = PromptTemplate.builder()
                .id("cache-hit-template")
                .name("Cache Hit Template")
                .content("Hello {{name}}!")
                .build();
            
            PromptExecutionRequest request = PromptExecutionRequest.builder()
                .templateId("cache-hit-template")
                .parameters(Map.of("name", "Alice"))
                .useCache(true)
                .build();
            
            // 第一次执行 - 缓存未命中
            PromptExecutionResponse firstResponse = PromptExecutionResponse.builder()
                .requestId(request.getRequestId())
                .templateId("cache-hit-template")
                .content("Hello Alice!")
                .status(ExecutionStatus.SUCCESS)
                .cacheHit(false)
                .build();
            
            // 第二次执行 - 缓存命中
            PromptExecutionResponse secondResponse = PromptExecutionResponse.builder()
                .requestId(UUID.randomUUID().toString())
                .templateId("cache-hit-template")
                .content("Hello Alice!")
                .status(ExecutionStatus.SUCCESS)
                .cacheHit(true)
                .build();
            
            when(mockMiddlewareChain.execute(any(PromptTemplate.class), any(PromptExecutionRequest.class)))
                .thenReturn(firstResponse)
                .thenReturn(secondResponse);
            
            // When
            PromptExecutionResponse response1 = executor.execute(template, request);
            PromptExecutionResponse response2 = executor.execute(template, request);
            
            // Then
            assertThat(response1.isCacheHit()).isFalse();
            assertThat(response2.isCacheHit()).isTrue();
            assertThat(response1.getContent()).isEqualTo(response2.getContent());
        }
    }
    
    @Nested
    @DisplayName("监控和指标测试")
    class MonitoringAndMetricsTest {
        
        @Test
        @DisplayName("应该正确收集执行指标")
        void shouldCollectExecutionMetrics() {
            // Given
            PromptTemplate template = PromptTemplate.builder()
                .id("metrics-template")
                .name("Metrics Template")
                .content("Hello {{name}}!")
                .build();
            
            PromptExecutionRequest request = PromptExecutionRequest.builder()
                .templateId("metrics-template")
                .parameters(Map.of("name", "Alice"))
                .build();
            
            PromptExecutionResponse expectedResponse = PromptExecutionResponse.builder()
                .requestId(request.getRequestId())
                .templateId("metrics-template")
                .content("Hello Alice!")
                .status(ExecutionStatus.SUCCESS)
                .executionTime(Duration.ofMillis(100))
                .tokenCount(10)
                .inputTokenCount(5)
                .outputTokenCount(5)
                .cost(0.01)
                .build();
            
            when(mockMiddlewareChain.execute(any(PromptTemplate.class), any(PromptExecutionRequest.class)))
                .thenReturn(expectedResponse);
            
            // When
            PromptExecutionResponse response = executor.execute(template, request);
            
            // Then
            assertThat(response.getTokenCount()).isEqualTo(10);
            assertThat(response.getInputTokenCount()).isEqualTo(5);
            assertThat(response.getOutputTokenCount()).isEqualTo(5);
            assertThat(response.getCost()).isEqualTo(0.01);
        }
        
        @Test
        @DisplayName("应该正确处理错误指标")
        void shouldHandleErrorMetrics() {
            // Given
            PromptTemplate template = PromptTemplate.builder()
                .id("error-metrics-template")
                .name("Error Metrics Template")
                .content("Content")
                .build();
            
            PromptExecutionRequest request = PromptExecutionRequest.builder()
                .templateId("error-metrics-template")
                .parameters(Map.of("name", "Alice"))
                .build();
            
            PromptExecutionResponse errorResponse = PromptExecutionResponse.builder()
                .requestId(request.getRequestId())
                .templateId("error-metrics-template")
                .content("")
                .status(ExecutionStatus.ERROR)
                .errorMessage("Service unavailable")
                .errorCode("SERVICE_UNAVAILABLE")
                .build();
            
            when(mockMiddlewareChain.execute(any(PromptTemplate.class), any(PromptExecutionRequest.class)))
                .thenReturn(errorResponse);
            
            // When
            PromptExecutionResponse response = executor.execute(template, request);
            
            // Then
            assertThat(response.getStatus()).isEqualTo(ExecutionStatus.ERROR);
            assertThat(response.getErrorMessage()).isEqualTo("Service unavailable");
            assertThat(response.getErrorCode()).isEqualTo("SERVICE_UNAVAILABLE");
        }
    }
}