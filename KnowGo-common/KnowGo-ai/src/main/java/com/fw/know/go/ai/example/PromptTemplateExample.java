package com.fw.know.go.ai.example;

import com.fw.know.go.ai.prompt.PromptTemplate;
import com.fw.know.go.ai.prompt.PromptTemplateManager;
import com.fw.know.go.ai.prompt.PromptTemplateExecutor;
import com.fw.know.go.ai.prompt.PromptExecutionRequest;
import com.fw.know.go.ai.prompt.PromptExecutionResponse;
import com.fw.know.go.ai.prompt.PromptTemplate.TemplateType;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * 提示词模板使用示例
 */
public class PromptTemplateExample {
    
    private final PromptTemplateManager templateManager;
    private final PromptTemplateExecutor executor;
    
    public PromptTemplateExample(PromptTemplateManager templateManager, PromptTemplateExecutor executor) {
        this.templateManager = templateManager;
        this.executor = executor;
    }
    
    /**
     * 示例1：创建和使用代码审查模板
     */
    public void codeReviewExample() {
        // 创建代码审查模板
        PromptTemplate codeReviewTemplate = PromptTemplate.builder()
            .id("code-review")
            .name("代码审查")
            .content("请作为资深开发工程师，审查以下代码：\n\n"
                    + "```${language}\n${code}\n```\n\n"
                    + "审查要点：\n"
                    + "1. 代码质量和可读性\n"
                    + "2. 性能和安全性\n"
                    + "3. 设计模式和最佳实践\n"
                    + "4. ${focus}\n\n"
                    + "请提供详细的审查意见和改进建议。")
            .category("开发")
            .type(TemplateType.USER)
            .description("用于代码审查的专业模板")
            .modelType("gpt-4")
            .tags(List.of("代码审查", "开发", "质量"))
            .parameters(List.of("language", "code", "focus"))
            .build();
        
        // 保存模板
        templateManager.saveTemplate(codeReviewTemplate);
        
        // 使用模板
        Map<String, Object> params = new HashMap<>();
        params.put("language", "java");
        params.put("code", "public class UserService {\n"
                + "    public User getUser(Long id) {\n"
                + "        if (id == null) {\n"
                + "            return null;\n"
                + "        }\n"
                + "        return userRepository.findById(id);\n"
                + "    }\n"
                + "}");
        params.put("focus", "异常处理和边界条件");
        
        // 渲染模板
        String renderedPrompt = templateManager.renderTemplate("code-review", params);
        System.out.println("渲染后的提示词：");
        System.out.println(renderedPrompt);
    }
    
    /**
     * 示例2：创建和使用SQL生成模板
     */
    public void sqlGenerationExample() {
        // 创建SQL生成模板
        PromptTemplate sqlTemplate = PromptTemplate.builder()
            .id("sql-generation")
            .name("SQL语句生成")
            .content("你是一个SQL专家。请根据以下需求生成${database}数据库的SQL语句：\n\n"
                    + "需求描述：${requirement}\n\n"
                    + "表结构信息：\n${tableStructure}\n\n"
                    + "生成要求：\n"
                    + "1. 确保SQL语法正确\n"
                    + "2. 考虑性能和安全性\n"
                    + "3. 添加适当的注释\n"
                    + "4. ${additionalRequirements}\n\n"
                    + "请只返回SQL语句，不要包含其他解释。")
            .category("数据库")
            .type(TemplateType.USER)
            .description("用于生成SQL语句的模板")
            .modelType("gpt-3.5-turbo")
            .tags(List.of("SQL", "数据库", "代码生成"))
            .parameters(List.of("database", "requirement", "tableStructure", "additionalRequirements"))
            .build();
        
        templateManager.saveTemplate(sqlTemplate);
        
        // 使用模板生成SQL
        Map<String, Object> sqlParams = new HashMap<>();
        sqlParams.put("database", "MySQL");
        sqlParams.put("requirement", "查询过去30天内注册用户的活跃度和订单统计");
        sqlParams.put("tableStructure", "users(id, username, email, created_at, last_login_at)\n"
                + "orders(id, user_id, total_amount, status, created_at)");
        sqlParams.put("additionalRequirements", "使用JOIN连接，按活跃度降序排列");
        
        String sqlPrompt = templateManager.renderTemplate("sql-generation", sqlParams);
        System.out.println("\nSQL生成提示词：");
        System.out.println(sqlPrompt);
    }
    
    /**
     * 示例3：同步执行提示词
     */
    public void syncExecutionExample() {
        // 创建执行请求
        Map<String, Object> params = new HashMap<>();
        params.put("language", "python");
        params.put("code", "def fibonacci(n):\n"
                + "    if n <= 1:\n"
                + "        return n\n"
                + "    return fibonacci(n-1) + fibonacci(n-2)");
        params.put("focus", "时间复杂度和递归优化");
        
        PromptExecutionRequest request = PromptExecutionRequest.builder()
            .templateId("code-review")
            .parameters(params)
            .modelType("gpt-4")
            .temperature(0.3)
            .maxTokens(1500)
            .userId("developer123")
            .sessionId("session456")
            .build();
        
        // 同步执行
        PromptExecutionResponse response = executor.execute(request);
        
        if (response.isSuccess()) {
            System.out.println("\n执行成功！");
            System.out.println("响应内容：" + response.getModelResponse());
            System.out.println("使用的令牌数：" + response.getTokenUsage().getTotalTokens());
            System.out.println("执行时间：" + response.getExecutionTime() + "ms");
        } else {
            System.out.println("执行失败：" + response.getErrorMessage());
        }
    }
    
