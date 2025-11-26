# KnowGo AI 模块使用说明文档

## 1. 概述

KnowGo AI模块是一个高性能、可扩展的AI服务抽象层，提供了统一的AI能力接入和管理接口。本文档将详细介绍如何安装、配置和使用KnowGo AI模块。

## 2. 安装

### 2.1 Maven依赖

将以下依赖添加到项目的pom.xml文件中：

```xml
<dependency>
    <groupId>com.fw</groupId>
    <artifactId>knowgo-ai</artifactId>
    <version>1.0.0</version>
</dependency>
```

### 2.2 Gradle依赖

将以下依赖添加到项目的build.gradle文件中：

```groovy
dependencies {
    implementation 'com.fw:knowgo-ai:1.0.0'
}
```

## 3. 配置

### 3.1 基本配置

在项目的application.properties或application.yml文件中添加以下配置：

**application.properties：**

```properties
# AI服务基本配置
knowgo.ai.service.type=openai
knowgo.ai.service.api-key=your-api-key
knowgo.ai.service.base-url=https://api.openai.com/v1

# 默认执行参数配置
knowgo.ai.default.temperature=0.7
knowgo.ai.default.top-p=0.9
knowgo.ai.default.max-tokens=1000

# 中间件配置
knowgo.ai.middleware.retry.enabled=true
knowgo.ai.middleware.retry.max-attempts=3
knowgo.ai.middleware.retry.delay=1000

knowgo.ai.middleware.circuit-breaker.enabled=true
knowgo.ai.middleware.circuit-breaker.failure-threshold=50
knowgo.ai.middleware.circuit-breaker.wait-duration-in-open-state=60000

knowgo.ai.middleware.rate-limiter.enabled=true
knowgo.ai.middleware.rate-limiter.requests-per-second=10

knowgo.ai.middleware.monitoring.enabled=true
```

**application.yml：**

```yaml
knowgo:
  ai:
    service:
      type: openai
      api-key: your-api-key
      base-url: https://api.openai.com/v1
    default:
      temperature: 0.7
      top-p: 0.9
      max-tokens: 1000
    middleware:
      retry:
        enabled: true
        max-attempts: 3
        delay: 1000
      circuit-breaker:
        enabled: true
        failure-threshold: 50
        wait-duration-in-open-state: 60000
      rate-limiter:
        enabled: true
        requests-per-second: 10
      monitoring:
        enabled: true
```

### 3.2 自定义AI服务配置

如果需要使用自定义的AI服务实现，可以添加以下配置：

```properties
# 自定义AI服务实现类
knowgo.ai.service.custom-implementation=com.example.CustomAiService
```

## 4. 快速开始

### 4.1 创建AI服务实例

```java
// 创建AI服务实例
AiService aiService = AiServiceFactory.createService();
```

### 4.2 创建提示词模板

```java
// 创建提示词模板
PromptTemplate template = PromptTemplate.builder()
    .name("greeting-template")
    .content("Hello, {{name}}! Welcome to KnowGo AI.")
    .addParameter("name")
    .build();

// 创建模板管理器
PromptTemplateManager templateManager = new InMemoryPromptTemplateManager();

// 保存模板
templateManager.createTemplate(template);
```

### 4.3 执行AI请求

```java
// 创建执行请求
PromptExecutionRequest request = PromptExecutionRequest.builder()
    .templateId(template.getTemplateId())
    .addParameter("name", "World")
    .temperature(0.7)
    .maxTokens(100)
    .build();

// 执行请求
PromptExecutionResponse response = aiService.execute(request);

// 处理响应
if (response.isSuccess()) {
    System.out.println("AI Response: " + response.getContent());
} else {
    System.err.println("Error: " + response.getError());
}
```

## 5. 核心功能使用

### 5.1 提示词模板管理

#### 5.1.1 创建和保存模板

