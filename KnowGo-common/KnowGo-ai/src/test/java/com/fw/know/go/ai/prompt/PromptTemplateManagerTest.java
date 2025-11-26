package com.fw.know.go.ai.prompt;

import com.fw.know.go.ai.multimodal.MultimodalContent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * 提示词模板管理器测试类
 */
@DisplayName("提示词模板管理器测试")
@ExtendWith(MockitoExtension.class)
class PromptTemplateManagerTest {
    
    private PromptTemplateManager templateManager;
    
    @Mock
    private PromptTemplateExecutor mockExecutor;
    
    @BeforeEach
    void setUp() {
        templateManager = new PromptTemplateManager(mockExecutor);
    }
    
    @Nested
    @DisplayName("模板注册测试")
    class TemplateRegistrationTest {
        
        @Test
        @DisplayName("应该成功注册新模板")
        void shouldRegisterNewTemplate() {
            // Given
            PromptTemplate template = PromptTemplate.builder()
                .id("test-template")
                .name("Test Template")
                .content("Hello {{name}}, welcome to {{place}}!")
                .description("A test template")
                .category("test")
                .tags(Set.of("greeting", "welcome"))
                .build();
            
            // When
            boolean result = templateManager.registerTemplate(template);
            
            // Then
            assertThat(result).isTrue();
            assertThat(templateManager.getTemplate("test-template")).isPresent();
            assertThat(templateManager.getTemplate("test-template").get()).isEqualTo(template);
        }
        
        @Test
        @DisplayName("注册时应该抛出异常当模板ID已存在")
        void shouldThrowExceptionWhenTemplateIdExists() {
            // Given
            PromptTemplate template1 = PromptTemplate.builder()
                .id("duplicate-template")
                .name("Template 1")
                .content("Content 1")
                .build();
            
            PromptTemplate template2 = PromptTemplate.builder()
                .id("duplicate-template")
                .name("Template 2")
                .content("Content 2")
                .build();
            
            // When
            templateManager.registerTemplate(template1);
            
            // Then
            assertThatThrownBy(() -> templateManager.registerTemplate(template2))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("模板ID已存在");
        }
        
        @Test
        @DisplayName("应该成功注册多个不同模板")
        void shouldRegisterMultipleDifferentTemplates() {
            // Given
            PromptTemplate template1 = PromptTemplate.builder()
                .id("template-1")
                .name("Template 1")
                .content("Content 1")
                .build();
            
            PromptTemplate template2 = PromptTemplate.builder()
                .id("template-2")
                .name("Template 2")
                .content("Content 2")
                .build();
            
            PromptTemplate template3 = PromptTemplate.builder()
                .id("template-3")
                .name("Template 3")
                .content("Content 3")
                .build();
            
            // When
            boolean result1 = templateManager.registerTemplate(template1);
            boolean result2 = templateManager.registerTemplate(template2);
            boolean result3 = templateManager.registerTemplate(template3);
            
            // Then
            assertThat(result1).isTrue();
            assertThat(result2).isTrue();
            assertThat(result3).isTrue();
            assertThat(templateManager.getTemplateCount()).isEqualTo(3);
        }
        
        @Test
        @DisplayName("应该成功批量注册模板")
        void shouldRegisterTemplatesInBatch() {
            // Given
            List<PromptTemplate> templates = Arrays.asList(
                PromptTemplate.builder().id("batch-1").name("Batch 1").content("Content 1").build(),
                PromptTemplate.builder().id("batch-2").name("Batch 2").content("Content 2").build(),
                PromptTemplate.builder().id("batch-3").name("Batch 3").content("Content 3").build()
            );
            
            // When
            int registeredCount = templateManager.registerTemplates(templates);
            
            // Then
            assertThat(registeredCount).isEqualTo(3);
            assertThat(templateManager.getTemplateCount()).isEqualTo(3);
            assertThat(templateManager.getTemplate("batch-1")).isPresent();
            assertThat(templateManager.getTemplate("batch-2")).isPresent();
            assertThat(templateManager.getTemplate("batch-3")).isPresent();
        }
        
        @Test
        @DisplayName("批量注册时应该跳过已存在的模板")
        void shouldSkipExistingTemplatesInBatch() {
            // Given
            PromptTemplate existingTemplate = PromptTemplate.builder()
                .id("existing-template")
                .name("Existing Template")
                .content("Existing Content")
                .build();
            
            List<PromptTemplate> templates = Arrays.asList(
                existingTemplate,
                PromptTemplate.builder().id("new-template-1").name("New 1").content("New Content 1").build(),
                PromptTemplate.builder().id("new-template-2").name("New 2").content("New Content 2").build()
            );
            
            // When
            templateManager.registerTemplate(existingTemplate);
            int registeredCount = templateManager.registerTemplates(templates);
            
            // Then
            assertThat(registeredCount).isEqualTo(2); // 只有2个新模板被注册
            assertThat(templateManager.getTemplateCount()).isEqualTo(3);
        }
    }
    
    @Nested
    @DisplayName("模板获取测试")
    class TemplateRetrievalTest {
        
        @Test
        @DisplayName("应该成功获取存在的模板")
        void shouldGetExistingTemplate() {
            // Given
            PromptTemplate template = PromptTemplate.builder()
                .id("existing-template")
                .name("Existing Template")
                .content("Existing Content")
                .build();
            
            templateManager.registerTemplate(template);
            
            // When
            Optional<PromptTemplate> result = templateManager.getTemplate("existing-template");
            
            // Then
            assertThat(result).isPresent();
            assertThat(result.get()).isEqualTo(template);
        }
        
