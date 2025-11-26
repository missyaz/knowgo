package com.fw.know.go.ai.example;

import com.fw.know.go.ai.prompt.*;
import com.fw.know.go.ai.prompt.impl.PromptTemplateExecutorImpl;
import com.fw.know.go.ai.multimodal.*;
import com.fw.know.go.ai.multimodal.impl.MultimodalContentManagerImpl;
import com.fw.know.go.ai.multimodal.impl.TextContentProcessor;
import com.fw.know.go.ai.multimodal.impl.ImageContentProcessor;
import com.fw.know.go.ai.multimodal.impl.JsonContentProcessor;

import java.util.*;
import java.util.concurrent.*;

/**
 * 完整的应用程序演示 - 展示如何在实际项目中使用KnowGo AI
 */
public class ApplicationDemo {
    
    private final PromptTemplateManager templateManager;
    private final PromptTemplateExecutor executor;
    private final MultimodalContentManager contentManager;
    private final ExecutorService executorService;
    
    public ApplicationDemo() {
        // 初始化组件
        this.templateManager = new InMemoryPromptTemplateManager();
        this.executor = new PromptTemplateExecutorImpl(templateManager);
        this.contentManager = createMultimodalContentManager();
        this.executorService = Executors.newFixedThreadPool(10);
        
        // 初始化模板
        initializeTemplates();
    }
    
    /**
     * 创建多模态内容管理器
     */
    private MultimodalContentManager createMultimodalContentManager() {
        Map<String, ContentProcessor> processors = new HashMap<>();
        processors.put("text/plain", new TextContentProcessor());
        processors.put("text/markdown", new TextContentProcessor());
        processors.put("image/jpeg", new ImageContentProcessor());
        processors.put("image/png", new ImageContentProcessor());
        processors.put("application/json", new JsonContentProcessor());
        
        return new MultimodalContentManagerImpl(processors);
    }
    
    /**
     * 初始化业务模板
     */
    private void initializeTemplates() {
        // 客服助手模板
        PromptTemplate customerService = PromptTemplate.builder()
            .id("customer-service")
            .name("智能客服助手")
            .content("你是一个专业的客服助手。请根据以下信息帮助客户：\n\n"
                    + "客户问题：${question}\n"
                    + "产品信息：${productInfo}\n"
                    + "历史对话：${history}\n\n"
                    + "请提供：\n"
                    + "1. 直接回答客户问题\n"
                    + "2. 如果需要更多信息，礼貌询问\n"
                    + "3. 提供相关建议\n\n"
                    + "回答：")
            .category("客服")
            .type(PromptTemplate.TemplateType.USER)
            .description("智能客服助手模板")
            .modelType("gpt-3.5-turbo")
            .parameters(Arrays.asList("question", "productInfo", "history"))
            .temperature(0.3)
            .maxTokens(500)
            .build();
        
        // 代码审查模板
        PromptTemplate codeReview = PromptTemplate.builder()
            .id("code-review")
            .name("代码审查助手")
            .content("请作为资深开发工程师，审查以下代码：\n\n"
                    + "编程语言：${language}\n"
                    + "代码内容：\n${code}\n\n"
                    + "请提供：\n"
                    + "1. 代码质量评估\n"
                    + "2. 潜在问题和风险\n"
                    + "3. 性能优化建议\n"
                    + "4. 最佳实践建议\n\n"
                    + "审查结果：")
            .category("开发")
            .type(PromptTemplate.TemplateType.USER)
            .description("代码审查助手模板")
            .modelType("gpt-4")
            .parameters(Arrays.asList("language", "code"))
            .temperature(0.2)
            .maxTokens(800)
            .build();
        
        // 数据分析模板
        PromptTemplate dataAnalysis = PromptTemplate.builder()
            .id("data-analysis")
            .name("数据分析助手")
            .content("请分析以下数据并提供见解：\n\n"
                    + "数据描述：${dataDescription}\n"
                    + "数据内容：\n${data}\n\n"
                    + "分析要求：\n"
                    + "1. 识别关键趋势和模式\n"
                    + "2. 提供数据洞察\n"
                    + "3. 建议后续行动\n"
                    + "4. 指出潜在问题\n\n"
                    + "分析结果：")
            .category("分析")
            .type(PromptTemplate.TemplateType.USER)
            .description("数据分析助手模板")
            .modelType("gpt-4")
            .parameters(Arrays.asList("dataDescription", "data"))
            .temperature(0.4)
            .maxTokens(600)
            .build();
        
        // 内容生成模板
        PromptTemplate contentGeneration = PromptTemplate.builder()
            .id("content-generation")
            .name("内容生成助手")
            .content("请根据以下要求生成${contentType}内容：\n\n"
                    + "主题：${topic}\n"
                    + "目标受众：${audience}\n"
                    + "风格要求：${style}\n"
                    + "长度要求：${length}\n"
                    + "关键词：${keywords}\n\n"
                    + "请确保内容：\n"
                    + "1. 符合主题要求\n"
                    + "2. 适合目标受众\n"
                    + "3. 包含指定关键词\n"
                    + "4. 风格一致\n\n"
                    + "生成内容：")
            .category("内容")
            .type(PromptTemplate.TemplateType.USER)
            .description("内容生成助手模板")
            .modelType("gpt-3.5-turbo")
            .parameters(Arrays.asList("contentType", "topic", "audience", "style", "length", "keywords"))
            .temperature(0.7)
            .maxTokens(1000)
            .build();
        
        // 保存所有模板
        Arrays.asList(customerService, codeReview, dataAnalysis, contentGeneration)
            .forEach(templateManager::saveTemplate);
    }
    
