package com.fw.know.go.ai.prompt;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.*;

/**
 * 提示词执行响应测试类
 */
@DisplayName("提示词执行响应测试")
class PromptExecutionResponseTest {
    
    private PromptExecutionResponse.Builder builder;
    
    @BeforeEach
    void setUp() {
        builder = PromptExecutionResponse.builder()
            .requestId("test-request-123")
            .templateId("test-template")
            .content("Generated response content")
            .modelType("gpt-3.5-turbo")
            .status(ExecutionStatus.SUCCESS);
    }
    
    @Nested
    @DisplayName("构建器测试")
    class BuilderTest {
        
        @Test
        @DisplayName("应该成功构建完整的执行响应")
        void shouldBuildCompleteExecutionResponse() {
            // Given
            String requestId = "complete-request-123";
            String templateId = "complete-template";
            String content = "Complete generated response content";
            String modelType = "gpt-4";
            ExecutionStatus status = ExecutionStatus.SUCCESS;
            String errorMessage = null;
            String errorCode = null;
            long executionTime = 1250L;
            int inputTokens = 50;
            int outputTokens = 100;
            int totalTokens = 150;
            double temperature = 0.8;
            int maxTokens = 500;
            double topP = 0.9;
            double frequencyPenalty = 0.1;
            double presencePenalty = 0.1;
            List<String> stopSequences = Arrays.asList("END", "STOP");
            Map<String, String> metadata = Map.of("source", "test", "version", "1.0");
            List<String> alternatives = Arrays.asList("Alternative 1", "Alternative 2");
            double confidence = 0.95;
            LocalDateTime timestamp = LocalDateTime.now();
            
            // When
            PromptExecutionResponse response = PromptExecutionResponse.builder()
                .requestId(requestId)
                .templateId(templateId)
                .content(content)
                .modelType(modelType)
                .status(status)
                .errorMessage(errorMessage)
                .errorCode(errorCode)
                .executionTime(executionTime)
                .inputTokens(inputTokens)
                .outputTokens(outputTokens)
                .totalTokens(totalTokens)
                .temperature(temperature)
                .maxTokens(maxTokens)
                .topP(topP)
                .frequencyPenalty(frequencyPenalty)
                .presencePenalty(presencePenalty)
                .stopSequences(stopSequences)
                .metadata(metadata)
                .alternatives(alternatives)
                .confidence(confidence)
                .timestamp(timestamp)
                .build();
            
            // Then
            assertThat(response).isNotNull();
            assertThat(response.getRequestId()).isEqualTo(requestId);
            assertThat(response.getTemplateId()).isEqualTo(templateId);
            assertThat(response.getContent()).isEqualTo(content);
            assertThat(response.getModelType()).isEqualTo(modelType);
            assertThat(response.getStatus()).isEqualTo(status);
            assertThat(response.getErrorMessage()).isEqualTo(errorMessage);
            assertThat(response.getErrorCode()).isEqualTo(errorCode);
            assertThat(response.getExecutionTime()).isEqualTo(executionTime);
            assertThat(response.getInputTokens()).isEqualTo(inputTokens);
            assertThat(response.getOutputTokens()).isEqualTo(outputTokens);
            assertThat(response.getTotalTokens()).isEqualTo(totalTokens);
            assertThat(response.getTemperature()).isEqualTo(temperature);
            assertThat(response.getMaxTokens()).isEqualTo(maxTokens);
            assertThat(response.getTopP()).isEqualTo(topP);
            assertThat(response.getFrequencyPenalty()).isEqualTo(frequencyPenalty);
            assertThat(response.getPresencePenalty()).isEqualTo(presencePenalty);
            assertThat(response.getStopSequences()).isEqualTo(stopSequences);
            assertThat(response.getMetadata()).isEqualTo(metadata);
            assertThat(response.getAlternatives()).isEqualTo(alternatives);
            assertThat(response.getConfidence()).isEqualTo(confidence);
            assertThat(response.getTimestamp()).isEqualTo(timestamp);
            assertThat(response.getResponseId()).isNotNull();
        }
        