        @Test
        @DisplayName("应该返回空当获取不存在的模板")
        void shouldReturnEmptyWhenTemplateNotExists() {
            // When
            Optional<PromptTemplate> result = templateManager.getTemplate("nonexistent-template");
            
            // Then
            assertThat(result).isEmpty();
        }
        
        @Test
        @DisplayName("应该成功获取所有模板")
        void shouldGetAllTemplates() {
            // Given
            PromptTemplate template1 = PromptTemplate.builder().id("all-1").name("All 1").content("Content 1").build();
            PromptTemplate template2 = PromptTemplate.builder().id("all-2").name("All 2").content("Content 2").build();
            PromptTemplate template3 = PromptTemplate.builder().id("all-3").name("All 3").content("Content 3").build();
            
            templateManager.registerTemplate(template1);
            templateManager.registerTemplate(template2);
            templateManager.registerTemplate(template3);
            
            // When
            List<PromptTemplate> allTemplates = templateManager.getAllTemplates();
            
            // Then
            assertThat(allTemplates).hasSize(3);
            assertThat(allTemplates).containsExactlyInAnyOrder(template1, template2, template3);
        }
        
        @Test
        @DisplayName("应该成功获取模板数量")
        void shouldGetTemplateCount() {
            // Given
            templateManager.registerTemplate(PromptTemplate.builder().id("count-1").name("Count 1").content("Content 1").build());
            templateManager.registerTemplate(PromptTemplate.builder().id("count-2").name("Count 2").content("Content 2").build());
            
            // When & Then
            assertThat(templateManager.getTemplateCount()).isEqualTo(2);
        }
        
        @Test
        @DisplayName("应该成功检查模板是否存在")
        void shouldCheckIfTemplateExists() {
            // Given
            PromptTemplate template = PromptTemplate.builder()
                .id("check-template")
                .name("Check Template")
                .content("Check Content")
                .build();
            
            templateManager.registerTemplate(template);
            
            // When & Then
            assertThat(templateManager.hasTemplate("check-template")).isTrue();
            assertThat(templateManager.hasTemplate("nonexistent-template")).isFalse();
        }
        
        @Test
        @DisplayName("应该成功按类别获取模板")
        void shouldGetTemplatesByCategory() {
            // Given
            PromptTemplate template1 = PromptTemplate.builder().id("cat-1").name("Cat 1").content("Content 1").category("category-a").build();
            PromptTemplate template2 = PromptTemplate.builder().id("cat-2").name("Cat 2").content("Content 2").category("category-a").build();
            PromptTemplate template3 = PromptTemplate.builder().id("cat-3").name("Cat 3").content("Content 3").category("category-b").build();
            
            templateManager.registerTemplate(template1);
            templateManager.registerTemplate(template2);
            templateManager.registerTemplate(template3);
            
            // When
            List<PromptTemplate> categoryATemplates = templateManager.getTemplatesByCategory("category-a");
            List<PromptTemplate> categoryBTemplates = templateManager.getTemplatesByCategory("category-b");
            List<PromptTemplate> categoryCTemplates = templateManager.getTemplatesByCategory("category-c");
            
            // Then
            assertThat(categoryATemplates).hasSize(2);
            assertThat(categoryATemplates).containsExactlyInAnyOrder(template1, template2);
            assertThat(categoryBTemplates).hasSize(1);
            assertThat(categoryBTemplates).containsExactly(template3);
            assertThat(categoryCTemplates).isEmpty();
        }
        
        @Test
        @DisplayName("应该成功按标签获取模板")
        void shouldGetTemplatesByTag() {
            // Given
            PromptTemplate template1 = PromptTemplate.builder().id("tag-1").name("Tag 1").content("Content 1").tags(Set.of("tag-a", "tag-b")).build();
            PromptTemplate template2 = PromptTemplate.builder().id("tag-2").name("Tag 2").content("Content 2").tags(Set.of("tag-a", "tag-c")).build();
            PromptTemplate template3 = PromptTemplate.builder().id("tag-3").name("Tag 3").content("Content 3").tags(Set.of("tag-b", "tag-d")).build();
            
            templateManager.registerTemplate(template1);
            templateManager.registerTemplate(template2);
            templateManager.registerTemplate(template3);
            
            // When
            List<PromptTemplate> tagATemplates = templateManager.getTemplatesByTag("tag-a");
            List<PromptTemplate> tagBTemplates = templateManager.getTemplatesByTag("tag-b");
            List<PromptTemplate> tagETemplates = templateManager.getTemplatesByTag("tag-e");
            
            // Then
            assertThat(tagATemplates).hasSize(2);
            assertThat(tagATemplates).containsExactlyInAnyOrder(template1, template2);
            assertThat(tagBTemplates).hasSize(2);
            assertThat(tagBTemplates).containsExactlyInAnyOrder(template1, template3);
            assertThat(tagETemplates).isEmpty();
        }
        