```java
// 创建模板
PromptTemplate template = PromptTemplate.builder()
    .name("code-generator")
    .content("Generate a {{language}} function that {{action}}.")
    .addParameter("language")
    .addParameter("action")
    .build();

// 保存模板
PromptTemplate savedTemplate = templateManager.createTemplate(template);
```

#### 5.1.2 获取和更新模板

```java
// 根据ID获取模板
PromptTemplate template = templateManager.getTemplateById("template-id");

// 更新模板内容
template.setContent("New template content with {{parameter}}.");

// 保存更新
PromptTemplate updatedTemplate = templateManager.updateTemplate(template);
```

#### 5.1.3 删除和克隆模板

```java
// 删除模板
templateManager.deleteTemplate("template-id");

// 克隆模板
PromptTemplate clonedTemplate = templateManager.cloneTemplate("template-id", "new-template-name");
```

### 5.2 模板执行

#### 5.2.1 基本执行

```java
// 创建执行请求
PromptExecutionRequest request = PromptExecutionRequest.builder()
    .templateId("code-generator")
    .addParameter("language", "Java")
    .addParameter("action", "calculates the factorial of a number")
    .build();

// 执行请求
PromptExecutionResponse response = aiService.execute(request);
```

#### 5.2.2 异步执行

```java
// 创建执行请求
PromptExecutionRequest request = PromptExecutionRequest.builder()
    .templateId("code-generator")
    .addParameter("language", "Java")
    .addParameter("action", "calculates the factorial of a number")
    .build();

// 异步执行请求
CompletableFuture<PromptExecutionResponse> future = aiService.executeAsync(request);

// 处理异步结果
future.thenAccept(response -> {
    if (response.isSuccess()) {
        System.out.println("Generated code: " + response.getContent());
    } else {
        System.err.println("Error: " + response.getError());
    }
}).exceptionally(ex -> {
    System.err.println("Exception: " + ex.getMessage());
    return null;
});
```

#### 5.2.3 批量执行

```java
// 创建多个执行请求
List<PromptExecutionRequest> requests = Arrays.asList(
    PromptExecutionRequest.builder()
        .templateId("code-generator")
        .addParameter("language", "Java")
        .addParameter("action", "calculates the factorial")
        .build(),
    PromptExecutionRequest.builder()
        .templateId("code-generator")
        .addParameter("language", "Python")
        .addParameter("action", "calculates the fibonacci sequence")
        .build()
);

// 批量执行请求
List<PromptExecutionResponse> responses = aiService.executeBatch(requests);

// 处理响应
responses.forEach(response -> {
    if (response.isSuccess()) {
        System.out.println("Generated code: " + response.getContent());
    }
});
```

### 5.3 多模态内容处理

```java
// 创建多模态内容管理器
MultimodalContentManager contentManager = new DefaultMultimodalContentManager();

// 创建多模态内容
MultimodalContent content = contentManager.createContent()
    .text("Describe this image in detail.")
    .addImage("base64-encoded-image-data")
    .addMetadata("image-type", "landscape")
    .build();

// 创建执行请求
PromptExecutionRequest request = PromptExecutionRequest.builder()
    .templateId("image-description")
    .multimodalContent(content)
    .build();

// 执行请求
PromptExecutionResponse response = aiService.execute(request);

// 处理响应
if (response.isSuccess()) {
    System.out.println("Image description: " + response.getContent());
}
```

### 5.4 中间件配置

```java
// 创建中间件链
AiMiddlewareChain middlewareChain = new DefaultAiMiddlewareChain();

// 添加重试中间件
middlewareChain.addMiddleware(new RetryMiddleware(3, 1000));

// 添加熔断中间件
middlewareChain.addMiddleware(new CircuitBreakerMiddleware(50, 60000));

// 添加监控中间件
middlewareChain.addMiddleware(new MonitoringMiddleware());

// 创建AI服务时配置中间件链
AiService aiService = AiServiceFactory.createService(middlewareChain);
```

## 6. 高级功能

### 6.1 自定义AI服务

