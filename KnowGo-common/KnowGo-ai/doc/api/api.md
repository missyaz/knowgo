# KnowGo AI 模块 API 文档

## 1. 核心接口概述

KnowGo AI模块提供了一系列核心接口，用于AI服务的调用、模板管理和中间件处理。本文档将详细介绍这些接口的使用方法和参数说明。

## 2. 核心服务接口

### 2.1 AiService 接口

`AiService` 是AI服务的抽象接口，定义了与AI服务交互的核心方法。

#### 2.1.1 同步执行方法

```java
/**
 * 同步执行AI请求
 * @param request 提示词执行请求
 * @return 提示词执行响应
 */
PromptExecutionResponse execute(PromptExecutionRequest request);
```

**参数说明：**
- `request`：提示词执行请求对象，包含执行AI请求所需的所有参数

**返回值：**
- `PromptExecutionResponse`：AI服务执行结果响应对象

#### 2.1.2 异步执行方法

```java
/**
 * 异步执行AI请求
 * @param request 提示词执行请求
 * @return 异步执行结果的CompletableFuture
 */
CompletableFuture<PromptExecutionResponse> executeAsync(PromptExecutionRequest request);
```

**参数说明：**
- `request`：提示词执行请求对象，包含执行AI请求所需的所有参数

**返回值：**
- `CompletableFuture<PromptExecutionResponse>`：异步执行结果的CompletableFuture对象

## 3. 提示词模板管理接口

### 3.1 PromptTemplateManager 接口

`PromptTemplateManager` 提供了提示词模板的CRUD操作和管理功能。

#### 3.1.1 创建模板

```java
/**
 * 创建提示词模板
 * @param template 提示词模板对象
 * @return 创建的提示词模板
 */
PromptTemplate createTemplate(PromptTemplate template);
```

**参数说明：**
- `template`：提示词模板对象，包含模板的基本信息和内容

**返回值：**
- `PromptTemplate`：创建成功的提示词模板对象

#### 3.1.2 获取模板

```java
/**
 * 根据ID获取提示词模板
 * @param templateId 模板ID
 * @return 提示词模板对象
 */
PromptTemplate getTemplateById(String templateId);

/**
 * 根据名称获取提示词模板
 * @param name 模板名称
 * @return 提示词模板对象
 */
PromptTemplate getTemplateByName(String name);

/**
 * 获取所有提示词模板
 * @return 提示词模板列表
 */
List<PromptTemplate> getAllTemplates();
```

#### 3.1.3 更新模板

```java
/**
 * 更新提示词模板
 * @param template 提示词模板对象
 * @return 更新后的提示词模板
 */
PromptTemplate updateTemplate(PromptTemplate template);
```

#### 3.1.4 删除模板

```java
/**
 * 删除提示词模板
 * @param templateId 模板ID
 */
void deleteTemplate(String templateId);
```

#### 3.1.5 克隆模板

```java
/**
 * 克隆提示词模板
 * @param templateId 模板ID
 * @param newName 新模板名称
 * @return 克隆的提示词模板
 */
PromptTemplate cloneTemplate(String templateId, String newName);
```

#### 3.1.6 导入/导出模板

```java
/**
 * 导入提示词模板
 * @param templates 提示词模板列表
 */
void importTemplates(List<PromptTemplate> templates);

/**
 * 导出提示词模板
 * @param templateIds 模板ID列表
 * @return 提示词模板列表
 */
List<PromptTemplate> exportTemplates(List<String> templateIds);
```

#### 3.1.7 模板统计信息

```java
/**
 * 获取模板使用统计信息
 * @return 模板统计信息列表
 */
List<TemplateStatistics> getTemplateStatistics();
```

### 3.2 PromptTemplateExecutor 接口

`PromptTemplateExecutor` 负责提示词模板的执行和渲染。

#### 3.2.1 执行模板