        @Test
        @DisplayName("应该成功按名称搜索模板")
        void shouldSearchTemplatesByName() {
            // Given
            PromptTemplate template1 = PromptTemplate.builder().id("search-1").name("User Greeting Template").content("Content 1").build();
            PromptTemplate template2 = PromptTemplate.builder().id("search-2").name("Admin Dashboard Template").content("Content 2").build();
            PromptTemplate template3 = PromptTemplate.builder().id("search-3").name("Product Description Template").content("Content 3").build();
            
            templateManager.registerTemplate(template1);
            templateManager.registerTemplate(template2);
            templateManager.registerTemplate(template3);
            
            // When
            List<PromptTemplate> greetingResults = templateManager.searchTemplates("greeting");
            List<PromptTemplate> adminResults = templateManager.searchTemplates("admin");
            List<PromptTemplate> templateResults = templateManager.searchTemplates("template");
            
            // Then
            assertThat(greetingResults).hasSize(1);
            assertThat(greetingResults).containsExactly(template1);
            assertThat(adminResults).hasSize(1);
            assertThat(adminResults).containsExactly(template2);
            assertThat(templateResults).hasSize(3); // 所有模板都包含"Template"
        }
        
        @Test
        @DisplayName("应该成功按内容搜索模板")
        void shouldSearchTemplatesByContent() {
            // Given
            PromptTemplate template1 = PromptTemplate.builder().id("content-1").name("Template 1").content("Generate a SQL query for {{table}}").build();
            PromptTemplate template2 = PromptTemplate.builder().id("content-2").name("Template 2").content("Create a Python function for {{task}}").build();
            PromptTemplate template3 = PromptTemplate.builder().id("content-3").name("Template 3").content("Write a SQL query to select {{columns}} from {{table}}").build();
            
            templateManager.registerTemplate(template1);
            templateManager.registerTemplate(template2);
            templateManager.registerTemplate(template3);
            
            // When
            List<PromptTemplate> sqlResults = templateManager.searchTemplates("SQL");
            List<PromptTemplate> queryResults = templateManager.searchTemplates("query");
            List<PromptTemplate> pythonResults = templateManager.searchTemplates("Python");
            
            // Then
            assertThat(sqlResults).hasSize(2);
            assertThat(sqlResults).containsExactlyInAnyOrder(template1, template3);
            assertThat(queryResults).hasSize(2);
            assertThat(queryResults).containsExactlyInAnyOrder(template1, template3);
            assertThat(pythonResults).hasSize(1);
            assertThat(pythonResults).containsExactly(template2);
        }
    }
    
    @Nested
    @DisplayName("模板更新测试")
    class TemplateUpdateTest {
        
        @Test
        @DisplayName("应该成功更新存在的模板")
        void shouldUpdateExistingTemplate() {
            // Given
            PromptTemplate originalTemplate = PromptTemplate.builder()
                .id("update-template")
                .name("Original Name")
                .content("Original Content")
                .description("Original Description")
                .category("original-category")
                .tags(Set.of("original-tag"))
                .build();
            
            templateManager.registerTemplate(originalTemplate);
            
            PromptTemplate updatedTemplate = PromptTemplate.builder()
                .id("update-template")
                .name("Updated Name")
                .content("Updated Content")
                .description("Updated Description")
                .category("updated-category")
                .tags(Set.of("updated-tag"))
                .build();
            
            // When
            boolean result = templateManager.updateTemplate(updatedTemplate);
            
            // Then
            assertThat(result).isTrue();
            Optional<PromptTemplate> retrievedTemplate = templateManager.getTemplate("update-template");
            assertThat(retrievedTemplate).isPresent();
            assertThat(retrievedTemplate.get().getName()).isEqualTo("Updated Name");
            assertThat(retrievedTemplate.get().getContent()).isEqualTo("Updated Content");
            assertThat(retrievedTemplate.get().getDescription()).isEqualTo("Updated Description");
            assertThat(retrievedTemplate.get().getCategory()).isEqualTo("updated-category");
            assertThat(retrievedTemplate.get().getTags()).containsExactly("updated-tag");
        }
        
        @Test
        @DisplayName("更新时应该返回false当模板不存在")
        void shouldReturnFalseWhenUpdatingNonexistentTemplate() {
            // Given
            PromptTemplate nonexistentTemplate = PromptTemplate.builder()
                .id("nonexistent-template")
                .name("Nonexistent Template")
                .content("Nonexistent Content")
                .build();
            
            // When
            boolean result = templateManager.updateTemplate(nonexistentTemplate);
            
            // Then
            assertThat(result).isFalse();
        }
        
        @Test
        @DisplayName("应该成功更新模板内容")
        void shouldUpdateTemplateContent() {
            // Given
            PromptTemplate template = PromptTemplate.builder()
                .id("content-update-template")
                .name("Content Update Template")
                .content("Original Content")
                .build();
            
            templateManager.registerTemplate(template);
            
            // When
            boolean result = templateManager.updateTemplateContent("content-update-template", "Updated Content");
            
            // Then
            assertThat(result).isTrue();
            Optional<PromptTemplate> updatedTemplate = templateManager.getTemplate("content-update-template");
            assertThat(updatedTemplate).isPresent();
            assertThat(updatedTemplate.get().getContent()).isEqualTo("Updated Content");
        }
        
        @Test
        @DisplayName("更新内容时应该返回false当模板不存在")
        void shouldReturnFalseWhenUpdatingContentOfNonexistentTemplate() {
            // When
            boolean result = templateManager.updateTemplateContent("nonexistent-template", "New Content");
            
            // Then
            assertThat(result).isFalse();
        }
    }
    
    @Nested
    @DisplayName("模板删除测试")
    class TemplateDeletionTest {
        