    /**
     * 演示：智能客服系统
     */
    public void demoCustomerService() {
        System.out.println("🤖 演示：智能客服系统");
        System.out.println("========================\n");
        
        String[] customerQuestions = {
            "我的订单什么时候能到？订单号是12345",
            "这个产品的保修期是多久？",
            "我想退货，需要什么流程？",
            "你们支持哪些支付方式？"
        };
        
        String productInfo = "产品名称：智能手表\n"
                           + "保修期：1年\n"
                           + "退货政策：7天无理由退货\n"
                           + "配送时间：3-5个工作日\n"
                           + "支付方式：支付宝、微信、银行卡";
        
        for (String question : customerQuestions) {
            System.out.println("👤 客户问题：" + question);
            
            Map<String, Object> params = new HashMap<>();
            params.put("question", question);
            params.put("productInfo", productInfo);
            params.put("history", "无历史对话");
            
            PromptExecutionRequest request = PromptExecutionRequest.builder()
                .templateId("customer-service")
                .parameters(params)
                .userId("customer-001")
                .build();
            
            PromptExecutionResponse response = executor.execute(request);
            
            if (response.isSuccess()) {
                System.out.println("🤖 AI回复：" + response.getModelResponse());
                System.out.println("⏱️  处理时间：" + response.getExecutionTime() + "ms");
            } else {
                System.out.println("❌ 处理失败：" + response.getErrorMessage());
            }
            System.out.println("---");
        }
        System.out.println();
    }
    
    /**
     * 演示：代码审查系统
     */
    public void demoCodeReview() {
        System.out.println("💻 演示：代码审查系统");
        System.out.println("========================\n");
        
        String[] codeSamples = {
            "public class UserService {\n"
            + "    public User getUserById(Long id) {\n"
            + "        return userRepository.findById(id);\n"
            + "    }\n"
            + "}",
            
            "def calculate_discount(price, discount):\n"
            + "    return price * discount / 100",
            
            "const fetchData = async () => {\n"
            + "    const response = await fetch('/api/data');\n"
            + "    return response.json();\n"
            + "}"
        };
        
        String[] languages = {"Java", "Python", "JavaScript"};
        
        for (int i = 0; i < codeSamples.length; i++) {
            System.out.println("📄 代码片段 (" + languages[i] + ")：");
            System.out.println(codeSamples[i]);
            
            Map<String, Object> params = new HashMap<>();
            params.put("language", languages[i]);
            params.put("code", codeSamples[i]);
            
            PromptExecutionRequest request = PromptExecutionRequest.builder()
                .templateId("code-review")
                .parameters(params)
                .userId("developer-001")
                .build();
            
            PromptExecutionResponse response = executor.execute(request);
            
            if (response.isSuccess()) {
                System.out.println("\n🔍 审查结果：");
                System.out.println(response.getModelResponse());
            } else {
                System.out.println("❌ 审查失败：" + response.getErrorMessage());
            }
            System.out.println("\n" + "=".repeat(50) + "\n");
        }
    }
    