```java
/**
 * 执行提示词模板
 * @param templateId 模板ID
 * @param parameters 模板参数
 * @return 提示词执行响应
 */
PromptExecutionResponse executeTemplate(String templateId, Map<String, Object> parameters);

/**
 * 执行提示词模板
 * @param templateId 模板ID
 * @param parameters 模板参数
 * @param options 执行选项
 * @return 提示词执行响应
 */
PromptExecutionResponse executeTemplate(String templateId, Map<String, Object> parameters, 
                                       Map<String, Object> options);

/**
 * 异步执行提示词模板
 * @param templateId 模板ID
 * @param parameters 模板参数
 * @return 异步执行结果的CompletableFuture
 */
CompletableFuture<PromptExecutionResponse> executeTemplateAsync(String templateId, 
                                                               Map<String, Object> parameters);
```

## 4. 中间件接口

### 4.1 AiMiddleware 接口

`AiMiddleware` 定义了中间件的执行方法，用于处理AI请求的增强功能。

```java
/**
 * 执行中间件逻辑
 * @param request 提示词执行请求
 * @param next 下一个中间件或AI服务
 * @return 提示词执行响应
 */
PromptExecutionResponse execute(PromptExecutionRequest request, AiMiddleware next);
```

**参数说明：**
- `request`：提示词执行请求对象
- `next`：下一个中间件或AI服务对象

**返回值：**
- `PromptExecutionResponse`：中间件处理后的响应对象

### 4.2 AiMiddlewareChain 接口

`AiMiddlewareChain` 管理和执行中间件序列。

#### 4.2.1 添加中间件

```java
/**
 * 添加中间件到链的末尾
 * @param middleware 中间件对象
 */
void addMiddleware(AiMiddleware middleware);

/**
 * 在指定位置添加中间件
 * @param index 索引位置
 * @param middleware 中间件对象
 */
void addMiddleware(int index, AiMiddleware middleware);
```

#### 4.2.2 移除中间件

```java
/**
 * 移除中间件
 * @param middleware 中间件对象
 */
void removeMiddleware(AiMiddleware middleware);

/**
 * 根据类型移除中间件
 * @param middlewareClass 中间件类
 */
void removeMiddleware(Class<? extends AiMiddleware> middlewareClass);
```

#### 4.2.3 执行中间件链

```java
/**
 * 执行中间件链
 * @param request 提示词执行请求
 * @param aiService AI服务对象
 * @return 提示词执行响应
 */
PromptExecutionResponse execute(PromptExecutionRequest request, AiService aiService);
```

## 5. 多模态内容接口

### 5.1 MultimodalContentManager 接口

`MultimodalContentManager` 提供了多模态内容的处理和管理功能。

#### 5.1.1 创建多模态内容

```java
/**
 * 创建多模态内容
 * @return 多模态内容构建器
 */
MultimodalContentBuilder createContent();
```

**返回值：**
- `MultimodalContentBuilder`：多模态内容构建器对象

#### 5.1.2 验证多模态内容

```java
/**
 * 验证多模态内容
 * @param content 多模态内容对象
 * @return 验证结果
 */
ValidationResult validateContent(MultimodalContent content);
```

**参数说明：**
- `content`：多模态内容对象

**返回值：**
- `ValidationResult`：验证结果对象，包含验证状态和错误信息

#### 5.1.3 转换多模态内容

```java
/**
 * 将多模态内容转换为指定格式
 * @param content 多模态内容对象
 * @param format 目标格式
 * @return 转换后的内容
 */
<T> T convertContent(MultimodalContent content, String format);
```

**参数说明：**
- `content`：多模态内容对象
- `format`：目标格式名称

**返回值：**
- `T`：转换后的内容对象

## 6. 数据模型接口

### 6.1 PromptTemplate 类

`PromptTemplate` 是提示词模板的数据模型类，包含模板的基本信息和内容。

#### 6.1.1 构建器方法

```java
/**
 * 创建PromptTemplate构建器
 * @return PromptTemplate构建器
 */
public static Builder builder() {
    return new Builder();
}
```

#### 6.1.2 Builder 类方法