    /**
     * 示例4：异步执行提示词
     */
    public void asyncExecutionExample() {
        // 创建批量执行请求
        List<PromptExecutionRequest> requests = List.of(
            createTranslationRequest("Hello, how are you?", "english", "chinese"),
            createTranslationRequest("Bonjour, comment allez-vous?", "french", "english"),
            createTranslationRequest("Hola, ¿cómo estás?", "spanish", "chinese")
        );
        
        // 异步批量执行
        List<CompletableFuture<PromptExecutionResponse>> futures = requests.stream()
            .map(executor::executeAsync)
            .toList();
        
        // 等待所有任务完成
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
            .thenRun(() -> {
                System.out.println("\n异步批量执行完成！");
                futures.forEach(future -> {
                    try {
                        PromptExecutionResponse response = future.get();
                        if (response.isSuccess()) {
                            System.out.println("翻译结果: " + response.getModelResponse());
                        }
                    } catch (Exception e) {
                        System.err.println("执行异常: " + e.getMessage());
                    }
                });
            })
            .join();
    }
    
    /**
     * 示例5：模板搜索和统计
     */
    public void searchAndStatisticsExample() {
        // 搜索模板
        List<PromptTemplate> developmentTemplates = templateManager.getTemplatesByCategory("开发");
        System.out.println("\n开发类模板数量: " + developmentTemplates.size());
        
        List<PromptTemplate> userTemplates = templateManager.getTemplatesByType(TemplateType.USER);
        System.out.println("用户模板数量: " + userTemplates.size());
        
        // 获取统计信息
        var statistics = templateManager.getTemplateStatistics();
        System.out.println("模板统计信息:");
        System.out.println("总模板数: " + statistics.getTotalTemplates());
        System.out.println("活跃模板数: " + statistics.getActiveTemplates());
        System.out.println("平均评分: " + statistics.getAverageRating());
        System.out.println("使用次数最多的模板: " + statistics.getMostUsedTemplate());
        
        // 获取热门模板
        List<PromptTemplate> popularTemplates = templateManager.getPopularTemplates(5);
        System.out.println("\n热门模板:");
        popularTemplates.forEach(template -> 
            System.out.println("- " + template.getName() + " (使用次数: " + template.getUsageCount() + ")")
        );
    }
    
    /**
     * 示例6：模板克隆和修改
     */
    public void cloneAndModifyExample() {
        // 克隆现有模板
        PromptTemplate originalTemplate = templateManager.getTemplate("code-review");
        if (originalTemplate != null) {
            PromptTemplate clonedTemplate = originalTemplate.clone();
            clonedTemplate.setId("code-review-enhanced");
            clonedTemplate.setName("增强版代码审查");
            clonedTemplate.setContent(clonedTemplate.getContent() + 
                "\n\n额外要求：\n"
                + "5. 检查代码是否符合SOLID原则\n"
                + "6. 评估代码的可测试性\n"
                + "7. 提供重构建议");
            
            templateManager.saveTemplate(clonedTemplate);
            System.out.println("\n成功克隆并修改模板");
        }
    }
    
    /**
     * 示例7：执行统计和监控
     */
    public void executionStatisticsExample() {
        // 获取执行统计
        var executionStats = executor.getExecutionStatistics();
        System.out.println("\n执行统计:");
        System.out.println("总执行次数: " + executionStats.getTotalExecutions());
        System.out.println("成功次数: " + executionStats.getSuccessfulExecutions());
        System.out.println("失败次数: " + executionStats.getFailedExecutions());
        System.out.println("成功率: " + String.format("%.2f%%", executionStats.getSuccessRate() * 100));
        System.out.println("平均响应时间: " + executionStats.getAverageResponseTime() + "ms");
        System.out.println("缓存命中率: " + String.format("%.2f%%", executionStats.getCacheHitRate() * 100));
        
        // 获取正在执行的任务
        var runningExecutions = executor.getRunningExecutions();
        System.out.println("\n正在执行的任务数: " + runningExecutions.size());
    }
    
    // 辅助方法
    private PromptExecutionRequest createTranslationRequest(String text, String fromLang, String toLang) {
        Map<String, Object> params = new HashMap<>();
        params.put("text", text);
        params.put("fromLang", fromLang);
        params.put("toLang", toLang);
        
        return PromptExecutionRequest.builder()
            .templateId("translation")
            .parameters(params)
            .modelType("gpt-3.5-turbo")
            .temperature(0.1)
            .maxTokens(500)
            .build();
    }
    
    /**
     * 运行所有示例
     */
    public void runAllExamples() {
        System.out.println("=== 开始运行提示词模板示例 ===\n");
        
        codeReviewExample();
        sqlGenerationExample();
        syncExecutionExample();
        asyncExecutionExample();
        searchAndStatisticsExample();
        cloneAndModifyExample();
        executionStatisticsExample();
        
        System.out.println("\n=== 所有示例运行完成 ===");
    }
}