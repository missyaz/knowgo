package com.fw.know.go.ai.prompt;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

import java.util.*;

import static org.assertj.core.api.Assertions.*;

/**
 * 提示词执行请求测试类
 */
@DisplayName("提示词执行请求测试")
class PromptExecutionRequestTest {
    
    private PromptExecutionRequest.Builder builder;
    
    @BeforeEach
    void setUp() {
        builder = PromptExecutionRequest.builder()
            .templateId("test-template")
            .parameters(Map.of("name", "Alice", "age", "25"))
            .modelType("gpt-3.5-turbo");
    }
    
    @Nested
    @DisplayName("构建器测试")
    class BuilderTest {
        
        @Test
        @DisplayName("应该成功构建完整的执行请求")
        void shouldBuildCompleteExecutionRequest() {
            // Given
            String templateId = "complete-template";
            Map<String, Object> parameters = Map.of("topic", "AI", "style", "formal");
            String modelType = "gpt-4";
            String userId = "user123";
            String sessionId = "session456";
            double temperature = 0.8;
            int maxTokens = 500;
            double topP = 0.9;
            double frequencyPenalty = 0.1;
            double presencePenalty = 0.1;
            List<String> stopSequences = Arrays.asList("END", "STOP");
            Map<String, String> metadata = Map.of("source", "test", "version", "1.0");
            
            // When
            PromptExecutionRequest request = PromptExecutionRequest.builder()
                .templateId(templateId)
                .parameters(parameters)
                .modelType(modelType)
                .userId(userId)
                .sessionId(sessionId)
                .temperature(temperature)
                .maxTokens(maxTokens)
                .topP(topP)
                .frequencyPenalty(frequencyPenalty)
                .presencePenalty(presencePenalty)
                .stopSequences(stopSequences)
                .metadata(metadata)
                .build();
            
            // Then
            assertThat(request).isNotNull();
            assertThat(request.getTemplateId()).isEqualTo(templateId);
            assertThat(request.getParameters()).isEqualTo(parameters);
            assertThat(request.getModelType()).isEqualTo(modelType);
            assertThat(request.getUserId()).isEqualTo(userId);
            assertThat(request.getSessionId()).isEqualTo(sessionId);
            assertThat(request.getTemperature()).isEqualTo(temperature);
            assertThat(request.getMaxTokens()).isEqualTo(maxTokens);
            assertThat(request.getTopP()).isEqualTo(topP);
            assertThat(request.getFrequencyPenalty()).isEqualTo(frequencyPenalty);
            assertThat(request.getPresencePenalty()).isEqualTo(presencePenalty);
            assertThat(request.getStopSequences()).isEqualTo(stopSequences);
            assertThat(request.getMetadata()).isEqualTo(metadata);
            assertThat(request.getRequestId()).isNotNull();
            assertThat(request.getTimestamp()).isNotNull();
        }
        
        @Test
        @DisplayName("应该成功构建最小化的执行请求")
        void shouldBuildMinimalExecutionRequest() {
            // When
            PromptExecutionRequest request = PromptExecutionRequest.builder()
                .templateId("minimal")
                .build();
            
            // Then
            assertThat(request).isNotNull();
            assertThat(request.getTemplateId()).isEqualTo("minimal");
            assertThat(request.getParameters()).isEmpty();
            assertThat(request.getModelType()).isNull();
            assertThat(request.getUserId()).isNull();
            assertThat(request.getSessionId()).isNull();
            assertThat(request.getTemperature()).isEqualTo(0.7);
            assertThat(request.getMaxTokens()).isEqualTo(150);
            assertThat(request.getTopP()).isEqualTo(1.0);
            assertThat(request.getFrequencyPenalty()).isEqualTo(0.0);
            assertThat(request.getPresencePenalty()).isEqualTo(0.0);
            assertThat(request.getStopSequences()).isEmpty();
            assertThat(request.getMetadata()).isEmpty();
            assertThat(request.getRequestId()).isNotNull();
            assertThat(request.getTimestamp()).isNotNull();
        }
        
