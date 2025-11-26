package com.fw.know.go.ai.prompt;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

import java.util.*;

import static org.assertj.core.api.Assertions.*;

/**
 * 提示词模板测试类
 */
@DisplayName("提示词模板测试")
class PromptTemplateTest {
    
    private PromptTemplate template;
    
    @BeforeEach
    void setUp() {
        template = PromptTemplate.builder()
            .id("test-template")
            .name("测试模板")
            .content("Hello ${name}, welcome to ${place}!")
            .category("测试")
            .type(PromptTemplate.TemplateType.USER)
            .description("测试用的提示词模板")
            .modelType("gpt-3.5-turbo")
            .parameters(Arrays.asList("name", "place"))
            .temperature(0.7)
            .maxTokens(100)
            .build();
    }
    
    @Nested
    @DisplayName("构建器测试")
    class BuilderTest {
        
        @Test
        @DisplayName("应该成功构建完整的提示词模板")
        void shouldBuildCompletePromptTemplate() {
            // Given
            String id = "complete-template";
            String name = "完整模板";
            String content = "请${action}这个${object}";
            String category = "功能测试";
            String description = "完整的测试模板";
            String modelType = "gpt-4";
            List<String> parameters = Arrays.asList("action", "object");
            
            // When
            PromptTemplate result = PromptTemplate.builder()
                .id(id)
                .name(name)
                .content(content)
                .category(category)
                .type(PromptTemplate.TemplateType.SYSTEM)
                .description(description)
                .modelType(modelType)
                .parameters(parameters)
                .temperature(0.5)
                .maxTokens(200)
                .topP(0.9)
                .frequencyPenalty(0.1)
                .presencePenalty(0.1)
                .disabled(false)
                .rating(4.5)
                .usageCount(10)
                .tags(Arrays.asList("测试", "功能"))
                .metadata(Map.of("author", "tester", "version", "1.0"))
                .build();
            
            // Then
            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(id);
            assertThat(result.getName()).isEqualTo(name);
            assertThat(result.getContent()).isEqualTo(content);
            assertThat(result.getCategory()).isEqualTo(category);
            assertThat(result.getType()).isEqualTo(PromptTemplate.TemplateType.SYSTEM);
            assertThat(result.getDescription()).isEqualTo(description);
            assertThat(result.getModelType()).isEqualTo(modelType);
            assertThat(result.getParameters()).isEqualTo(parameters);
            assertThat(result.getTemperature()).isEqualTo(0.5);
            assertThat(result.getMaxTokens()).isEqualTo(200);
            assertThat(result.getTopP()).isEqualTo(0.9);
            assertThat(result.getFrequencyPenalty()).isEqualTo(0.1);
            assertThat(result.getPresencePenalty()).isEqualTo(0.1);
            assertThat(result.isDisabled()).isFalse();
            assertThat(result.getRating()).isEqualTo(4.5);
            assertThat(result.getUsageCount()).isEqualTo(10);
            assertThat(result.getTags()).containsExactly("测试", "功能");
            assertThat(result.getMetadata()).containsEntry("author", "tester");
            assertThat(result.getMetadata()).containsEntry("version", "1.0");
            assertThat(result.getCreatedAt()).isNotNull();
            assertThat(result.getUpdatedAt()).isNotNull();
        }
        
        @Test
        @DisplayName("应该成功构建最小化的提示词模板")
        void shouldBuildMinimalPromptTemplate() {
            // When
            PromptTemplate result = PromptTemplate.builder()
                .id("minimal")
                .name("最小模板")
                .content("Hello World")
                .build();
            
            // Then
            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo("minimal");
            assertThat(result.getName()).isEqualTo("最小模板");
            assertThat(result.getContent()).isEqualTo("Hello World");
            assertThat(result.getCategory()).isNull();
            assertThat(result.getType()).isEqualTo(PromptTemplate.TemplateType.USER);
            assertThat(result.getDescription()).isNull();
            assertThat(result.getModelType()).isNull();
            assertThat(result.getParameters()).isEmpty();
            assertThat(result.getTemperature()).isEqualTo(0.7);
            assertThat(result.getMaxTokens()).isEqualTo(150);
            assertThat(result.getTopP()).isEqualTo(1.0);
            assertThat(result.getFrequencyPenalty()).isEqualTo(0.0);
            assertThat(result.getPresencePenalty()).isEqualTo(0.0);
            assertThat(result.isDisabled()).isFalse();
            assertThat(result.getRating()).isEqualTo(0.0);
            assertThat(result.getUsageCount()).isEqualTo(0);
            assertThat(result.getTags()).isEmpty();
            assertThat(result.getMetadata()).isEmpty();
        }
        
