package com.fw.know.go.ai.example;

import com.fw.know.go.ai.prompt.PromptTemplate;
import com.fw.know.go.ai.prompt.PromptTemplateManager;
import com.fw.know.go.ai.prompt.PromptTemplateExecutor;
import com.fw.know.go.ai.prompt.PromptExecutionRequest;
import com.fw.know.go.ai.prompt.PromptExecutionResponse;
import com.fw.know.go.ai.prompt.PromptTemplate.TemplateType;
import com.fw.know.go.ai.multimodal.MultimodalContent;
import com.fw.know.go.ai.multimodal.MultimodalContentManager;

import java.util.*;

/**
 * 快速开始示例 - 展示最常用的功能
 */
public class QuickStartExample {
    
    private final PromptTemplateManager templateManager;
    private final PromptTemplateExecutor executor;
    private final MultimodalContentManager contentManager;
    
    public QuickStartExample(PromptTemplateManager templateManager, 
                              PromptTemplateExecutor executor,
                              MultimodalContentManager contentManager) {
        this.templateManager = templateManager;
        this.executor = executor;
        this.contentManager = contentManager;
    }
    
    /**
     * 快速开始1：创建和使用提示词模板
     */
    public void quickStartTemplate() {
        System.out.println("🚀 快速开始1：提示词模板");
        
        // 1. 创建模板
        PromptTemplate template = PromptTemplate.builder()
            .id("translation")
            .name("翻译助手")
            .content("请将以下${fromLang}文本翻译成${toLang}：\n\n${text}\n\n"
                    + "翻译要求：\n"
                    + "1. 保持原意准确\n"
                    + "2. 语言自然流畅\n"
                    + "3. 符合目标语言习惯\n\n"
                    + "翻译结果：")
            .category("翻译")
            .type(TemplateType.USER)
            .description("专业的翻译助手模板")
            .modelType("gpt-3.5-turbo")
            .parameters(Arrays.asList("fromLang", "toLang", "text"))
            .build();
        
        // 保存模板
        templateManager.saveTemplate(template);
        System.out.println("✅ 模板创建成功！");
        
        // 2. 使用模板
        Map<String, Object> params = new HashMap<>();
        params.put("fromLang", "中文");
        params.put("toLang", "英文");
        params.put("text", "人工智能正在改变世界");
        
        String renderedPrompt = templateManager.renderTemplate("translation", params);
        System.out.println("📝 渲染后的提示词：");
        System.out.println(renderedPrompt);
        System.out.println();
    }
    
    /**
     * 快速开始2：执行AI请求
     */
    public void quickStartExecution() {
        System.out.println("🚀 快速开始2：AI请求执行");
        
        // 1. 创建执行请求
        Map<String, Object> params = new HashMap<>();
        params.put("fromLang", "英文");
        params.put("toLang", "中文");
        params.put("text", "Hello, how are you today?");
        
        PromptExecutionRequest request = PromptExecutionRequest.builder()
            .templateId("translation")
            .parameters(params)
            .modelType("gpt-3.5-turbo")
            .temperature(0.1)  // 低温度保证一致性
            .maxTokens(200)
            .userId("user123")
            .build();
        
        // 2. 执行请求
        PromptExecutionResponse response = executor.execute(request);
        
        // 3. 处理结果
        if (response.isSuccess()) {
            System.out.println("✅ AI请求执行成功！");
            System.out.println("🤖 AI响应：" + response.getModelResponse());
            System.out.println("⏱️  执行时间：" + response.getExecutionTime() + "ms");
            System.out.println("🔢 使用令牌数：" + response.getTokenUsage().getTotalTokens());
        } else {
            System.out.println("❌ 执行失败：" + response.getErrorMessage());
        }
        System.out.println();
    }
    