```java
// 实现自定义AI服务
public class CustomAiService implements AiService {
    @Override
    public PromptExecutionResponse execute(PromptExecutionRequest request) {
        // 实现与自定义AI服务的交互逻辑
        String content = "Custom AI response for " + request.getParameters().get("query");
        
        return PromptExecutionResponse.builder()
            .requestId(request.getRequestId())
            .content(content)
            .status("SUCCESS")
            .startTime(LocalDateTime.now())
            .endTime(LocalDateTime.now())
            .executionTime(100)
            .build();
    }
    
    @Override
    public CompletableFuture<PromptExecutionResponse> executeAsync(PromptExecutionRequest request) {
        return CompletableFuture.supplyAsync(() -> execute(request));
    }
}

// 使用自定义AI服务
AiService aiService = new CustomAiService();
```

### 6.2 自定义中间件

```java
// 实现自定义中间件
public class LoggingMiddleware implements AiMiddleware {
    private static final Logger logger = LoggerFactory.getLogger(LoggingMiddleware.class);
    
    @Override
    public PromptExecutionResponse execute(PromptExecutionRequest request, AiMiddleware next) {
        logger.info("Executing AI request: {}", request.getRequestId());
        long startTime = System.currentTimeMillis();
        
        try {
            PromptExecutionResponse response = next.execute(request);
            long executionTime = System.currentTimeMillis() - startTime;
            logger.info("AI request completed in {}ms: {}", executionTime, response.getRequestId());
            return response;
        } catch (Exception e) {
            long executionTime = System.currentTimeMillis() - startTime;
            logger.error("AI request failed in {}ms: {}", executionTime, request.getRequestId(), e);
            throw e;
        }
    }
}

// 添加自定义中间件到中间件链
middlewareChain.addMiddleware(new LoggingMiddleware());
```

### 6.3 模板参数高级用法

#### 6.3.1 条件逻辑

```java
// 创建包含条件逻辑的模板
PromptTemplate template = PromptTemplate.builder()
    .name("conditional-template")
    .content("Hello, {{name}}! {{#if isMember}}Welcome back!{{else}}Please sign up.{{/if}}")
    .addParameter("name")
    .addParameter("isMember")
    .build();

// 使用条件参数
PromptExecutionRequest request = PromptExecutionRequest.builder()
    .templateId(template.getTemplateId())
    .addParameter("name", "John")
    .addParameter("isMember", true)
    .build();
```

#### 6.3.2 循环逻辑

```java
// 创建包含循环逻辑的模板
PromptTemplate template = PromptTemplate.builder()
    .name("list-template")
    .content("Your tasks:
{{#each tasks}}
- {{this}}
{{/each}}")
    .addParameter("tasks")
    .build();

// 使用列表参数
List<String> tasks = Arrays.asList("Task 1", "Task 2", "Task 3");
PromptExecutionRequest request = PromptExecutionRequest.builder()
    .templateId(template.getTemplateId())
    .addParameter("tasks", tasks)
    .build();
```

## 7. 错误处理

### 7.1 基本错误处理

```java
try {
    PromptExecutionResponse response = aiService.execute(request);
    if (response.isSuccess()) {
        // 处理成功响应
    } else {
        // 处理业务错误
        System.err.println("AI Service Error: " + response.getError());
    }
} catch (AiException e) {
    // 处理AI服务异常
    System.err.println("AI Exception: " + e.getMessage());
    System.err.println("Error Code: " + e.getErrorCode());
} catch (Exception e) {
    // 处理其他异常
    System.err.println("Unexpected Error: " + e.getMessage());
}
```

### 7.2 重试和熔断

```java
// 配置重试中间件
RetryMiddleware retryMiddleware = new RetryMiddleware.Builder()
    .maxAttempts(3)
    .delay(1000)
    .retryableExceptions(IOException.class, TimeoutException.class)
    .build();

// 配置熔断中间件
CircuitBreakerMiddleware circuitBreaker = new CircuitBreakerMiddleware.Builder()
    .failureThreshold(50)
    .waitDurationInOpenState(60000)
    .build();

// 添加到中间件链
middlewareChain.addMiddleware(retryMiddleware);
middlewareChain.addMiddleware(circuitBreaker);
```