    /**
     * 演示：内容生成系统
     */
    public void demoContentGeneration() {
        System.out.println("✍️ 演示：内容生成系统");
        System.out.println("========================\n");
        
        String[] topics = {
            "人工智能在医疗领域的应用",
            "远程办公的最佳实践",
            "可持续发展的未来趋势"
        };
        
        String[] audiences = {"医疗从业者", "企业管理者", "环保倡导者"};
        String[] styles = {"专业学术", "商务正式", "轻松易懂"};
        
        for (int i = 0; i < topics.length; i++) {
            System.out.println("📝 生成内容：" + topics[i]);
            
            Map<String, Object> params = new HashMap<>();
            params.put("contentType", "文章");
            params.put("topic", topics[i]);
            params.put("audience", audiences[i]);
            params.put("style", styles[i]);
            params.put("length", "500字");
            params.put("keywords", "创新, 发展, 未来");
            
            PromptExecutionRequest request = PromptExecutionRequest.builder()
                .templateId("content-generation")
                .parameters(params)
                .userId("content-creator-001")
                .build();
            
            PromptExecutionResponse response = executor.execute(request);
            
            if (response.isSuccess()) {
                System.out.println("🎯 生成内容：");
                System.out.println(response.getModelResponse());
                System.out.println("📊 令牌使用：" + response.getTokenUsage().getTotalTokens());
            } else {
                System.out.println("❌ 生成失败：" + response.getErrorMessage());
            }
            System.out.println("\n" + "=".repeat(50) + "\n");
        }
    }
    
    /**
     * 演示：批量处理系统
     */
    public void demoBatchProcessing() {
        System.out.println("⚡ 演示：批量处理系统");
        System.out.println("========================\n");
        
        // 准备批量请求
        List<PromptExecutionRequest> batchRequests = new ArrayList<>();
        
        // 批量客服请求
        String[] questions = {
            "产品如何使用？",
            "价格是多少？",
            "有优惠活动吗？",
            "支持哪些地区配送？"
        };
        
        for (String question : questions) {
            Map<String, Object> params = new HashMap<>();
            params.put("question", question);
            params.put("productInfo", "智能手表 - 价格¥1999，支持全国配送");
            params.put("history", "");
            
            batchRequests.add(PromptExecutionRequest.builder()
                .templateId("customer-service")
                .parameters(params)
                .userId("batch-user")
                .build());
        }
        
        System.out.println("🔄 开始批量处理 " + batchRequests.size() + " 个请求...");
        
        long startTime = System.currentTimeMillis();
        List<CompletableFuture<PromptExecutionResponse>> futures = new ArrayList<>();
        
        // 异步执行所有请求
        for (PromptExecutionRequest request : batchRequests) {
            CompletableFuture<PromptExecutionResponse> future = 
                CompletableFuture.supplyAsync(() -> executor.execute(request), executorService);
            futures.add(future);
        }
        
        // 等待所有请求完成
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        
        long endTime = System.currentTimeMillis();
        
        // 统计结果
        int successCount = 0;
        int failureCount = 0;
        long totalTokens = 0;
        
        for (int i = 0; i < futures.size(); i++) {
            try {
                PromptExecutionResponse response = futures.get(i).get();
                if (response.isSuccess()) {
                    successCount++;
                    totalTokens += response.getTokenUsage().getTotalTokens();
                    System.out.println("✅ 请求 " + (i + 1) + " 成功：" + response.getModelResponse().substring(0, Math.min(50, response.getModelResponse().length())) + "...");
                } else {
                    failureCount++;
                    System.out.println("❌ 请求 " + (i + 1) + " 失败：" + response.getErrorMessage());
                }
            } catch (Exception e) {
                failureCount++;
                System.out.println("❌ 请求 " + (i + 1) + " 异常：" + e.getMessage());
            }
        }
        
        System.out.println("\n📊 批量处理统计：");
        System.out.println("   总请求数：" + batchRequests.size());
        System.out.println("   成功数：" + successCount);
        System.out.println("   失败数：" + failureCount);
        System.out.println("   总耗时：" + (endTime - startTime) + "ms");
        System.out.println("   总令牌数：" + totalTokens);
        System.out.println("   平均响应时间：" + ((endTime - startTime) / batchRequests.size()) + "ms");
        System.out.println();
    }
    
