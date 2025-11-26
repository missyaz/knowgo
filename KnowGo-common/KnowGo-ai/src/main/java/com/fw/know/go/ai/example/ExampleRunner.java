package com.fw.know.go.ai.example;

import com.fw.know.go.ai.prompt.PromptTemplateManager;
import com.fw.know.go.ai.prompt.PromptTemplateExecutor;
import com.fw.know.go.ai.multimodal.MultimodalContentManager;

import java.util.Scanner;

/**
 * 示例运行器 - 统一管理和运行所有示例
 */
public class ExampleRunner {
    
    private final PromptTemplateManager templateManager;
    private final PromptTemplateExecutor executor;
    private final MultimodalContentManager contentManager;
    private final Scanner scanner;
    
    public ExampleRunner(PromptTemplateManager templateManager, 
                         PromptTemplateExecutor executor,
                         MultimodalContentManager contentManager) {
        this.templateManager = templateManager;
        this.executor = executor;
        this.contentManager = contentManager;
        this.scanner = new Scanner(System.in);
    }
    
    /**
     * 显示主菜单
     */
    private void showMainMenu() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("🎯 KnowGo AI 示例程序菜单");
        System.out.println("=".repeat(60));
        System.out.println("1. 快速开始示例 (QuickStart)");
        System.out.println("2. 完整应用程序演示 (Application Demo)");
        System.out.println("3. 提示词模板管理示例");
        System.out.println("4. 多模态内容处理示例");
        System.out.println("5. AI中间件使用示例");
        System.out.println("6. 运行所有示例");
        System.out.println("0. 退出");
        System.out.println("=".repeat(60));
        System.out.print("请选择要运行的示例 (0-6): ");
    }
    
    /**
     * 显示快速开始子菜单
     */
    private void showQuickStartMenu() {
        System.out.println("\n" + "-".repeat(40));
        System.out.println("🚀 快速开始示例");
        System.out.println("-".repeat(40));
        System.out.println("1. 简洁快速开始 (推荐)");
        System.out.println("2. 完整快速开始演示");
        System.out.println("3. 单独功能演示");
        System.out.println("0. 返回主菜单");
        System.out.print("请选择 (0-3): ");
    }
    
    /**
     * 显示单独功能菜单
     */
    private void showFeatureMenu() {
        System.out.println("\n" + "-".repeat(40));
        System.out.println("🔧 单独功能演示");
        System.out.println("-".repeat(40));
        System.out.println("1. 模板创建和使用");
        System.out.println("2. AI请求执行");
        System.out.println("3. 多模态内容处理");
        System.out.println("4. 批量处理");
        System.out.println("5. 模板搜索和管理");
        System.out.println("6. 内容验证");
        System.out.println("0. 返回上级菜单");
        System.out.print("请选择 (0-6): ");
    }
    
    /**
     * 显示应用程序演示子菜单
     */
    private void showAppDemoMenu() {
        System.out.println("\n" + "-".repeat(40));
        System.out.println("💼 应用程序演示");
        System.out.println("-".repeat(40));
        System.out.println("1. 简化演示 (推荐)");
        System.out.println("2. 完整演示 (所有功能)");
        System.out.println("3. 智能客服演示");
        System.out.println("4. 代码审查演示");
        System.out.println("5. 内容生成演示");
        System.out.println("6. 批量处理演示");
        System.out.println("7. 性能监控演示");
        System.out.println("0. 返回主菜单");
        System.out.print("请选择 (0-7): ");
    }
    
    /**
     * 运行快速开始示例
     */
    private void runQuickStartExample() {
        QuickStartExample example = new QuickStartExample(templateManager, executor, contentManager);
        
        while (true) {
            showQuickStartMenu();
            String choice = scanner.nextLine().trim();
            
            try {
                switch (choice) {
                    case "1":
                        System.out.println("\n🎯 运行简洁快速开始...\n");
                        example.runSimpleQuickStart();
                        break;
                    case "2":
                        System.out.println("\n🎯 运行完整快速开始演示...\n");
                        example.runCompleteQuickStart();
                        break;
                    case "3":
                        runIndividualQuickStartFeatures(example);
                        break;
                    case "0":
                        return;
                    default:
                        System.out.println("❌ 无效选择，请重试");
                }
            } catch (Exception e) {
                System.err.println("❌ 运行示例时出错: " + e.getMessage());
                e.printStackTrace();
            }
            
            if (!choice.equals("0")) {
                pauseForContinue();
            }
        }
    }
    
    /**
     * 运行单独的快速开始功能
     */
    private void runIndividualQuickStartFeatures(QuickStartExample example) {
        while (true) {
            showFeatureMenu();
            String choice = scanner.nextLine().trim();
            
            try {
                switch (choice) {
                    case "1":
                        System.out.println("\n📝 模板创建和使用演示...\n");
                        example.quickStartTemplate();
                        break;
                    case "2":
                        System.out.println("\n🤖 AI请求执行演示...\n");
                        example.quickStartExecution();
                        break;
                    case "3":
                        System.out.println("\n🎨 多模态内容处理演示...\n");
                        example.quickStartMultimodal();
                        break;
                    case "4":
                        System.out.println("\n⚡ 批量处理演示...\n");
                        example.quickStartBatchProcessing();
                        break;
                    case "5":
                        System.out.println("\n🔍 模板搜索和管理演示...\n");
                        example.quickStartTemplateManagement();
                        break;
                    case "6":
                        System.out.println("\n✅ 内容验证演示...\n");
                        example.quickStartValidation();
                        break;
                    case "0":
                        return;
                    default:
                        System.out.println("❌ 无效选择，请重试");
                }
            } catch (Exception e) {
                System.err.println("❌ 运行功能演示时出错: " + e.getMessage());
            }
            
            if (!choice.equals("0")) {
                pauseForContinue();
            }
        }
    }
    
    /**
     * 运行应用程序演示
     */
    private void runApplicationDemo() {
        ApplicationDemo demo = new ApplicationDemo();
        
        while (true) {
            showAppDemoMenu();
            String choice = scanner.nextLine().trim();
            
            try {
                switch (choice) {
                    case "1":
                        System.out.println("\n🚀 运行简化演示...\n");
                        demo.runSimpleDemo();
                        break;
                    case "2":
                        System.out.println("\n🎯 运行完整应用程序演示...\n");
                        demo.runCompleteDemo();
                        break;
                    case "3":
                        System.out.println("\n🤖 运行智能客服演示...\n");
                        demo.demoCustomerService();
                        break;
                    case "4":
                        System.out.println("\n💻 运行代码审查演示...\n");
                        demo.demoCodeReview();
                        break;
                    case "5":
                        System.out.println("\n✍️ 运行内容生成演示...\n");
                        demo.demoContentGeneration();
                        break;
                    case "6":
                        System.out.println("\n⚡ 运行批量处理演示...\n");
                        demo.demoBatchProcessing();
                        break;
                    case "7":
                        System.out.println("\n📈 运行性能监控演示...\n");
                        demo.demoPerformanceMonitoring();
                        break;
                    case "0":
                        return;
                    default:
                        System.out.println("❌ 无效选择，请重试");
                }
            } catch (Exception e) {
                System.err.println("❌ 运行演示时出错: " + e.getMessage());
                e.printStackTrace();
            }
            
            if (!choice.equals("0")) {
                pauseForContinue();
            }
        }
    }
    
    /**
     * 运行模板管理示例
     */
    private void runPromptTemplateExample() {
        System.out.println("\n📋 运行提示词模板管理示例...\n");
        PromptTemplateExample example = new PromptTemplateExample(templateManager, executor);
        
        try {
            example.runAllExamples();
        } catch (Exception e) {
            System.err.println("❌ 运行模板示例时出错: " + e.getMessage());
        }
        
        pauseForContinue();
    }
    
    /**
     * 运行多模态示例
     */
    private void runMultimodalExample() {
        System.out.println("\n🎨 运行多模态内容处理示例...\n");
        MultimodalExample example = new MultimodalExample(contentManager);
        
        try {
            example.runAllExamples();
        } catch (Exception e) {
            System.err.println("❌ 运行多模态示例时出错: " + e.getMessage());
        }
        
        pauseForContinue();
    }
    
    /**
     * 运行中间件示例
     */
    private void runMiddlewareExample() {
        System.out.println("\n🔧 运行AI中间件使用示例...\n");
        MiddlewareExample example = new MiddlewareExample();
        
        try {
            example.runAllExamples();
        } catch (Exception e) {
            System.err.println("❌ 运行中间件示例时出错: " + e.getMessage());
        }
        
        pauseForContinue();
    }
    
    /**
     * 运行所有示例
     */
    private void runAllExamples() {
        System.out.println("\n🎯 运行所有示例 (这可能需要一些时间)...\n");
        
        try {
            // 1. 快速开始示例
            System.out.println("1️⃣ 快速开始示例...\n");
            QuickStartExample quickStart = new QuickStartExample(templateManager, executor, contentManager);
            quickStart.runSimpleQuickStart();
            pauseForContinue();
            
            // 2. 应用程序演示
            System.out.println("2️⃣ 应用程序演示...\n");
            ApplicationDemo appDemo = new ApplicationDemo();
            appDemo.runSimpleDemo();
            pauseForContinue();
            
            // 3. 模板管理示例
            System.out.println("3️⃣ 模板管理示例...\n");
            PromptTemplateExample templateExample = new PromptTemplateExample(templateManager, executor);
            templateExample.demoTemplateCreation();
            templateExample.demoTemplateUsage();
            pauseForContinue();
            
            // 4. 多模态示例
            System.out.println("4️⃣ 多模态示例...\n");
            MultimodalExample multimodalExample = new MultimodalExample(contentManager);
            multimodalExample.demoTextProcessing();
            multimodalExample.demoJsonProcessing();
            pauseForContinue();
            
            // 5. 中间件示例
            System.out.println("5️⃣ 中间件示例...\n");
            MiddlewareExample middlewareExample = new MiddlewareExample();
            middlewareExample.demoBasicUsage();
            middlewareExample.demoRetryMechanism();
            pauseForContinue();
            
            System.out.println("🎉 所有示例运行完成！");
            
        } catch (Exception e) {
            System.err.println("❌ 运行示例时出错: " + e.getMessage());
        }
    }
    
    /**
     * 暂停等待用户继续
     */
    private void pauseForContinue() {
        System.out.println("\n⏸️  按回车键继续...");
        scanner.nextLine();
    }
    
    /**
     * 显示欢迎信息
     */
    private void showWelcome() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("🎉 欢迎使用 KnowGo AI 示例程序！");
        System.out.println("=".repeat(60));
        System.out.println("这个程序将帮助您了解 KnowGo AI 的所有功能特性。");
        System.out.println("您可以选择运行不同的示例来体验各种功能。");
        System.out.println("建议新用户从 '快速开始示例' 开始。");
        System.out.println("=".repeat(60));
    }
    
    /**
     * 显示结束信息
     */
    private void showGoodbye() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("👋 感谢使用 KnowGo AI 示例程序！");
        System.out.println("=".repeat(60));
        System.out.println("💡 您现在可以：");
        System.out.println("   • 将 KnowGo AI 集成到您的项目中");
        System.out.println("   • 根据示例代码定制您的应用");
        System.out.println("   • 查看 README.md 获取更多文档");
        System.out.println("   • 运行单元测试验证功能");
        System.out.println("=".repeat(60));
    }
    
    /**
     * 运行示例运行器
     */
    public void run() {
        showWelcome();
        
        while (true) {
            showMainMenu();
            String choice = scanner.nextLine().trim();
            
            try {
                switch (choice) {
                    case "1":
                        runQuickStartExample();
                        break;
                    case "2":
                        runApplicationDemo();
                        break;
                    case "3":
                        runPromptTemplateExample();
                        break;
                    case "4":
                        runMultimodalExample();
                        break;
                    case "5":
                        runMiddlewareExample();
                        break;
                    case "6":
                        runAllExamples();
                        break;
                    case "0":
                        showGoodbye();
                        return;
                    default:
                        System.out.println("❌ 无效选择，请重试");
                }
            } catch (Exception e) {
                System.err.println("❌ 运行示例时出错: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
    
    /**
     * 主方法 - 用于测试
     */
    public static void main(String[] args) {
        System.out.println("⚠️  注意：这个类需要依赖注入的组件。");
        System.out.println("在实际使用中，请通过 Spring 或其他 DI 框架注入所需的组件。");
        System.out.println("或者使用下面的测试运行器：");
        System.out.println();
        System.out.println("💡 建议使用方法：");
        System.out.println("1. 在您的 Spring Boot 应用中注入组件");
        System.out.println("2. 创建 ExampleRunner 实例");
        System.out.println("3. 调用 run() 方法");
        System.out.println();
        System.out.println("例如：");
        System.out.println("```java");
        System.out.println("@Autowired");
        System.out.println("private PromptTemplateManager templateManager;");
        System.out.println("");
        System.out.println("@Autowired");
        System.out.println("private PromptTemplateExecutor executor;");
        System.out.println("");
        System.out.println("@Autowired");
        System.out.println("private MultimodalContentManager contentManager;");
        System.out.println("");
        System.out.println("// 运行示例");
        System.out.println("ExampleRunner runner = new ExampleRunner(templateManager, executor, contentManager);");
        System.out.println("runner.run();");
        System.out.println("```");
    }
}