        @Test
        @DisplayName("应该成功构建错误响应")
        void shouldBuildErrorResponse() {
            // Given
            String errorMessage = "API调用失败";
            String errorCode = "API_ERROR";
            
            // When
            PromptExecutionResponse response = PromptExecutionResponse.builder()
                .requestId("error-request")
                .templateId("error-template")
                .status(ExecutionStatus.FAILED)
                .errorMessage(errorMessage)
                .errorCode(errorCode)
                .build();
            
            // Then
            assertThat(response).isNotNull();
            assertThat(response.getStatus()).isEqualTo(ExecutionStatus.FAILED);
            assertThat(response.getErrorMessage()).isEqualTo(errorMessage);
            assertThat(response.getErrorCode()).isEqualTo(errorCode);
            assertThat(response.getContent()).isNull();
            assertThat(response.getModelType()).isNull();
        }
        
        @Test
        @DisplayName("应该成功构建最小化响应")
        void shouldBuildMinimalResponse() {
            // When
            PromptExecutionResponse response = PromptExecutionResponse.builder()
                .requestId("minimal-request")
                .templateId("minimal-template")
                .status(ExecutionStatus.SUCCESS)
                .build();
            
            // Then
            assertThat(response).isNotNull();
            assertThat(response.getRequestId()).isEqualTo("minimal-request");
            assertThat(response.getTemplateId()).isEqualTo("minimal-template");
            assertThat(response.getStatus()).isEqualTo(ExecutionStatus.SUCCESS);
            assertThat(response.getContent()).isNull();
            assertThat(response.getModelType()).isNull();
            assertThat(response.getExecutionTime()).isEqualTo(0L);
            assertThat(response.getInputTokens()).isEqualTo(0);
            assertThat(response.getOutputTokens()).isEqualTo(0);
            assertThat(response.getTotalTokens()).isEqualTo(0);
            assertThat(response.getConfidence()).isEqualTo(1.0);
            assertThat(response.getTimestamp()).isNotNull();
            assertThat(response.getResponseId()).isNotNull();
        }
        