        @Test
        @DisplayName("应该成功删除存在的模板")
        void shouldDeleteExistingTemplate() {
            // Given
            PromptTemplate template = PromptTemplate.builder()
                .id("delete-template")
                .name("Delete Template")
                .content("Delete Content")
                .build();
            
            templateManager.registerTemplate(template);
            
            // When
            boolean result = templateManager.deleteTemplate("delete-template");
            
            // Then
            assertThat(result).isTrue();
            assertThat(templateManager.hasTemplate("delete-template")).isFalse();
            assertThat(templateManager.getTemplateCount()).isEqualTo(0);
        }
        
        @Test
        @DisplayName("删除时应该返回false当模板不存在")
        void shouldReturnFalseWhenDeletingNonexistentTemplate() {
            // When
            boolean result = templateManager.deleteTemplate("nonexistent-template");
            
            // Then
            assertThat(result).isFalse();
        }
        
        @Test
        @DisplayName("应该成功批量删除模板")
        void shouldDeleteTemplatesInBatch() {
            // Given
            PromptTemplate template1 = PromptTemplate.builder().id("batch-delete-1").name("Batch Delete 1").content("Content 1").build();
            PromptTemplate template2 = PromptTemplate.builder().id("batch-delete-2").name("Batch Delete 2").content("Content 2").build();
            PromptTemplate template3 = PromptTemplate.builder().id("batch-delete-3").name("Batch Delete 3").content("Content 3").build();
            
            templateManager.registerTemplate(template1);
            templateManager.registerTemplate(template2);
            templateManager.registerTemplate(template3);
            
            // When
            int deletedCount = templateManager.deleteTemplates(Arrays.asList("batch-delete-1", "batch-delete-2", "batch-delete-3"));
            
            // Then
            assertThat(deletedCount).isEqualTo(3);
            assertThat(templateManager.getTemplateCount()).isEqualTo(0);
            assertThat(templateManager.hasTemplate("batch-delete-1")).isFalse();
            assertThat(templateManager.hasTemplate("batch-delete-2")).isFalse();
            assertThat(templateManager.hasTemplate("batch-delete-3")).isFalse();
        }
        
        @Test
        @DisplayName("批量删除时应该只删除存在的模板")
        void shouldOnlyDeleteExistingTemplatesInBatch() {
            // Given
            PromptTemplate template1 = PromptTemplate.builder().id("existing-1").name("Existing 1").content("Content 1").build();
            PromptTemplate template2 = PromptTemplate.builder().id("existing-2").name("Existing 2").content("Content 2").build();
            
            templateManager.registerTemplate(template1);
            templateManager.registerTemplate(template2);
            
            // When
            int deletedCount = templateManager.deleteTemplates(Arrays.asList("existing-1", "nonexistent", "existing-2"));
            
            // Then
            assertThat(deletedCount).isEqualTo(2); // 只有2个存在的模板被删除
            assertThat(templateManager.getTemplateCount()).isEqualTo(0);
        }
        
        @Test
        @DisplayName("应该成功清空所有模板")
        void shouldClearAllTemplates() {
            // Given
            templateManager.registerTemplate(PromptTemplate.builder().id("clear-1").name("Clear 1").content("Content 1").build());
            templateManager.registerTemplate(PromptTemplate.builder().id("clear-2").name("Clear 2").content("Content 2").build());
            templateManager.registerTemplate(PromptTemplate.builder().id("clear-3").name("Clear 3").content("Content 3").build());
            
            // When
            templateManager.clearAllTemplates();
            
            // Then
            assertThat(templateManager.getTemplateCount()).isEqualTo(0);
            assertThat(templateManager.hasTemplate("clear-1")).isFalse();
            assertThat(templateManager.hasTemplate("clear-2")).isFalse();
            assertThat(templateManager.hasTemplate("clear-3")).isFalse();
        }
    }
    
    @Nested
    @DisplayName("模板执行测试")
    class TemplateExecutionTest {
        
        @Test
        @DisplayName("应该成功执行模板")
        void shouldExecuteTemplate() {
            // Given
            PromptTemplate template = PromptTemplate.builder()
                .id("execute-template")
                .name("Execute Template")
                .content("Hello {{name}}, welcome to {{place}}!")
                .build();
            
            templateManager.registerTemplate(template);
            
            PromptExecutionRequest request = PromptExecutionRequest.builder()
                .templateId("execute-template")
                .parameters(Map.of("name", "Alice", "place", "Wonderland"))
                .build();
            
            PromptExecutionResponse expectedResponse = PromptExecutionResponse.builder()
                .requestId(request.getRequestId())
                .templateId("execute-template")
                .content("Hello Alice, welcome to Wonderland!")
                .status(ExecutionStatus.SUCCESS)
                .build();
            
            when(mockExecutor.execute(any(PromptTemplate.class), any(PromptExecutionRequest.class)))
                .thenReturn(expectedResponse);
            
            // When
            PromptExecutionResponse response = templateManager.executeTemplate(request);
            
            // Then
            assertThat(response).isNotNull();
            assertThat(response.getContent()).isEqualTo("Hello Alice, welcome to Wonderland!");
            assertThat(response.getStatus()).isEqualTo(ExecutionStatus.SUCCESS);
            
            verify(mockExecutor).execute(any(PromptTemplate.class), any(PromptExecutionRequest.class));
        }
        