## 8. 性能优化

### 8.1 缓存策略

```java
// 配置缓存
AiCache cache = new RedisAiCache(redisTemplate);
AiService aiService = AiServiceFactory.createService(middlewareChain, cache);

// 创建缓存请求
PromptExecutionRequest request = PromptExecutionRequest.builder()
    .templateId("template-id")
    .addParameter("key", "value")
    .addMetadata("cache-enabled", "true")
    .addMetadata("cache-ttl", "3600")
    .build();
```

### 8.2 异步处理

```java
// 异步执行请求
CompletableFuture<PromptExecutionResponse> future = aiService.executeAsync(request);

// 处理异步结果
future.thenAccept(response -> {
    // 处理响应
}).exceptionally(ex -> {
    // 处理异常
    return null;
});

// 组合多个异步请求
CompletableFuture<PromptExecutionResponse> future1 = aiService.executeAsync(request1);
CompletableFuture<PromptExecutionResponse> future2 = aiService.executeAsync(request2);

CompletableFuture<Void> allOf = CompletableFuture.allOf(future1, future2);
allOf.thenRun(() -> {
    // 所有请求完成
});
```

## 9. 监控和日志

### 9.1 监控指标

KnowGo AI模块提供了以下监控指标：

- `knowgo_ai_request_count`：AI请求总数
- `knowgo_ai_request_success_count`：成功的AI请求数
- `knowgo_ai_request_failure_count`：失败的AI请求数
- `knowgo_ai_request_duration_ms`：AI请求执行时间（毫秒）
- `knowgo_ai_request_tokens_prompt`：提示词令牌数
- `knowgo_ai_request_tokens_completion`：完成令牌数
- `knowgo_ai_request_cost`：AI请求成本

### 9.2 日志配置

在logback.xml或log4j2.xml文件中添加以下配置：

**logback.xml：**

```xml
<logger name="com.fw.know.go.ai" level="INFO" additivity="false">
    <appender-ref ref="CONSOLE" />
    <appender-ref ref="FILE" />
</logger>
```

**log4j2.xml：**

```xml
<Logger name="com.fw.know.go.ai" level="INFO" additivity="false">
    <AppenderRef ref="Console" />
    <AppenderRef ref="File" />
</Logger>
```

## 10. 示例代码

KnowGo AI模块提供了丰富的示例代码，位于`src/main/java/com/fw/know/go/ai/example`目录下：

- `QuickStartExample.java`：快速开始示例
- `ApplicationDemo.java`：完整应用程序演示
- `ExampleRunner.java`：示例运行器

运行示例：

```bash
java -cp knowgo-ai-1.0.0.jar com.fw.know.go.ai.example.ExampleRunner
```

## 11. 常见问题

### 11.1 API密钥配置

**问题：** 收到"Invalid API Key"错误。

**解决方案：** 检查配置文件中的API密钥是否正确，确保没有额外的空格或特殊字符。

### 11.2 模板参数缺失

**问题：** 收到"Missing required parameter"错误。

**解决方案：** 检查执行请求中是否包含了模板所需的所有参数。

### 11.3 多模态内容大小限制

**问题：** 收到"Content too large"错误。

**解决方案：** 确保多模态内容的大小不超过AI服务的限制，考虑压缩或缩小内容。

### 11.4 并发请求限制

**问题：** 收到"Rate limit exceeded"错误。

**解决方案：** 增加中间件链中的限流中间件配置，或减少并发请求数量。

## 12. 联系方式

如有任何问题或建议，请联系：
- 项目负责人：KnowGo开发团队
- 邮箱：knowgo-dev@example.com
- 文档更新日期：2024-01-01