        @Test
        @DisplayName("构建时应该抛出异常当必填字段缺失")
        void shouldThrowExceptionWhenRequiredFieldsMissing() {
            // When & Then
            assertThatThrownBy(() -> PromptExecutionResponse.builder().build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("请求ID不能为空");
            
            assertThatThrownBy(() -> PromptExecutionResponse.builder()
                    .requestId("test-request")
                    .build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("模板ID不能为空");
            
            assertThatThrownBy(() -> PromptExecutionResponse.builder()
                    .requestId("test-request")
                    .templateId("test-template")
                    .build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("执行状态不能为空");
        }
        
        @Test
        @DisplayName("应该验证令牌数")
        void shouldValidateTokenCounts() {
            // When & Then
            assertThatThrownBy(() -> builder.inputTokens(-1).build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("输入令牌数不能为负数");
            
            assertThatThrownBy(() -> builder.outputTokens(-1).build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("输出令牌数不能为负数");
            
            assertThatThrownBy(() -> builder.totalTokens(-1).build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("总令牌数不能为负数");
        }
        
        @Test
        @DisplayName("应该验证执行时间")
        void shouldValidateExecutionTime() {
            // When & Then
            assertThatThrownBy(() -> builder.executionTime(-1L).build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("执行时间不能为负数");
        }
        
        @Test
        @DisplayName("应该验证置信度范围")
        void shouldValidateConfidenceRange() {
            // When & Then
            assertThatThrownBy(() -> builder.confidence(-0.1).build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("置信度必须在0到1之间");
            
            assertThatThrownBy(() -> builder.confidence(1.1).build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("置信度必须在0到1之间");
        }
    }
    
    @Nested
    @DisplayName("响应方法测试")
    class ResponseMethodTest {
        
        @Test
        @DisplayName("应该正确检查是否成功")
        void shouldCheckIfSuccessful() {
            // Given
            PromptExecutionResponse successResponse = builder.status(ExecutionStatus.SUCCESS).build();
            PromptExecutionResponse failedResponse = builder.status(ExecutionStatus.FAILED).build();
            PromptExecutionResponse timeoutResponse = builder.status(ExecutionStatus.TIMEOUT).build();
            
            // When & Then
            assertThat(successResponse.isSuccessful()).isTrue();
            assertThat(failedResponse.isSuccessful()).isFalse();
            assertThat(timeoutResponse.isSuccessful()).isFalse();
        }
        
        @Test
        @DisplayName("应该正确检查是否失败")
        void shouldCheckIfFailed() {
            // Given
            PromptExecutionResponse successResponse = builder.status(ExecutionStatus.SUCCESS).build();
            PromptExecutionResponse failedResponse = builder.status(ExecutionStatus.FAILED).build();
            PromptExecutionResponse timeoutResponse = builder.status(ExecutionStatus.TIMEOUT).build();
            
            // When & Then
            assertThat(successResponse.isFailed()).isFalse();
            assertThat(failedResponse.isFailed()).isTrue();
            assertThat(timeoutResponse.isFailed()).isTrue();
        }
        
        @Test
        @DisplayName("应该正确检查是否超时")
        void shouldCheckIfTimeout() {
            // Given
            PromptExecutionResponse successResponse = builder.status(ExecutionStatus.SUCCESS).build();
            PromptExecutionResponse failedResponse = builder.status(ExecutionStatus.FAILED).build();
            PromptExecutionResponse timeoutResponse = builder.status(ExecutionStatus.TIMEOUT).build();
            
            // When & Then
            assertThat(successResponse.isTimeout()).isFalse();
            assertThat(failedResponse.isTimeout()).isFalse();
            assertThat(timeoutResponse.isTimeout()).isTrue();
        }
        
        @Test
        @DisplayName("应该正确获取元数据值")
        void shouldGetMetadataValue() {
            // Given
            PromptExecutionResponse response = builder
                .metadata(Map.of("source", "test", "version", "1.0"))
                .build();
            
            // When & Then
            assertThat(response.getMetadataValue("source")).isEqualTo("test");
            assertThat(response.getMetadataValue("version")).isEqualTo("1.0");
            assertThat(response.getMetadataValue("nonexistent")).isNull();
        }
        
        @Test
        @DisplayName("应该正确添加元数据")
        void shouldAddMetadata() {
            // Given
            PromptExecutionResponse response = builder.build();
            
            // When
            response.addMetadata("key", "value");
            
            // Then
            assertThat(response.getMetadataValue("key")).isEqualTo("value");
        }
        
        @Test
        @DisplayName("应该正确移除元数据")
        void shouldRemoveMetadata() {
            // Given
            PromptExecutionResponse response = builder
                .metadata(Map.of("key", "value"))
                .build();
            
            // When
            response.removeMetadata("key");
            
            // Then
            assertThat(response.getMetadataValue("key")).isNull();
        }
        
        @Test
        @DisplayName("应该正确检查是否包含元数据")
        void shouldCheckIfContainsMetadata() {
            // Given
            PromptExecutionResponse response = builder
                .metadata(Map.of("key", "value"))
                .build();
            
            // When & Then
            assertThat(response.containsMetadata("key")).isTrue();
            assertThat(response.containsMetadata("nonexistent")).isFalse();
        }
        
        @Test
        @DisplayName("应该正确获取所有元数据键")
        void shouldGetAllMetadataKeys() {
            // Given
            PromptExecutionResponse response = builder
                .metadata(Map.of("key1", "value1", "key2", "value2"))
                .build();
            
            // When
            Set<String> keys = response.getMetadataKeys();
            
            // Then
            assertThat(keys).containsExactlyInAnyOrder("key1", "key2");
        }
        
        @Test
        @DisplayName("应该正确清空所有元数据")
        void shouldClearAllMetadata() {
            // Given
            PromptExecutionResponse response = builder
                .metadata(Map.of("key", "value"))
                .build();
            
            // When
            response.clearMetadata();
            
            // Then
            assertThat(response.getMetadata()).isEmpty();
        }
        
        @Test
        @DisplayName("应该正确获取替代响应")
        void shouldGetAlternativeResponse() {
            // Given
            List<String> alternatives = Arrays.asList("Alternative 1", "Alternative 2", "Alternative 3");
            PromptExecutionResponse response = builder
                .alternatives(alternatives)
                .build();
            
            // When & Then
            assertThat(response.getAlternativeResponse(0)).isEqualTo("Alternative 1");
            assertThat(response.getAlternativeResponse(1)).isEqualTo("Alternative 2");
            assertThat(response.getAlternativeResponse(2)).isEqualTo("Alternative 3");
            assertThat(response.getAlternativeResponse(3)).isNull(); // 超出范围
            assertThat(response.getAlternativeResponse(-1)).isNull(); // 负数索引
        }
        
        @Test
        @DisplayName("应该正确获取替代响应数量")
        void shouldGetAlternativeResponseCount() {
            // Given
            List<String> alternatives = Arrays.asList("Alternative 1", "Alternative 2", "Alternative 3");
            PromptExecutionResponse response = builder
                .alternatives(alternatives)
                .build();
            
            // When & Then
            assertThat(response.getAlternativeResponseCount()).isEqualTo(3);
            
            // 测试没有替代响应的情况
            PromptExecutionResponse noAlternatives = builder.build();
            assertThat(noAlternatives.getAlternativeResponseCount()).isEqualTo(0);
        }
        
        @Test
        @DisplayName("应该正确检查是否有替代响应")
        void shouldCheckIfHasAlternatives() {
            // Given
            List<String> alternatives = Arrays.asList("Alternative 1", "Alternative 2");
            PromptExecutionResponse responseWithAlternatives = builder
                .alternatives(alternatives)
                .build();
            
            PromptExecutionResponse responseWithoutAlternatives = builder.build();
            
            // When & Then
            assertThat(responseWithAlternatives.hasAlternatives()).isTrue();
            assertThat(responseWithoutAlternatives.hasAlternatives()).isFalse();
        }
        
        @Test
        @DisplayName("应该正确计算成本")
        void shouldCalculateCost() {
            // Given
            PromptExecutionResponse response = builder
                .inputTokens(100)
                .outputTokens(200)
                .totalTokens(300)
                .build();
            
            // When
            double cost = response.calculateCost();
            
            // Then
            assertThat(cost).isGreaterThan(0.0);
            assertThat(cost).isEqualTo(0.0045); // 假设每1000个token成本为0.015
        }
        
        @Test
        @DisplayName("应该正确计算成本当令牌数为0")
        void shouldCalculateCostWhenTokensZero() {
            // Given
            PromptExecutionResponse response = builder.build();
            
            // When
            double cost = response.calculateCost();
            
            // Then
            assertThat(cost).isEqualTo(0.0);
        }
        
        @Test
        @DisplayName("应该正确获取响应摘要")
        void shouldGetResponseSummary() {
            // Given
            PromptExecutionResponse response = builder.build();
            
            // When
            String summary = response.getSummary();
            
            // Then
            assertThat(summary).isNotNull();
            assertThat(summary).contains("test-template");
            assertThat(summary).contains("SUCCESS");
            assertThat(summary).contains("gpt-3.5-turbo");
        }
        
        @Test
        @DisplayName("应该正确获取错误响应摘要")
        void shouldGetErrorResponseSummary() {
            // Given
            PromptExecutionResponse response = builder
                .status(ExecutionStatus.FAILED)
                .errorMessage("API调用失败")
                .errorCode("API_ERROR")
                .build();
            
            // When
            String summary = response.getSummary();
            
            // Then
            assertThat(summary).isNotNull();
            assertThat(summary).contains("test-template");
            assertThat(summary).contains("FAILED");
            assertThat(summary).contains("API调用失败");
        }
        
        @Test
        @DisplayName("应该正确转换为字符串")
        void shouldConvertToString() {
            // Given
            PromptExecutionResponse response = builder.build();
            
            // When
            String str = response.toString();
            
            // Then
            assertThat(str).isNotNull();
            assertThat(str).contains("test-template");
            assertThat(str).contains("SUCCESS");
        }
        
        @Test
        @DisplayName("应该正确创建响应副本")
        void shouldCreateResponseCopy() {
            // Given
            PromptExecutionResponse original = builder.build();
            
            // When
            PromptExecutionResponse copy = original.copy();
            
            // Then
            assertThat(copy).isNotNull();
            assertThat(copy.getRequestId()).isEqualTo(original.getRequestId());
            assertThat(copy.getTemplateId()).isEqualTo(original.getTemplateId());
            assertThat(copy.getContent()).isEqualTo(original.getContent());
            assertThat(copy).isNotSameAs(original);
            assertThat(copy.getResponseId()).isNotEqualTo(original.getResponseId());
        }
        
        @Test
        @DisplayName("应该正确验证响应")
        void shouldValidateResponse() {
            // Given
            PromptExecutionResponse response = builder.build();
            
            // When & Then
            assertThatCode(() -> response.validate())
                .doesNotThrowAnyException();
            
            // 测试无效响应
            PromptExecutionResponse invalidResponse = PromptExecutionResponse.builder()
                .requestId("")
                .templateId("test-template")
                .status(ExecutionStatus.SUCCESS)
                .build();
            
            assertThatThrownBy(() -> invalidResponse.validate())
                .isInstanceOf(IllegalStateException.class);
        }
        
        @Test
        @DisplayName("应该正确获取执行状态描述")
        void shouldGetExecutionStatusDescription() {
            // Given
            PromptExecutionResponse successResponse = builder.status(ExecutionStatus.SUCCESS).build();
            PromptExecutionResponse failedResponse = builder.status(ExecutionStatus.FAILED).build();
            PromptExecutionResponse timeoutResponse = builder.status(ExecutionStatus.TIMEOUT).build();
            
            // When & Then
            assertThat(successResponse.getStatusDescription()).isEqualTo("执行成功");
            assertThat(failedResponse.getStatusDescription()).isEqualTo("执行失败");
            assertThat(timeoutResponse.getStatusDescription()).isEqualTo("执行超时");
        }
        
        @Test
        @DisplayName("应该正确获取性能指标")
        void shouldGetPerformanceMetrics() {
            // Given
            PromptExecutionResponse response = builder
                .executionTime(1500L)
                .inputTokens(100)
                .outputTokens(200)
                .totalTokens(300)
                .build();
            
            // When
            Map<String, Object> metrics = response.getPerformanceMetrics();
            
            // Then
            assertThat(metrics).isNotNull();
            assertThat(metrics).containsEntry("executionTime", 1500L);
            assertThat(metrics).containsEntry("inputTokens", 100);
            assertThat(metrics).containsEntry("outputTokens", 200);
            assertThat(metrics).containsEntry("totalTokens", 300);
            assertThat(metrics).containsEntry("tokensPerSecond", 200.0);
            assertThat(metrics).containsEntry("cost", 0.0045);
        }
    }
    
    @Nested
    @DisplayName("响应比较测试")
    class ResponseComparisonTest {
        
        @Test
        @DisplayName("应该正确比较相等的响应")
        void shouldCompareEqualResponses() {
            // Given
            PromptExecutionResponse response1 = builder.build();
            PromptExecutionResponse response2 = builder.build();
            
            // When & Then
            assertThat(response1).isNotEqualTo(response2); // 不同的responseId
            
            // 比较除responseId外的其他属性
            assertThat(response1.getRequestId()).isEqualTo(response2.getRequestId());
            assertThat(response1.getTemplateId()).isEqualTo(response2.getTemplateId());
            assertThat(response1.getContent()).isEqualTo(response2.getContent());
            assertThat(response1.getStatus()).isEqualTo(response2.getStatus());
        }
        
        @Test
        @DisplayName("应该正确比较不同的响应")
        void shouldCompareDifferentResponses() {
            // Given
            PromptExecutionResponse response1 = builder.build();
            PromptExecutionResponse response2 = builder
                .status(ExecutionStatus.FAILED)
                .errorMessage("API调用失败")
                .build();
            
            // When & Then
            assertThat(response1.getStatus()).isNotEqualTo(response2.getStatus());
            assertThat(response1.isSuccessful()).isTrue();
            assertThat(response2.isSuccessful()).isFalse();
        }
    }
}