```java
/**
 * 设置模板ID
 * @param templateId 模板ID
 * @return 构建器对象
 */
public Builder templateId(String templateId) {
    this.templateId = templateId;
    return this;
}

/**
 * 设置模板名称
 * @param name 模板名称
 * @return 构建器对象
 */
public Builder name(String name) {
    this.name = name;
    return this;
}

/**
 * 设置模板类型
 * @param type 模板类型
 * @return 构建器对象
 */
public Builder type(String type) {
    this.type = type;
    return this;
}

/**
 * 设置模板内容
 * @param content 模板内容
 * @return 构建器对象
 */
public Builder content(String content) {
    this.content = content;
    return this;
}

/**
 * 设置模板参数
 * @param parameters 模板参数列表
 * @return 构建器对象
 */
public Builder parameters(List<String> parameters) {
    this.parameters = parameters;
    return this;
}

/**
 * 添加模板参数
 * @param parameter 模板参数
 * @return 构建器对象
 */
public Builder addParameter(String parameter) {
    if (this.parameters == null) {
        this.parameters = new ArrayList<>();
    }
    this.parameters.add(parameter);
    return this;
}

/**
 * 设置模板元数据
 * @param metadata 模板元数据
 * @return 构建器对象
 */
public Builder metadata(Map<String, Object> metadata) {
    this.metadata = metadata;
    return this;
}

/**
 * 添加模板元数据
 * @param key 键
 * @param value 值
 * @return 构建器对象
 */
public Builder addMetadata(String key, Object value) {
    if (this.metadata == null) {
        this.metadata = new HashMap<>();
    }
    this.metadata.put(key, value);
    return this;
}

/**
 * 构建PromptTemplate对象
 * @return PromptTemplate对象
 */
public PromptTemplate build() {
    return new PromptTemplate(this);
}
```

### 6.2 PromptExecutionRequest 类

`PromptExecutionRequest` 是提示词执行请求的数据模型类，包含执行AI请求所需的所有参数。

#### 6.2.1 构建器方法

```java
/**
 * 创建PromptExecutionRequest构建器
 * @return PromptExecutionRequest构建器
 */
public static Builder builder() {
    return new Builder();
}
```

#### 6.2.2 Builder 类方法

```java
/**
 * 设置模板ID
 * @param templateId 模板ID
 * @return 构建器对象
 */
public Builder templateId(String templateId) {
    this.templateId = templateId;
    return this;
}

/**
 * 设置模板参数
 * @param parameters 模板参数映射
 * @return 构建器对象
 */
public Builder parameters(Map<String, Object> parameters) {
    this.parameters = parameters;
    return this;
}

/**
 * 添加模板参数
 * @param key 键
 * @param value 值
 * @return 构建器对象
 */
public Builder addParameter(String key, Object value) {
    if (this.parameters == null) {
        this.parameters = new HashMap<>();
    }
    this.parameters.put(key, value);
    return this;
}

/**
 * 设置温度参数
 * @param temperature 温度参数
 * @return 构建器对象
 */
public Builder temperature(double temperature) {
    this.temperature = temperature;
    return this;
}

/**
 * 设置Top-P参数
 * @param topP Top-P参数
 * @return 构建器对象
 */
public Builder topP(double topP) {
    this.topP = topP;
    return this;
}

/**
 * 设置最大令牌数
 * @param maxTokens 最大令牌数
 * @return 构建器对象
 */
public Builder maxTokens(int maxTokens) {
    this.maxTokens = maxTokens;
    return this;
}

/**
 * 设置停止序列
 * @param stopSequences 停止序列列表
 * @return 构建器对象
 */
public Builder stopSequences(List<String> stopSequences) {
    this.stopSequences = stopSequences;
    return this;
}

/**
 * 添加停止序列
 * @param stopSequence 停止序列
 * @return 构建器对象
 */
public Builder addStopSequence(String stopSequence) {
    if (this.stopSequences == null) {
        this.stopSequences = new ArrayList<>();
    }
    this.stopSequences.add(stopSequence);
    return this;
}

/**
 * 设置请求元数据
 * @param metadata 请求元数据
 * @return 构建器对象
 */
public Builder metadata(Map<String, Object> metadata) {
    this.metadata = metadata;
    return this;
}

/**
 * 添加请求元数据
 * @param key 键
 * @param value 值
 * @return 构建器对象
 */
public Builder addMetadata(String key, Object value) {
    if (this.metadata == null) {
        this.metadata = new HashMap<>();
    }
    this.metadata.put(key, value);
    return this;
}

/**
 * 设置多模态内容
 * @param multimodalContent 多模态内容对象
 * @return 构建器对象
 */
public Builder multimodalContent(MultimodalContent multimodalContent) {
    this.multimodalContent = multimodalContent;
    return this;
}

/**
 * 构建PromptExecutionRequest对象
 * @return PromptExecutionRequest对象
 */
public PromptExecutionRequest build() {
    return new PromptExecutionRequest(this);
}
```