        @Test
        @DisplayName("构建时应该抛出异常当必填字段缺失")
        void shouldThrowExceptionWhenRequiredFieldsMissing() {
            // When & Then
            assertThatThrownBy(() -> PromptExecutionRequest.builder().build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("模板ID不能为空");
        }
        
        @Test
        @DisplayName("应该验证温度值范围")
        void shouldValidateTemperatureRange() {
            // When & Then
            assertThatThrownBy(() -> builder.temperature(-0.1).build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("温度值必须在0到2之间");
            
            assertThatThrownBy(() -> builder.temperature(2.1).build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("温度值必须在0到2之间");
        }
        
        @Test
        @DisplayName("应该验证top-p值范围")
        void shouldValidateTopPRange() {
            // When & Then
            assertThatThrownBy(() -> builder.topP(-0.1).build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("top-p值必须在0到1之间");
            
            assertThatThrownBy(() -> builder.topP(1.1).build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("top-p值必须在0到1之间");
        }
        
        @Test
        @DisplayName("应该验证惩罚值范围")
        void shouldValidatePenaltyRange() {
            // When & Then
            assertThatThrownBy(() -> builder.frequencyPenalty(-2.1).build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("频率惩罚值必须在-2到2之间");
            
            assertThatThrownBy(() -> builder.presencePenalty(2.1).build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("存在惩罚值必须在-2到2之间");
        }
        
        @Test
        @DisplayName("应该验证最大令牌数")
        void shouldValidateMaxTokens() {
            // When & Then
            assertThatThrownBy(() -> builder.maxTokens(0).build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("最大令牌数必须大于0");
            
            assertThatThrownBy(() -> builder.maxTokens(-1).build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("最大令牌数必须大于0");
        }
    }
    
    @Nested
    @DisplayName("请求方法测试")
    class RequestMethodTest {
        
        @Test
        @DisplayName("应该正确验证请求参数")
        void shouldValidateRequestParameters() {
            // Given
            PromptExecutionRequest request = builder.build();
            Map<String, Object> validParams = Map.of("name", "Alice", "age", "25");
            Map<String, Object> invalidParams = Map.of("name", "Alice"); // 缺少 age 参数
            
            // When & Then
            assertThatCode(() -> request.validateParameters(validParams))
                .doesNotThrowAnyException();
            
            assertThatCode(() -> request.validateParameters(invalidParams))
                .doesNotThrowAnyException(); // 请求不验证参数，这是模板的责任
        }
        
        @Test
        @DisplayName("应该正确获取参数值")
        void shouldGetParameterValue() {
            // Given
            PromptExecutionRequest request = builder.build();
            
            // When & Then
            assertThat(request.getParameter("name")).isEqualTo("Alice");
            assertThat(request.getParameter("age")).isEqualTo("25");
            assertThat(request.getParameter("nonexistent")).isNull();
        }
        
        @Test
        @DisplayName("应该正确获取参数值或默认值")
        void shouldGetParameterValueOrDefault() {
            // Given
            PromptExecutionRequest request = builder.build();
            
            // When & Then
            assertThat(request.getParameterOrDefault("name", "Default")).isEqualTo("Alice");
            assertThat(request.getParameterOrDefault("nonexistent", "Default")).isEqualTo("Default");
        }
        
        @Test
        @DisplayName("应该正确检查是否包含参数")
        void shouldCheckIfContainsParameter() {
            // Given
            PromptExecutionRequest request = builder.build();
            
            // When & Then
            assertThat(request.containsParameter("name")).isTrue();
            assertThat(request.containsParameter("age")).isTrue();
            assertThat(request.containsParameter("nonexistent")).isFalse();
        }
        
        @Test
        @DisplayName("应该正确获取所有参数名称")
        void shouldGetAllParameterNames() {
            // Given
            PromptExecutionRequest request = builder.build();
            
            // When
            Set<String> parameterNames = request.getParameterNames();
            
            // Then
            assertThat(parameterNames).containsExactlyInAnyOrder("name", "age");
        }
        
        @Test
        @DisplayName("应该正确获取参数数量")
        void shouldGetParameterCount() {
            // Given
            PromptExecutionRequest request = builder.build();
            
            // When & Then
            assertThat(request.getParameterCount()).isEqualTo(2);
        }
        
        @Test
        @DisplayName("应该正确添加参数")
        void shouldAddParameter() {
            // Given
            PromptExecutionRequest request = builder.build();
            
            // When
            request.addParameter("city", "New York");
            
            // Then
            assertThat(request.getParameter("city")).isEqualTo("New York");
            assertThat(request.getParameterCount()).isEqualTo(3);
        }
        
        @Test
        @DisplayName("应该正确移除参数")
        void shouldRemoveParameter() {
            // Given
            PromptExecutionRequest request = builder.build();
            
            // When
            request.removeParameter("age");
            
            // Then
            assertThat(request.containsParameter("age")).isFalse();
            assertThat(request.getParameterCount()).isEqualTo(1);
        }
        
        @Test
        @DisplayName("应该正确更新参数")
        void shouldUpdateParameter() {
            // Given
            PromptExecutionRequest request = builder.build();
            
            // When
            request.updateParameter("name", "Bob");
            
            // Then
            assertThat(request.getParameter("name")).isEqualTo("Bob");
            assertThat(request.getParameterCount()).isEqualTo(2);
        }
        
        @Test
        @DisplayName("应该正确清空所有参数")
        void shouldClearAllParameters() {
            // Given
            PromptExecutionRequest request = builder.build();
            
            // When
            request.clearParameters();
            
            // Then
            assertThat(request.getParameters()).isEmpty();
            assertThat(request.getParameterCount()).isEqualTo(0);
        }
        
        @Test
        @DisplayName("应该正确获取元数据值")
        void shouldGetMetadataValue() {
            // Given
            PromptExecutionRequest request = builder
                .metadata(Map.of("source", "test", "version", "1.0"))
                .build();
            
            // When & Then
            assertThat(request.getMetadataValue("source")).isEqualTo("test");
            assertThat(request.getMetadataValue("version")).isEqualTo("1.0");
            assertThat(request.getMetadataValue("nonexistent")).isNull();
        }
        
        @Test
        @DisplayName("应该正确添加元数据")
        void shouldAddMetadata() {
            // Given
            PromptExecutionRequest request = builder.build();
            
            // When
            request.addMetadata("key", "value");
            
            // Then
            assertThat(request.getMetadataValue("key")).isEqualTo("value");
        }
        
        @Test
        @DisplayName("应该正确移除元数据")
        void shouldRemoveMetadata() {
            // Given
            PromptExecutionRequest request = builder
                .metadata(Map.of("key", "value"))
                .build();
            
            // When
            request.removeMetadata("key");
            
            // Then
            assertThat(request.getMetadataValue("key")).isNull();
        }
        
        @Test
        @DisplayName("应该正确检查是否包含元数据")
        void shouldCheckIfContainsMetadata() {
            // Given
            PromptExecutionRequest request = builder
                .metadata(Map.of("key", "value"))
                .build();
            
            // When & Then
            assertThat(request.containsMetadata("key")).isTrue();
            assertThat(request.containsMetadata("nonexistent")).isFalse();
        }
        
        @Test
        @DisplayName("应该正确获取所有元数据键")
        void shouldGetAllMetadataKeys() {
            // Given
            PromptExecutionRequest request = builder
                .metadata(Map.of("key1", "value1", "key2", "value2"))
                .build();
            
            // When
            Set<String> keys = request.getMetadataKeys();
            
            // Then
            assertThat(keys).containsExactlyInAnyOrder("key1", "key2");
        }
        
        @Test
        @DisplayName("应该正确清空所有元数据")
        void shouldClearAllMetadata() {
            // Given
            PromptExecutionRequest request = builder
                .metadata(Map.of("key", "value"))
                .build();
            
            // When
            request.clearMetadata();
            
            // Then
            assertThat(request.getMetadata()).isEmpty();
        }
        
        @Test
        @DisplayName("应该正确创建缓存键")
        void shouldCreateCacheKey() {
            // Given
            PromptExecutionRequest request = builder.build();
            
            // When
            String cacheKey = request.createCacheKey();
            
            // Then
            assertThat(cacheKey).isNotNull();
            assertThat(cacheKey).contains("test-template");
            assertThat(cacheKey).contains("gpt-3.5-turbo");
        }
        
        @Test
        @DisplayName("应该正确转换为字符串")
        void shouldConvertToString() {
            // Given
            PromptExecutionRequest request = builder.build();
            
            // When
            String str = request.toString();
            
            // Then
            assertThat(str).isNotNull();
            assertThat(str).contains("test-template");
            assertThat(str).contains("gpt-3.5-turbo");
        }
        
        @Test
        @DisplayName("应该正确获取请求摘要")
        void shouldGetRequestSummary() {
            // Given
            PromptExecutionRequest request = builder.build();
            
            // When
            String summary = request.getSummary();
            
            // Then
            assertThat(summary).isNotNull();
            assertThat(summary).contains("test-template");
            assertThat(summary).contains("gpt-3.5-turbo");
            assertThat(summary).contains("2"); // 参数数量
        }
        
        @Test
        @DisplayName("应该正确验证请求")
        void shouldValidateRequest() {
            // Given
            PromptExecutionRequest request = builder.build();
            
            // When & Then
            assertThatCode(() -> request.validate())
                .doesNotThrowAnyException();
            
            // 测试无效请求
            PromptExecutionRequest invalidRequest = PromptExecutionRequest.builder()
                .templateId("")
                .build();
            
            assertThatThrownBy(() -> invalidRequest.validate())
                .isInstanceOf(IllegalStateException.class);
        }
        
        @Test
        @DisplayName("应该正确检查是否为系统请求")
        void shouldCheckIfSystemRequest() {
            // Given
            PromptExecutionRequest systemRequest = builder
                .metadata(Map.of("type", "system"))
                .build();
            
            PromptExecutionRequest userRequest = builder
                .metadata(Map.of("type", "user"))
                .build();
            
            // When & Then
            assertThat(systemRequest.isSystemRequest()).isTrue();
            assertThat(userRequest.isSystemRequest()).isFalse();
        }
        
        @Test
        @DisplayName("应该正确创建请求副本")
        void shouldCreateRequestCopy() {
            // Given
            PromptExecutionRequest original = builder.build();
            
            // When
            PromptExecutionRequest copy = original.copy();
            
            // Then
            assertThat(copy).isNotNull();
            assertThat(copy.getTemplateId()).isEqualTo(original.getTemplateId());
            assertThat(copy.getParameters()).isEqualTo(original.getParameters());
            assertThat(copy).isNotSameAs(original);
            assertThat(copy.getRequestId()).isNotEqualTo(original.getRequestId());
        }
    }
    
    @Nested
    @DisplayName("请求比较测试")
    class RequestComparisonTest {
        
        @Test
        @DisplayName("应该正确比较相等的请求")
        void shouldCompareEqualRequests() {
            // Given
            PromptExecutionRequest request1 = builder.build();
            PromptExecutionRequest request2 = builder.build();
            
            // When & Then
            assertThat(request1).isNotEqualTo(request2); // 不同的requestId
            
            // 比较除requestId外的其他属性
            assertThat(request1.getTemplateId()).isEqualTo(request2.getTemplateId());
            assertThat(request1.getParameters()).isEqualTo(request2.getParameters());
            assertThat(request1.getModelType()).isEqualTo(request2.getModelType());
        }
        
        @Test
        @DisplayName("应该正确比较不同的请求")
        void shouldCompareDifferentRequests() {
            // Given
            PromptExecutionRequest request1 = builder.build();
            PromptExecutionRequest request2 = builder
                .templateId("different-template")
                .build();
            
            // When & Then
            assertThat(request1.getTemplateId()).isNotEqualTo(request2.getTemplateId());
        }
    }
}