    /**
     * 演示：性能监控和统计
     */
    public void demoPerformanceMonitoring() {
        System.out.println("📈 演示：性能监控和统计");
        System.out.println("========================\n");
        
        // 执行一些测试请求来生成统计数据
        System.out.println("🔄 执行测试请求生成统计数据...");
        
        for (int i = 0; i < 10; i++) {
            Map<String, Object> params = new HashMap<>();
            params.put("question", "测试问题 " + (i + 1));
            params.put("productInfo", "测试产品信息");
            params.put("history", "");
            
            PromptExecutionRequest request = PromptExecutionRequest.builder()
                .templateId("customer-service")
                .parameters(params)
                .userId("test-user-" + i)
                .build();
            
            executor.execute(request);
        }
        
        // 获取执行统计
        PromptTemplateExecutor.ExecutionStatistics stats = executor.getExecutionStatistics();
        
        System.out.println("📊 执行统计：");
        System.out.println("   总执行数：" + stats.getTotalExecutions());
        System.out.println("   成功数：" + stats.getSuccessfulExecutions());
        System.out.println("   失败数：" + stats.getFailedExecutions());
        System.out.println("   成功率：" + String.format("%.2f%%", stats.getSuccessRate() * 100));
        System.out.println("   平均执行时间：" + stats.getAverageExecutionTime() + "ms");
        System.out.println("   缓存命中数：" + stats.getCacheHits());
        System.out.println("   缓存命中率：" + String.format("%.2f%%", stats.getCacheHitRate() * 100));
        
        // 获取模板统计
        var templateStats = templateManager.getTemplateStatistics();
        System.out.println("\n📊 模板统计：");
        System.out.println("   总模板数：" + templateStats.getTotalTemplates());
        System.out.println("   活跃模板数：" + templateStats.getActiveTemplates());
        System.out.println("   平均评分：" + String.format("%.2f", templateStats.getAverageRating()));
        
        // 获取热门模板
        List<PromptTemplate> popularTemplates = templateManager.getPopularTemplates(5);
        System.out.println("\n🔥 热门模板：");
        popularTemplates.forEach(template -> {
            System.out.println("   - " + template.getName() + 
                             " (使用 " + template.getUsageCount() + " 次, " +
                             "评分 " + String.format("%.1f", template.getRating()) + ")");
        });
        
        System.out.println();
    }
    