### 6.3 PromptExecutionResponse 类

`PromptExecutionResponse` 是提示词执行响应的数据模型类，包含AI服务返回的结果和元数据。

#### 6.3.1 构建器方法

```java
/**
 * 创建PromptExecutionResponse构建器
 * @return PromptExecutionResponse构建器
 */
public static Builder builder() {
    return new Builder();
}
```

#### 6.3.2 Builder 类方法

```java
/**
 * 设置请求ID
 * @param requestId 请求ID
 * @return 构建器对象
 */
public Builder requestId(String requestId) {
    this.requestId = requestId;
    return this;
}

/**
 * 设置响应内容
 * @param content 响应内容
 * @return 构建器对象
 */
public Builder content(String content) {
    this.content = content;
    return this;
}

/**
 * 设置响应状态
 * @param status 响应状态
 * @return 构建器对象
 */
public Builder status(String status) {
    this.status = status;
    return this;
}

/**
 * 设置错误信息
 * @param error 错误信息
 * @return 构建器对象
 */
public Builder error(String error) {
    this.error = error;
    return this;
}

/**
 * 设置开始时间
 * @param startTime 开始时间
 * @return 构建器对象
 */
public Builder startTime(LocalDateTime startTime) {
    this.startTime = startTime;
    return this;
}

/**
 * 设置结束时间
 * @param endTime 结束时间
 * @return 构建器对象
 */
public Builder endTime(LocalDateTime endTime) {
    this.endTime = endTime;
    return this;
}

/**
 * 设置执行时间
 * @param executionTime 执行时间(毫秒)
 * @return 构建器对象
 */
public Builder executionTime(long executionTime) {
    this.executionTime = executionTime;
    return this;
}

/**
 * 设置提示词令牌数
 * @param promptTokens 提示词令牌数
 * @return 构建器对象
 */
public Builder promptTokens(int promptTokens) {
    this.promptTokens = promptTokens;
    return this;
}

/**
 * 设置完成令牌数
 * @param completionTokens 完成令牌数
 * @return 构建器对象
 */
public Builder completionTokens(int completionTokens) {
    this.completionTokens = completionTokens;
    return this;
}

/**
 * 设置总令牌数
 * @param totalTokens 总令牌数
 * @return 构建器对象
 */
public Builder totalTokens(int totalTokens) {
    this.totalTokens = totalTokens;
    return this;
}

/**
 * 设置成本
 * @param cost 成本
 * @return 构建器对象
 */
public Builder cost(double cost) {
    this.cost = cost;
    return this;
}

/**
 * 设置使用的模型
 * @param model 模型名称
 * @return 构建器对象
 */
public Builder model(String model) {
    this.model = model;
    return this;
}

/**
 * 设置响应元数据
 * @param metadata 响应元数据
 * @return 构建器对象
 */
public Builder metadata(Map<String, Object> metadata) {
    this.metadata = metadata;
    return this;
}

/**
 * 添加响应元数据
 * @param key 键
 * @param value 值
 * @return 构建器对象
 */
public Builder addMetadata(String key, Object value) {
    if (this.metadata == null) {
        this.metadata = new HashMap<>();
    }
    this.metadata.put(key, value);
    return this;
}

/**
 * 设置备选响应
 * @param alternativeResponses 备选响应列表
 * @return 构建器对象
 */
public Builder alternativeResponses(List<String> alternativeResponses) {
    this.alternativeResponses = alternativeResponses;
    return this;
}

/**
 * 添加备选响应
 * @param alternativeResponse 备选响应
 * @return 构建器对象
 */
public Builder addAlternativeResponse(String alternativeResponse) {
    if (this.alternativeResponses == null) {
        this.alternativeResponses = new ArrayList<>();
    }
    this.alternativeResponses.add(alternativeResponse);
    return this;
}

/**
 * 设置置信度
 * @param confidence 置信度
 * @return 构建器对象
 */
public Builder confidence(double confidence) {
    this.confidence = confidence;
    return this;
}

/**
 * 构建PromptExecutionResponse对象
 * @return PromptExecutionResponse对象
 */
public PromptExecutionResponse build() {
    return new PromptExecutionResponse(this);
}
```