        @Test
        @DisplayName("执行时应该抛出异常当模板不存在")
        void shouldThrowExceptionWhenExecutingNonexistentTemplate() {
            // Given
            PromptExecutionRequest request = PromptExecutionRequest.builder()
                .templateId("nonexistent-template")
                .parameters(Map.of("name", "Alice"))
                .build();
            
            // When & Then
            assertThatThrownBy(() -> templateManager.executeTemplate(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("模板不存在");
            
            verify(mockExecutor, never()).execute(any(), any());
        }
        
        @Test
        @DisplayName("应该成功异步执行模板")
        void shouldExecuteTemplateAsync() {
            // Given
            PromptTemplate template = PromptTemplate.builder()
                .id("async-template")
                .name("Async Template")
                .content("Hello {{name}}!")
                .build();
            
            templateManager.registerTemplate(template);
            
            PromptExecutionRequest request = PromptExecutionRequest.builder()
                .templateId("async-template")
                .parameters(Map.of("name", "Bob"))
                .build();
            
            PromptExecutionResponse expectedResponse = PromptExecutionResponse.builder()
                .requestId(request.getRequestId())
                .templateId("async-template")
                .content("Hello Bob!")
                .status(ExecutionStatus.SUCCESS)
                .build();
            
            when(mockExecutor.executeAsync(any(PromptTemplate.class), any(PromptExecutionRequest.class)))
                .thenReturn(CompletableFuture.completedFuture(expectedResponse));
            
            // When
            CompletableFuture<PromptExecutionResponse> future = templateManager.executeTemplateAsync(request);
            PromptExecutionResponse response = future.join();
            
            // Then
            assertThat(response).isNotNull();
            assertThat(response.getContent()).isEqualTo("Hello Bob!");
            assertThat(response.getStatus()).isEqualTo(ExecutionStatus.SUCCESS);
            
            verify(mockExecutor).executeAsync(any(PromptTemplate.class), any(PromptExecutionRequest.class));
        }
        
        @Test
        @DisplayName("应该成功批量执行模板")
        void shouldExecuteTemplatesInBatch() {
            // Given
            PromptTemplate template1 = PromptTemplate.builder().id("batch-exec-1").name("Batch 1").content("Hello {{name}}!").build();
            PromptTemplate template2 = PromptTemplate.builder().id("batch-exec-2").name("Batch 2").content("Goodbye {{name}}!").build();
            
            templateManager.registerTemplate(template1);
            templateManager.registerTemplate(template2);
            
            List<PromptExecutionRequest> requests = Arrays.asList(
                PromptExecutionRequest.builder().templateId("batch-exec-1").parameters(Map.of("name", "Alice")).build(),
                PromptExecutionRequest.builder().templateId("batch-exec-2").parameters(Map.of("name", "Bob")).build()
            );
            
            PromptExecutionResponse response1 = PromptExecutionResponse.builder()
                .requestId(requests.get(0).getRequestId())
                .templateId("batch-exec-1")
                .content("Hello Alice!")
                .status(ExecutionStatus.SUCCESS)
                .build();
            
            PromptExecutionResponse response2 = PromptExecutionResponse.builder()
                .requestId(requests.get(1).getRequestId())
                .templateId("batch-exec-2")
                .content("Goodbye Bob!")
                .status(ExecutionStatus.SUCCESS)
                .build();
            
            when(mockExecutor.execute(any(PromptTemplate.class), any(PromptExecutionRequest.class)))
                .thenReturn(response1)
                .thenReturn(response2);
            
            // When
            List<PromptExecutionResponse> responses = templateManager.executeTemplates(requests);
            
            // Then
            assertThat(responses).hasSize(2);
            assertThat(responses.get(0).getContent()).isEqualTo("Hello Alice!");
            assertThat(responses.get(1).getContent()).isEqualTo("Goodbye Bob!");
            
            verify(mockExecutor, times(2)).execute(any(PromptTemplate.class), any(PromptExecutionRequest.class));
        }
    }
    
    @Nested
    @DisplayName("模板统计测试")
    class TemplateStatisticsTest {
        
        @Test
        @DisplayName("应该正确获取模板使用统计")
        void shouldGetTemplateUsageStatistics() {
            // Given
            PromptTemplate template1 = PromptTemplate.builder().id("stat-1").name("Stat 1").content("Content 1").usageCount(10).build();
            PromptTemplate template2 = PromptTemplate.builder().id("stat-2").name("Stat 2").content("Content 2").usageCount(5).build();
            PromptTemplate template3 = PromptTemplate.builder().id("stat-3").name("Stat 3").content("Content 3").usageCount(15).build();
            
            templateManager.registerTemplate(template1);
            templateManager.registerTemplate(template2);
            templateManager.registerTemplate(template3);
            
            // When
            Map<String, Object> statistics = templateManager.getTemplateStatistics();
            
            // Then
            assertThat(statistics).isNotNull();
            assertThat(statistics).containsEntry("totalTemplates", 3);
            assertThat(statistics).containsEntry("totalUsageCount", 30);
            assertThat(statistics).containsEntry("averageUsageCount", 10.0);
            assertThat(statistics).containsEntry("mostUsedTemplateId", "stat-3");
            assertThat(statistics).containsEntry("leastUsedTemplateId", "stat-2");
        }
        
        @Test
        @DisplayName("应该正确获取类别统计")
        void shouldGetCategoryStatistics() {
            // Given
            PromptTemplate template1 = PromptTemplate.builder().id("cat-stat-1").name("Cat 1").content("Content 1").category("category-a").build();
            PromptTemplate template2 = PromptTemplate.builder().id("cat-stat-2").name("Cat 2").content("Content 2").category("category-a").build();
            PromptTemplate template3 = PromptTemplate.builder().id("cat-stat-3").name("Cat 3").content("Content 3").category("category-b").build();
            
            templateManager.registerTemplate(template1);
            templateManager.registerTemplate(template2);
            templateManager.registerTemplate(template3);
            
            // When
            Map<String, Long> categoryStatistics = templateManager.getCategoryStatistics();
            
            // Then
            assertThat(categoryStatistics).isNotNull();
            assertThat(categoryStatistics).containsEntry("category-a", 2L);
            assertThat(categoryStatistics).containsEntry("category-b", 1L);
        }
        
        @Test
        @DisplayName("应该正确获取标签统计")
        void shouldGetTagStatistics() {
            // Given
            PromptTemplate template1 = PromptTemplate.builder().id("tag-stat-1").name("Tag 1").content("Content 1").tags(Set.of("tag-a", "tag-b")).build();
            PromptTemplate template2 = PromptTemplate.builder().id("tag-stat-2").name("Tag 2").content("Content 2").tags(Set.of("tag-a", "tag-c")).build();
            PromptTemplate template3 = PromptTemplate.builder().id("tag-stat-3").name("Tag 3").content("Content 3").tags(Set.of("tag-b", "tag-d")).build();
            
            templateManager.registerTemplate(template1);
            templateManager.registerTemplate(template2);
            templateManager.registerTemplate(template3);
            
            // When
            Map<String, Long> tagStatistics = templateManager.getTagStatistics();
            
            // Then
            assertThat(tagStatistics).isNotNull();
            assertThat(tagStatistics).containsEntry("tag-a", 2L);
            assertThat(tagStatistics).containsEntry("tag-b", 2L);
            assertThat(tagStatistics).containsEntry("tag-c", 1L);
            assertThat(tagStatistics).containsEntry("tag-d", 1L);
        }
        
        @Test
        @DisplayName("应该正确获取热门模板")
        void shouldGetPopularTemplates() {
            // Given
            PromptTemplate template1 = PromptTemplate.builder().id("pop-1").name("Pop 1").content("Content 1").usageCount(20).build();
            PromptTemplate template2 = PromptTemplate.builder().id("pop-2").name("Pop 2").content("Content 2").usageCount(15).build();
            PromptTemplate template3 = PromptTemplate.builder().id("pop-3").name("Pop 3").content("Content 3").usageCount(10).build();
            PromptTemplate template4 = PromptTemplate.builder().id("pop-4").name("Pop 4").content("Content 4").usageCount(5).build();
            
            templateManager.registerTemplate(template1);
            templateManager.registerTemplate(template2);
            templateManager.registerTemplate(template3);
            templateManager.registerTemplate(template4);
            
            // When
            List<PromptTemplate> top3Templates = templateManager.getPopularTemplates(3);
            List<PromptTemplate> top2Templates = templateManager.getPopularTemplates(2);
            
            // Then
            assertThat(top3Templates).hasSize(3);
            assertThat(top3Templates).containsExactly(template1, template2, template3);
            assertThat(top2Templates).hasSize(2);
            assertThat(top2Templates).containsExactly(template1, template2);
        }
        
        @Test
        @DisplayName("应该正确获取最近使用的模板")
        void shouldGetRecentlyUsedTemplates() {
            // Given
            LocalDateTime now = LocalDateTime.now();
            PromptTemplate template1 = PromptTemplate.builder().id("recent-1").name("Recent 1").content("Content 1").lastUsed(now.minusHours(1)).build();
            PromptTemplate template2 = PromptTemplate.builder().id("recent-2").name("Recent 2").content("Content 2").lastUsed(now.minusHours(2)).build();
            PromptTemplate template3 = PromptTemplate.builder().id("recent-3").name("Recent 3").content("Content 3").lastUsed(now.minusHours(3)).build();
            PromptTemplate template4 = PromptTemplate.builder().id("recent-4").name("Recent 4").content("Content 4").lastUsed(null).build();
            
            templateManager.registerTemplate(template1);
            templateManager.registerTemplate(template2);
            templateManager.registerTemplate(template3);
            templateManager.registerTemplate(template4);
            
            // When
            List<PromptTemplate> recentTemplates = templateManager.getRecentlyUsedTemplates(3);
            
            // Then
            assertThat(recentTemplates).hasSize(3);
            assertThat(recentTemplates).containsExactly(template1, template2, template3);
            assertThat(recentTemplates).doesNotContain(template4); // 没有使用记录的模板不应该出现
        }
    }
    
    @Nested
    @DisplayName("模板验证测试")
    class TemplateValidationTest {
        
        @Test
        @DisplayName("应该成功验证模板")
        void shouldValidateTemplate() {
            // Given
            PromptTemplate validTemplate = PromptTemplate.builder()
                .id("valid-template")
                .name("Valid Template")
                .content("Hello {{name}}!")
                .build();
            
            // When & Then
            assertThatCode(() -> templateManager.validateTemplate(validTemplate))
                .doesNotThrowAnyException();
        }
        
        @Test
        @DisplayName("验证时应该抛出异常当模板无效")
        void shouldThrowExceptionWhenTemplateInvalid() {
            // Given
            PromptTemplate invalidTemplate = PromptTemplate.builder()
                .id("")
                .name("")
                .content("")
                .build();
            
            // When & Then
            assertThatThrownBy(() -> templateManager.validateTemplate(invalidTemplate))
                .isInstanceOf(IllegalArgumentException.class);
        }
        
        @Test
        @DisplayName("应该成功验证模板参数")
        void shouldValidateTemplateParameters() {
            // Given
            PromptTemplate template = PromptTemplate.builder()
                .id("param-template")
                .name("Param Template")
                .content("Hello {{name}}, you are {{age}} years old!")
                .build();
            
            Map<String, Object> validParameters = Map.of("name", "Alice", "age", "25");
            Map<String, Object> invalidParameters = Map.of("name", "Bob"); // 缺少 age 参数
            
            // When & Then
            assertThatCode(() -> templateManager.validateTemplateParameters(template, validParameters))
                .doesNotThrowAnyException();
            
            assertThatCode(() -> templateManager.validateTemplateParameters(template, invalidParameters))
                .doesNotThrowAnyException(); // 模板管理器不强制参数验证
        }
    }
    
    @Nested
    @DisplayName("模板克隆测试")
    class TemplateCloningTest {
        
        @Test
        @DisplayName("应该成功克隆模板")
        void shouldCloneTemplate() {
            // Given
            PromptTemplate originalTemplate = PromptTemplate.builder()
                .id("original-template")
                .name("Original Template")
                .content("Original Content")
                .description("Original Description")
                .category("original-category")
                .tags(Set.of("original-tag"))
                .build();
            
            templateManager.registerTemplate(originalTemplate);
            
            // When
            Optional<PromptTemplate> clonedTemplate = templateManager.cloneTemplate("original-template", "cloned-template");
            
            // Then
            assertThat(clonedTemplate).isPresent();
            assertThat(clonedTemplate.get().getId()).isEqualTo("cloned-template");
            assertThat(clonedTemplate.get().getName()).isEqualTo("Original Template (Clone)");
            assertThat(clonedTemplate.get().getContent()).isEqualTo("Original Content");
            assertThat(clonedTemplate.get().getDescription()).isEqualTo("Original Description");
            assertThat(clonedTemplate.get().getCategory()).isEqualTo("original-category");
            assertThat(clonedTemplate.get().getTags()).containsExactly("original-tag");
            assertThat(clonedTemplate.get()).isNotEqualTo(originalTemplate);
            
            // 验证克隆的模板已注册
            assertThat(templateManager.hasTemplate("cloned-template")).isTrue();
        }
        
        @Test
        @DisplayName("克隆时应该返回空当原模板不存在")
        void shouldReturnEmptyWhenCloningNonexistentTemplate() {
            // When
            Optional<PromptTemplate> clonedTemplate = templateManager.cloneTemplate("nonexistent-template", "cloned-template");
            
            // Then
            assertThat(clonedTemplate).isEmpty();
            assertThat(templateManager.hasTemplate("cloned-template")).isFalse();
        }
        
        @Test
        @DisplayName("克隆时应该抛出异常当目标ID已存在")
        void shouldThrowExceptionWhenCloneTargetIdExists() {
            // Given
            PromptTemplate originalTemplate = PromptTemplate.builder()
                .id("original-template")
                .name("Original Template")
                .content("Original Content")
                .build();
            
            PromptTemplate existingTemplate = PromptTemplate.builder()
                .id("existing-template")
                .name("Existing Template")
                .content("Existing Content")
                .build();
            
            templateManager.registerTemplate(originalTemplate);
            templateManager.registerTemplate(existingTemplate);
            
            // When & Then
            assertThatThrownBy(() -> templateManager.cloneTemplate("original-template", "existing-template"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("目标模板ID已存在");
        }
    }
    
    @Nested
    @DisplayName("模板导入导出测试")
    class TemplateImportExportTest {
        
        @Test
        @DisplayName("应该成功导出模板")
        void shouldExportTemplates() {
            // Given
            PromptTemplate template1 = PromptTemplate.builder().id("export-1").name("Export 1").content("Content 1").build();
            PromptTemplate template2 = PromptTemplate.builder().id("export-2").name("Export 2").content("Content 2").build();
            
            templateManager.registerTemplate(template1);
            templateManager.registerTemplate(template2);
            
            // When
            List<PromptTemplate> exportedTemplates = templateManager.exportTemplates();
            
            // Then
            assertThat(exportedTemplates).hasSize(2);
            assertThat(exportedTemplates).containsExactlyInAnyOrder(template1, template2);
        }
        
        @Test
        @DisplayName("应该成功导出特定类别的模板")
        void shouldExportTemplatesByCategory() {
            // Given
            PromptTemplate template1 = PromptTemplate.builder().id("export-cat-1").name("Export Cat 1").content("Content 1").category("category-a").build();
            PromptTemplate template2 = PromptTemplate.builder().id("export-cat-2").name("Export Cat 2").content("Content 2").category("category-b").build();
            PromptTemplate template3 = PromptTemplate.builder().id("export-cat-3").name("Export Cat 3").content("Content 3").category("category-a").build();
            
            templateManager.registerTemplate(template1);
            templateManager.registerTemplate(template2);
            templateManager.registerTemplate(template3);
            
            // When
            List<PromptTemplate> exportedTemplates = templateManager.exportTemplatesByCategory("category-a");
            
            // Then
            assertThat(exportedTemplates).hasSize(2);
            assertThat(exportedTemplates).containsExactlyInAnyOrder(template1, template3);
        }
        
        @Test
        @DisplayName("应该成功导入模板")
        void shouldImportTemplates() {
            // Given
            List<PromptTemplate> templatesToImport = Arrays.asList(
                PromptTemplate.builder().id("import-1").name("Import 1").content("Content 1").build(),
                PromptTemplate.builder().id("import-2").name("Import 2").content("Content 2").build()
            );
            
            // When
            int importedCount = templateManager.importTemplates(templatesToImport);
            
            // Then
            assertThat(importedCount).isEqualTo(2);
            assertThat(templateManager.getTemplateCount()).isEqualTo(2);
            assertThat(templateManager.hasTemplate("import-1")).isTrue();
            assertThat(templateManager.hasTemplate("import-2")).isTrue();
        }
        
        @Test
        @DisplayName("导入时应该跳过已存在的模板")
        void shouldSkipExistingTemplatesWhenImporting() {
            // Given
            PromptTemplate existingTemplate = PromptTemplate.builder()
                .id("existing-import")
                .name("Existing Import")
                .content("Existing Content")
                .build();
            
            List<PromptTemplate> templatesToImport = Arrays.asList(
                existingTemplate,
                PromptTemplate.builder().id("new-import-1").name("New Import 1").content("New Content 1").build(),
                PromptTemplate.builder().id("new-import-2").name("New Import 2").content("New Content 2").build()
            );
            
            // When
            templateManager.registerTemplate(existingTemplate);
            int importedCount = templateManager.importTemplates(templatesToImport);
            
            // Then
            assertThat(importedCount).isEqualTo(2); // 只有2个新模板被导入
            assertThat(templateManager.getTemplateCount()).isEqualTo(3);
        }
    }
    
    @Nested
    @DisplayName("多模态内容测试")
    class MultimodalContentTest {
        
        @Test
        @DisplayName("应该成功创建包含多模态内容的模板")
        void shouldCreateTemplateWithMultimodalContent() {
            // Given
            MultimodalContent content = MultimodalContent.builder()
                .addText("Analyze this image: {{description}}")
                .addImage("base64:image/png:{{imageData}}")
                .build();
            
            PromptTemplate template = PromptTemplate.builder()
                .id("multimodal-template")
                .name("Multimodal Template")
                .multimodalContent(content)
                .build();
            
            // When
            boolean result = templateManager.registerTemplate(template);
            
            // Then
            assertThat(result).isTrue();
            Optional<PromptTemplate> retrievedTemplate = templateManager.getTemplate("multimodal-template");
            assertThat(retrievedTemplate).isPresent();
            assertThat(retrievedTemplate.get().getMultimodalContent()).isEqualTo(content);
        }
        
        @Test
        @DisplayName("应该成功执行包含多模态内容的模板")
        void shouldExecuteTemplateWithMultimodalContent() {
            // Given
            MultimodalContent content = MultimodalContent.builder()
                .addText("Analyze this image: {{description}}")
                .addImage("base64:image/png:{{imageData}}")
                .build();
            
            PromptTemplate template = PromptTemplate.builder()
                .id("multimodal-exec-template")
                .name("Multimodal Exec Template")
                .multimodalContent(content)
                .build();
            
            templateManager.registerTemplate(template);
            
            PromptExecutionRequest request = PromptExecutionRequest.builder()
                .templateId("multimodal-exec-template")
                .parameters(Map.of("description", "A beautiful sunset", "imageData", "base64encodedimagedata"))
                .build();
            
            PromptExecutionResponse expectedResponse = PromptExecutionResponse.builder()
                .requestId(request.getRequestId())
                .templateId("multimodal-exec-template")
                .content("This image shows a beautiful sunset with vibrant colors.")
                .status(ExecutionStatus.SUCCESS)
                .build();
            
            when(mockExecutor.execute(any(PromptTemplate.class), any(PromptExecutionRequest.class)))
                .thenReturn(expectedResponse);
            
            // When
            PromptExecutionResponse response = templateManager.executeTemplate(request);
            
            // Then
            assertThat(response).isNotNull();
            assertThat(response.getContent()).isEqualTo("This image shows a beautiful sunset with vibrant colors.");
            assertThat(response.getStatus()).isEqualTo(ExecutionStatus.SUCCESS);
            
            verify(mockExecutor).execute(any(PromptTemplate.class), any(PromptExecutionRequest.class));
        }
    }
    
    @Nested
    @DisplayName("错误处理测试")
    class ErrorHandlingTest {
        
        @Test
        @DisplayName("应该正确处理执行器异常")
        void shouldHandleExecutorException() {
            // Given
            PromptTemplate template = PromptTemplate.builder()
                .id("error-template")
                .name("Error Template")
                .content("Content")
                .build();
            
            templateManager.registerTemplate(template);
            
            PromptExecutionRequest request = PromptExecutionRequest.builder()
                .templateId("error-template")
                .parameters(Map.of("name", "Alice"))
                .build();
            
            when(mockExecutor.execute(any(PromptTemplate.class), any(PromptExecutionRequest.class)))
                .thenThrow(new RuntimeException("API调用失败"));
            
            // When & Then
            assertThatThrownBy(() -> templateManager.executeTemplate(request))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("API调用失败");
        }
        
        @Test
        @DisplayName("应该正确处理空参数")
        void shouldHandleNullParameters() {
            // When & Then
            assertThatThrownBy(() -> templateManager.registerTemplate(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("模板不能为空");
            
            assertThatThrownBy(() -> templateManager.updateTemplate(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("模板不能为空");
            
            assertThatThrownBy(() -> templateManager.executeTemplate(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("执行请求不能为空");
        }
        
        @Test
        @DisplayName("应该正确处理空ID")
        void shouldHandleNullIds() {
            // When & Then
            assertThatThrownBy(() -> templateManager.getTemplate(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("模板ID不能为空");
            
            assertThatThrownBy(() -> templateManager.hasTemplate(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("模板ID不能为空");
            
            assertThatThrownBy(() -> templateManager.deleteTemplate(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("模板ID不能为空");
        }
    }
}