        @Test
        @DisplayName("构建时应该抛出异常当必填字段缺失")
        void shouldThrowExceptionWhenRequiredFieldsMissing() {
            // When & Then
            assertThatThrownBy(() -> PromptTemplate.builder().build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("ID不能为空");
            
            assertThatThrownBy(() -> PromptTemplate.builder().id("test").build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("名称不能为空");
            
            assertThatThrownBy(() -> PromptTemplate.builder().id("test").name("test").build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("内容不能为空");
        }
        
        @Test
        @DisplayName("应该验证参数名称格式")
        void shouldValidateParameterNames() {
            // When & Then
            assertThatThrownBy(() -> PromptTemplate.builder()
                .id("test")
                .name("test")
                .content("Hello ${invalid-name}")
                .build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("参数名称格式无效");
            
            assertThatThrownBy(() -> PromptTemplate.builder()
                .id("test")
                .name("test")
                .content("Hello ${123invalid}")
                .build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("参数名称格式无效");
        }
        
        @Test
        @DisplayName("应该验证温度值范围")
        void shouldValidateTemperatureRange() {
            // When & Then
            assertThatThrownBy(() -> PromptTemplate.builder()
                .id("test")
                .name("test")
                .content("Hello")
                .temperature(-0.1)
                .build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("温度值必须在0到2之间");
            
            assertThatThrownBy(() -> PromptTemplate.builder()
                .id("test")
                .name("test")
                .content("Hello")
                .temperature(2.1)
                .build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("温度值必须在0到2之间");
        }
        
        @Test
        @DisplayName("应该验证top-p值范围")
        void shouldValidateTopPRange() {
            // When & Then
            assertThatThrownBy(() -> PromptTemplate.builder()
                .id("test")
                .name("test")
                .content("Hello")
                .topP(-0.1)
                .build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("top-p值必须在0到1之间");
            
            assertThatThrownBy(() -> PromptTemplate.builder()
                .id("test")
                .name("test")
                .content("Hello")
                .topP(1.1)
                .build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("top-p值必须在0到1之间");
        }
        
        @Test
        @DisplayName("应该验证惩罚值范围")
        void shouldValidatePenaltyRange() {
            // When & Then
            assertThatThrownBy(() -> PromptTemplate.builder()
                .id("test")
                .name("test")
                .content("Hello")
                .frequencyPenalty(-0.1)
                .build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("频率惩罚值必须在-2到2之间");
            
            assertThatThrownBy(() -> PromptTemplate.builder()
                .id("test")
                .name("test")
                .content("Hello")
                .presencePenalty(2.1)
                .build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("存在惩罚值必须在-2到2之间");
        }
        
        @Test
        @DisplayName("应该验证评分值范围")
        void shouldValidateRatingRange() {
            // When & Then
            assertThatThrownBy(() -> PromptTemplate.builder()
                .id("test")
                .name("test")
                .content("Hello")
                .rating(-0.1)
                .build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("评分必须在0到5之间");
            
            assertThatThrownBy(() -> PromptTemplate.builder()
                .id("test")
                .name("test")
                .content("Hello")
                .rating(5.1)
                .build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("评分必须在0到5之间");
        }
    }
    
    @Nested
    @DisplayName("模板方法测试")
    class TemplateMethodTest {
        
        @Test
        @DisplayName("应该正确验证模板参数")
        void shouldValidateTemplateParameters() {
            // Given
            Map<String, Object> validParams = Map.of("name", "Alice", "place", "Wonderland");
            Map<String, Object> missingParams = Map.of("name", "Alice");
            Map<String, Object> extraParams = Map.of("name", "Alice", "place", "Wonderland", "extra", "value");
            
            // When & Then
            assertThatCode(() -> template.validateParameters(validParams))
                .doesNotThrowAnyException();
            
            assertThatThrownBy(() -> template.validateParameters(missingParams))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("缺少必需参数");
            
            assertThatCode(() -> template.validateParameters(extraParams))
                .doesNotThrowAnyException(); // 额外参数应该被忽略
        }
        
        @Test
        @DisplayName("应该正确渲染模板内容")
        void shouldRenderTemplateContent() {
            // Given
            Map<String, Object> params = Map.of("name", "Alice", "place", "Wonderland");
            
            // When
            String result = template.render(params);
            
            // Then
            assertThat(result).isEqualTo("Hello Alice, welcome to Wonderland!");
        }
        
        @Test
        @DisplayName("渲染时应该忽略未使用的参数")
        void shouldIgnoreUnusedParameters() {
            // Given
            Map<String, Object> params = Map.of("name", "Alice", "place", "Wonderland", "unused", "value");
            
            // When
            String result = template.render(params);
            
            // Then
            assertThat(result).isEqualTo("Hello Alice, welcome to Wonderland!");
        }
        
        @Test
        @DisplayName("渲染时应该抛出异常当缺少必需参数")
        void shouldThrowExceptionWhenMissingRequiredParameters() {
            // Given
            Map<String, Object> params = Map.of("name", "Alice"); // 缺少 place 参数
            
            // When & Then
            assertThatThrownBy(() -> template.render(params))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("缺少必需参数");
        }
        
        @Test
        @DisplayName("应该正确处理空参数值")
        void shouldHandleEmptyParameterValues() {
            // Given
            Map<String, Object> params = Map.of("name", "", "place", null);
            
            // When
            String result = template.render(params);
            
            // Then
            assertThat(result).isEqualTo("Hello , welcome to null!");
        }
        
        @Test
        @DisplayName("应该正确处理嵌套参数")
        void shouldHandleNestedParameters() {
            // Given
            PromptTemplate nestedTemplate = PromptTemplate.builder()
                .id("nested")
                .name("嵌套模板")
                .content("User ${user.name} from ${user.address.city}")
                .parameters(Arrays.asList("user.name", "user.address.city"))
                .build();
            
            Map<String, Object> params = Map.of(
                "user.name", "Alice",
                "user.address.city", "New York"
            );
            
            // When
            String result = nestedTemplate.render(params);
            
            // Then
            assertThat(result).isEqualTo("User Alice from New York");
        }
        
        @Test
        @DisplayName("应该正确克隆模板")
        void shouldCloneTemplate() {
            // When
            PromptTemplate cloned = template.clone();
            
            // Then
            assertThat(cloned).isNotNull();
            assertThat(cloned.getId()).isEqualTo(template.getId());
            assertThat(cloned.getName()).isEqualTo(template.getName());
            assertThat(cloned.getContent()).isEqualTo(template.getContent());
            assertThat(cloned).isNotSameAs(template);
        }
        
        @Test
        @DisplayName("应该正确更新使用时间")
        void shouldUpdateUsageTime() {
            // Given
            Date originalTime = template.getUpdatedAt();
            
            // When
            try {
                Thread.sleep(10); // 确保时间有变化
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            template.updateUsageTime();
            
            // Then
            assertThat(template.getUpdatedAt()).isAfter(originalTime);
        }
        
        @Test
        @DisplayName("应该正确增加使用计数")
        void shouldIncrementUsageCount() {
            // Given
            int originalCount = template.getUsageCount();
            
            // When
            template.incrementUsageCount();
            
            // Then
            assertThat(template.getUsageCount()).isEqualTo(originalCount + 1);
        }
        
        @Test
        @DisplayName("应该正确验证模板有效性")
        void shouldValidateTemplateValidity() {
            // When & Then
            assertThatCode(() -> template.validate())
                .doesNotThrowAnyException();
            
            // 测试无效模板
            PromptTemplate invalidTemplate = PromptTemplate.builder()
                .id("invalid")
                .name("")
                .content("")
                .build();
            
            assertThatThrownBy(() -> invalidTemplate.validate())
                .isInstanceOf(IllegalStateException.class);
        }
        
        @Test
        @DisplayName("应该正确生成模板摘要")
        void shouldGenerateTemplateSummary() {
            // When
            String summary = template.getSummary();
            
            // Then
            assertThat(summary).isNotNull();
            assertThat(summary).contains(template.getName());
            assertThat(summary).contains(template.getId());
            assertThat(summary).contains(String.valueOf(template.getContent().length()));
        }
        
        @Test
        @DisplayName("应该正确检查是否包含参数")
        void shouldCheckIfContainsParameter() {
            // When & Then
            assertThat(template.containsParameter("name")).isTrue();
            assertThat(template.containsParameter("place")).isTrue();
            assertThat(template.containsParameter("nonexistent")).isFalse();
        }
        
        @Test
        @DisplayName("应该正确获取参数数量")
        void shouldGetParameterCount() {
            // When & Then
            assertThat(template.getParameterCount()).isEqualTo(2);
        }
        
        @Test
        @DisplayName("应该正确转换为字符串")
        void shouldConvertToString() {
            // When
            String str = template.toString();
            
            // Then
            assertThat(str).isNotNull();
            assertThat(str).contains(template.getId());
            assertThat(str).contains(template.getName());
        }
    }
    
    @Nested
    @DisplayName("模板类型测试")
    class TemplateTypeTest {
        
        @Test
        @DisplayName("应该正确处理不同的模板类型")
        void shouldHandleDifferentTemplateTypes() {
            // Given
            PromptTemplate systemTemplate = PromptTemplate.builder()
                .id("system")
                .name("系统模板")
                .content("System prompt")
                .type(PromptTemplate.TemplateType.SYSTEM)
                .build();
            
            PromptTemplate userTemplate = PromptTemplate.builder()
                .id("user")
                .name("用户模板")
                .content("User prompt")
                .type(PromptTemplate.TemplateType.USER)
                .build();
            
            PromptTemplate assistantTemplate = PromptTemplate.builder()
                .id("assistant")
                .name("助手模板")
                .content("Assistant prompt")
                .type(PromptTemplate.TemplateType.ASSISTANT)
                .build();
            
            // When & Then
            assertThat(systemTemplate.getType()).isEqualTo(PromptTemplate.TemplateType.SYSTEM);
            assertThat(userTemplate.getType()).isEqualTo(PromptTemplate.TemplateType.USER);
            assertThat(assistantTemplate.getType()).isEqualTo(PromptTemplate.TemplateType.ASSISTANT);
        }
    }
}