## 7. 异常处理接口

### 7.1 AiException 类

`AiException` 是AI模块的基础异常类，用于封装AI服务调用过程中发生的异常。

```java
/**
 * 获取错误代码
 * @return 错误代码
 */
public String getErrorCode();

/**
 * 获取错误消息
 * @return 错误消息
 */
public String getErrorMessage();

/**
 * 获取错误详情
 * @return 错误详情
 */
public String getErrorDetail();

/**
 * 获取根本原因
 * @return 根本原因
 */
public Throwable getRootCause();
```

## 8. 使用示例

### 8.1 基本使用示例

```java
// 创建提示词模板
PromptTemplate template = PromptTemplate.builder()
    .name("test-template")
    .content("Hello, {{name}}!")
    .addParameter("name")
    .build();

// 保存模板
PromptTemplate savedTemplate = promptTemplateManager.createTemplate(template);

// 创建执行请求
PromptExecutionRequest request = PromptExecutionRequest.builder()
    .templateId(savedTemplate.getTemplateId())
    .addParameter("name", "World")
    .build();

// 执行请求
PromptExecutionResponse response = aiService.execute(request);

// 处理响应
System.out.println("AI Response: " + response.getContent());
```

### 8.2 异步执行示例

```java
// 创建执行请求
PromptExecutionRequest request = PromptExecutionRequest.builder()
    .templateId("test-template")
    .addParameter("name", "World")
    .build();

// 异步执行请求
CompletableFuture<PromptExecutionResponse> future = aiService.executeAsync(request);

// 处理异步结果
future.thenAccept(response -> {
    System.out.println("AI Response: " + response.getContent());
}).exceptionally(ex -> {
    System.err.println("Error occurred: " + ex.getMessage());
    return null;
});
```

### 8.3 多模态内容示例

```java
// 创建多模态内容
MultimodalContent content = multimodalContentManager.createContent()
    .text("Describe this image:")
    .addImage("base64-encoded-image-data")
    .build();

// 创建执行请求
PromptExecutionRequest request = PromptExecutionRequest.builder()
    .templateId("image-description-template")
    .multimodalContent(content)
    .build();

// 执行请求
PromptExecutionResponse response = aiService.execute(request);

// 处理响应
System.out.println("Image description: " + response.getContent());
```

## 9. 最佳实践

1. **模板复用**：将常用的提示词封装为模板，提高代码复用性和可维护性
2. **参数验证**：在执行请求前验证参数的有效性，避免无效请求
3. **异步处理**：对于耗时的AI请求，使用异步执行方式提高系统响应性
4. **错误处理**：合理处理AI服务调用过程中可能发生的异常
5. **资源管理**：及时释放不再使用的资源，避免内存泄漏
6. **监控和日志**：监控AI服务的调用情况和性能指标，记录关键日志

## 10. 版本历史

| 版本 | 发布日期 | 主要变更 |
|------|----------|----------|
| 1.0.0 | 2024-01-01 | 初始版本，包含核心功能 |
| 1.1.0 | 2024-02-15 | 新增多模态支持 |
| 1.2.0 | 2024-03-30 | 增强中间件功能，支持动态管理 |
| 1.3.0 | 2024-05-10 | 优化性能，添加缓存支持 |

## 11. 联系方式

如有任何问题或建议，请联系：
- 项目负责人：KnowGo开发团队
- 邮箱：knowgo-dev@example.com
- 文档更新日期：2024-01-01