    /**
     * 快速开始3：多模态内容处理
     */
    public void quickStartMultimodal() {
        System.out.println("🚀 快速开始3：多模态内容处理");
        
        // 1. 处理文本内容
        String textContent = "这是一段关于机器学习的介绍文本。"
                + "机器学习是人工智能的一个重要分支，它使计算机能够从数据中学习。";
        
        MultimodalContent textResult = contentManager.processContent(textContent, "text/plain");
        System.out.println("✅ 文本处理完成！");
        System.out.println("📄 内容类型：" + textResult.getContentType());
        System.out.println("📊 内容大小：" + textResult.getSize() + " 字节");
        System.out.println("ℹ️  元数据：" + textResult.getMetadata());
        
        // 2. 处理JSON内容
        String jsonContent = "{\n"
                + "  \"title\": \"AI技术报告\",\n"
                + "  \"author\": \"KnowGo AI\",\n"
                + "  \"date\": \"2024-01-01\",\n"
                + "  \"summary\": \"人工智能技术发展趋势分析\"\n"
                + "}";
        
        MultimodalContent jsonResult = contentManager.processContent(jsonContent, "application/json");
        System.out.println("✅ JSON处理完成！");
        System.out.println("📄 内容类型：" + jsonResult.getContentType());
        System.out.println("📊 内容大小：" + jsonResult.getSize() + " 字节");
        
        // 3. 格式转换示例
        String markdownContent = "# AI技术概述\n\n"
                + "## 机器学习\n"
                + "机器学习是AI的核心技术。\n\n"
                + "## 深度学习\n"
                + "深度学习在图像识别等领域表现出色。";
        
        MultimodalContent markdownResult = contentManager.processContent(markdownContent, "text/markdown");
        MultimodalContent htmlResult = contentManager.convertContent(markdownResult, "text/html");
        
        System.out.println("✅ 格式转换完成！");
        System.out.println("🔄 Markdown -> HTML");
        System.out.println("📄 原始格式：" + markdownResult.getContentFormat());
        System.out.println("📄 目标格式：" + htmlResult.getContentFormat());
        System.out.println("📊 转换后大小：" + htmlResult.getSize() + " 字节");
        System.out.println();
    }
    
    /**
     * 快速开始4：批量处理
     */
    public void quickStartBatchProcessing() {
        System.out.println("🚀 快速开始4：批量处理");
        
        // 1. 批量创建模板
        List<PromptTemplate> templates = Arrays.asList(
            PromptTemplate.builder()
                .id("summarization")
                .name("文本摘要")
                .content("请将以下文本总结成${maxWords}个词以内：\n\n${text}\n\n摘要：")
                .category("文本处理")
                .type(TemplateType.USER)
                .parameters(Arrays.asList("text", "maxWords"))
                .build(),
            
            PromptTemplate.builder()
                .id("qa")
                .name("问答助手")
                .content("问题：${question}\n\n请提供详细准确的答案。")
                .category("问答")
                .type(TemplateType.USER)
                .parameters(Arrays.asList("question"))
                .build()
        );
        
        templates.forEach(templateManager::saveTemplate);
        System.out.println("✅ 批量创建 " + templates.size() + " 个模板");
        
        // 2. 批量执行请求
        List<PromptExecutionRequest> requests = Arrays.asList(
            PromptExecutionRequest.builder()
                .templateId("summarization")
                .parameters(Map.of(
                    "text", "人工智能是计算机科学的一个分支，它企图了解智能的实质，并生产出一种新的能以人类智能相似的方式做出反应的智能机器。",
                    "maxWords", "20"
                ))
                .modelType("gpt-3.5-turbo")
                .build(),
            
            PromptExecutionRequest.builder()
                .templateId("qa")
                .parameters(Map.of(
                    "question", "什么是深度学习？"
                ))
                .modelType("gpt-3.5-turbo")
                .build()
        );
        
        System.out.println("🔄 开始批量执行 " + requests.size() + " 个请求...");
        
        requests.forEach(request -> {
            PromptExecutionResponse response = executor.execute(request);
            if (response.isSuccess()) {
                System.out.println("✅ " + request.getTemplateId() + " 执行成功");
                System.out.println("🤖 结果: " + response.getModelResponse());
            } else {
                System.out.println("❌ " + request.getTemplateId() + " 执行失败");
            }
        });
        System.out.println();
    }
    