    /**
     * 运行完整的应用程序演示
     */
    public void runCompleteDemo() {
        System.out.println("🎯 KnowGo AI 完整应用程序演示");
        System.out.println("=====================================\n");
        
        try {
            demoCustomerService();
            demoCodeReview();
            demoContentGeneration();
            demoBatchProcessing();
            demoPerformanceMonitoring();
            
            System.out.println("🎉 完整演示完成！");
            System.out.println("💡 您已经看到了 KnowGo AI 在实际应用中的强大功能！");
            
        } catch (Exception e) {
            System.err.println("❌ 演示过程中出现错误: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // 清理资源
            executorService.shutdown();
        }
    }
    
    /**
     * 简化的应用程序演示
     */
    public void runSimpleDemo() {
        System.out.println("🚀 KnowGo AI 简化应用程序演示");
        System.out.println("=====================================\n");
        
        try {
            // 简单的客服对话
            System.out.println("🤖 智能客服演示：");
            
            Map<String, Object> params = new HashMap<>();
            params.put("question", "这个产品怎么用？");
            params.put("productInfo", "智能手表 - 支持心率监测、运动追踪");
            params.put("history", "");
            
            PromptExecutionRequest request = PromptExecutionRequest.builder()
                .templateId("customer-service")
                .parameters(params)
                .build();
            
            PromptExecutionResponse response = executor.execute(request);
            
            if (response.isSuccess()) {
                System.out.println("👤 用户：这个产品怎么用？");
                System.out.println("🤖 AI客服：" + response.getModelResponse());
                System.out.println("⏱️  响应时间：" + response.getExecutionTime() + "ms");
            }
            
            System.out.println("\n🎉 简化演示完成！");
            
        } catch (Exception e) {
            System.err.println("❌ 演示失败: " + e.getMessage());
        } finally {
            executorService.shutdown();
        }
    }
    
    /**
     * 简单的内存模板管理器实现（用于演示）
     */
    private static class InMemoryPromptTemplateManager implements PromptTemplateManager {
        private final Map<String, PromptTemplate> templates = new ConcurrentHashMap<>();
        private final Map<String, Integer> usageCount = new ConcurrentHashMap<>();
        
        @Override
        public void saveTemplate(PromptTemplate template) {
            templates.put(template.getId(), template);
            usageCount.putIfAbsent(template.getId(), 0);
        }
        
        @Override
        public PromptTemplate getTemplate(String id) {
            usageCount.merge(id, 1, Integer::sum);
            return templates.get(id);
        }
        
        @Override
        public List<PromptTemplate> getAllTemplates() {
            return new ArrayList<>(templates.values());
        }
        
        @Override
        public void deleteTemplate(String id) {
            templates.remove(id);
            usageCount.remove(id);
        }
        
        @Override
        public List<PromptTemplate> getTemplatesByCategory(String category) {
            return templates.values().stream()
                .filter(t -> category.equals(t.getCategory()))
                .collect(ArrayList::new, (list, item) -> {
                    list.add(item);
                    usageCount.merge(item.getId(), 1, Integer::sum);
                }, ArrayList::addAll);
        }
        
        @Override
        public List<PromptTemplate> getTemplatesByType(PromptTemplate.TemplateType type) {
            return templates.values().stream()
                .filter(t -> type == t.getType())
                .collect(ArrayList::new, (list, item) -> {
                    list.add(item);
                    usageCount.merge(item.getId(), 1, Integer::sum);
                }, ArrayList::addAll);
        }
        
        @Override
        public List<PromptTemplate> searchTemplates(String keyword) {
            return templates.values().stream()
                .filter(t -> t.getName().contains(keyword) || t.getDescription().contains(keyword))
                .collect(ArrayList::new, (list, item) -> {
                    list.add(item);
                    usageCount.merge(item.getId(), 1, Integer::sum);
                }, ArrayList::addAll);
        }
        
        @Override
        public List<PromptTemplate> getPopularTemplates(int limit) {
            return templates.values().stream()
                .sorted((a, b) -> usageCount.getOrDefault(b.getId(), 0) - usageCount.getOrDefault(a.getId(), 0))
                .limit(limit)
                .collect(ArrayList::new, (list, item) -> {
                    list.add(item);
                    usageCount.merge(item.getId(), 1, Integer::sum);
                }, ArrayList::addAll);
        }
        
        @Override
        public TemplateStatistics getTemplateStatistics() {
            return new TemplateStatistics() {
                @Override public int getTotalTemplates() { return templates.size(); }
                @Override public int getActiveTemplates() { return (int) templates.values().stream().filter(t -> !t.isDisabled()).count(); }
                @Override public double getAverageRating() { return templates.values().stream().mapToDouble(PromptTemplate::getRating).average().orElse(0.0); }
            };
        }
        
        @Override
        public String renderTemplate(String templateId, Map<String, Object> parameters) {
            PromptTemplate template = getTemplate(templateId);
            if (template == null) {
                throw new RuntimeException("模板不存在：" + templateId);
            }
            
            String content = template.getContent();
            for (Map.Entry<String, Object> entry : parameters.entrySet()) {
                content = content.replace("${" + entry.getKey() + "}", String.valueOf(entry.getValue()));
            }
            return content;
        }
    }
}