    /**
     * 快速开始5：模板搜索和管理
     */
    public void quickStartTemplateManagement() {
        System.out.println("🚀 快速开始5：模板管理");
        
        // 1. 搜索模板
        List<PromptTemplate> userTemplates = templateManager.getTemplatesByType(TemplateType.USER);
        System.out.println("📋 找到 " + userTemplates.size() + " 个用户模板");
        
        List<PromptTemplate> translationTemplates = templateManager.getTemplatesByCategory("翻译");
        System.out.println("📋 找到 " + translationTemplates.size() + " 个翻译模板");
        
        // 2. 获取模板统计
        var stats = templateManager.getTemplateStatistics();
        System.out.println("📊 模板统计:");
        System.out.println("   总模板数: " + stats.getTotalTemplates());
        System.out.println("   活跃模板数: " + stats.getActiveTemplates());
        System.out.println("   平均评分: " + String.format("%.2f", stats.getAverageRating()));
        
        // 3. 获取热门模板
        List<PromptTemplate> popularTemplates = templateManager.getPopularTemplates(3);
        System.out.println("🔥 热门模板:");
        popularTemplates.forEach(template -> 
            System.out.println("   - " + template.getName() + " (使用 " + template.getUsageCount() + " 次)")
        );
        System.out.println();
    }
    
    /**
     * 快速开始6：内容验证
     */
    public void quickStartValidation() {
        System.out.println("🚀 快速开始6：内容验证");
        
        // 1. 验证不同类型的内容
        String[] testContents = {
            "这是有效的文本内容",
            "data:image/jpeg;base64,/9j/4AAQSkZJRgABAQEAYABgAAD",
            "{\"valid\": \"json\"}",
            "<valid>XML</valid>",
            "invalid content type"
        };
        
        String[] mimeTypes = {
            "text/plain",
            "image/jpeg",
            "application/json",
            "application/xml",
            "application/unsupported"
        };
        
        System.out.println("🔍 内容验证结果:");
        for (int i = 0; i < testContents.length; i++) {
            boolean isValid = contentManager.validateContent(testContents[i], mimeTypes[i]);
            System.out.println("   " + mimeTypes[i] + ": " + (isValid ? "✅ 有效" : "❌ 无效"));
        }
        
        // 2. 获取支持的类型
        List<String> supportedTypes = contentManager.getSupportedTypes();
        System.out.println("📋 支持的内容类型 (" + supportedTypes.size() + " 种):");
        supportedTypes.stream()
            .limit(10)
            .forEach(type -> System.out.println("   - " + type));
        if (supportedTypes.size() > 10) {
            System.out.println("   ... 还有 " + (supportedTypes.size() - 10) + " 种类型");
        }
        System.out.println();
    }
    
    /**
     * 完整的快速开始演示
     */
    public void runCompleteQuickStart() {
        System.out.println("🎯 KnowGo AI 快速开始完整演示");
        System.out.println("=====================================\n");
        
        try {
            quickStartTemplate();
            quickStartExecution();
            quickStartMultimodal();
            quickStartBatchProcessing();
            quickStartTemplateManagement();
            quickStartValidation();
            
            System.out.println("🎉 快速开始演示完成！");
            System.out.println("💡 您现在可以开始使用 KnowGo AI 构建您的AI应用了！");
            
        } catch (Exception e) {
            System.err.println("❌ 演示过程中出现错误: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * 简洁的快速开始 - 最常用的功能
     */
    public void runSimpleQuickStart() {
        System.out.println("🚀 KnowGo AI 简洁快速开始");
        System.out.println("============================\n");
        
        try {
            // 1. 创建模板
            PromptTemplate template = PromptTemplate.builder()
                .id("simple-translation")
                .name("简单翻译")
                .content("将以下文本从${fromLang}翻译成${toLang}：${text}")
                .parameters(Arrays.asList("fromLang", "toLang", "text"))
                .build();
            
            templateManager.saveTemplate(template);
            System.out.println("✅ 模板创建成功");
            
            // 2. 执行翻译
            Map<String, Object> params = Map.of(
                "fromLang", "中文",
                "toLang", "英文", 
                "text", "你好，世界！"
            );
            
            PromptExecutionRequest request = PromptExecutionRequest.builder()
                .templateId("simple-translation")
                .parameters(params)
                .modelType("gpt-3.5-turbo")
                .build();
            
            PromptExecutionResponse response = executor.execute(request);
            System.out.println("✅ 翻译结果: " + response.getModelResponse());
            
            // 3. 处理文本内容
            MultimodalContent content = contentManager.processContent("Hello World", "text/plain");
            System.out.println("✅ 内容处理完成，大小: " + content.getSize() + " 字节");
            
            System.out.println("\n🎉 简洁快速开始完成！");
            
        } catch (Exception e) {
            System.err.println("❌ 错误: " + e.getMessage());
        }
    }
}