# KnowGo AI 模块

KnowGo AI 是一个功能强大的企业级AI服务框架，提供了完整的提示词模板管理、AI调用中间件和多模态内容处理能力。

## 🌟 核心功能

### 1. 提示词模板管理系统
- **模板管理**: 创建、更新、删除、查询提示词模板
- **参数化渲染**: 支持动态参数替换和模板渲染
- **分类管理**: 按分类、类型、标签、模型类型组织模板
- **执行管理**: 同步/异步执行、批量处理、执行统计

### 2. AI调用中间件
- **重试机制**: 指数退避、抖动、可配置重试策略
- **熔断保护**: 三种状态管理、失败率计算、自动恢复
- **监控指标**: 实时性能统计、错误追踪、指标收集
- **中间件链**: 优先级排序、动态管理、灵活配置

### 3. 多模态内容处理
- **内容类型**: 文本、图片、音频、视频、PDF、文件
- **内容处理器**: 专门的处理器支持各种内容类型
- **格式转换**: 支持多种格式之间的转换
- **压缩优化**: 内容压缩、解压缩、大小估算

## 📦 快速开始

### 1. 添加依赖

```xml
<dependency>
    <groupId>com.fw</groupId>
    <artifactId>KnowGo-ai</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

### 2. 配置应用

```yaml
spring:
  ai:
    openai:
      api-key: your-openai-api-key
      base-url: https://api.openai.com
    dashscope:
      api-key: your-dashscope-api-key
```

### 3. 使用示例

#### 提示词模板管理

```java
@Autowired
private PromptTemplateManager templateManager;

// 创建模板
PromptTemplate template = PromptTemplate.builder()
    .id("code-review")
    .name("代码审查")
    .content("请审查以下代码：\n${code}\n\n重点关注：${focus}")
    .category("开发")
    .type(TemplateType.USER)
    .build();

templateManager.saveTemplate(template);

// 使用模板
Map<String, Object> params = new HashMap<>();
params.put("code", "public void method() {...}");
params.put("focus", "性能和安全性");

String renderedPrompt = templateManager.renderTemplate("code-review", params);
```

#### AI调用执行

```java
@Autowired
private PromptTemplateExecutor executor;

// 创建执行请求
PromptExecutionRequest request = PromptExecutionRequest.builder()
    .templateId("code-review")
    .parameters(params)
    .modelType("gpt-4")
    .temperature(0.7)
    .maxTokens(1000)
    .build();

// 同步执行
PromptExecutionResponse response = executor.execute(request);

// 异步执行
CompletableFuture<PromptExecutionResponse> future = executor.executeAsync(request);
```

#### 多模态内容处理

```java
@Autowired
private MultimodalContentManager contentManager;

// 处理文本内容
String textContent = "这是一段文本内容";
MultimodalContent textResult = contentManager.processContent(textContent, "text/plain");

// 处理图片内容
String imageBase64 = "data:image/jpeg;base64,/9j/4AAQSkZJRg...";
MultimodalContent imageResult = contentManager.processContent(imageBase64, "image/jpeg");

// 格式转换
MultimodalContent converted = contentManager.convertContent(imageResult, "image/png");
```

## 🏗️ 架构设计

### 核心组件

```
KnowGo-ai/
├── core/                    # 核心模型和接口
│   ├── model/              # 数据模型
│   ├── prompt/             # 提示词管理
│   ├── multimodal/         # 多模态处理
│   └── middleware/         # 中间件机制
├── impl/                   # 实现类
└── config/                # 配置类
```

### 设计原则

1. **接口隔离**: 每个功能模块都有清晰的接口定义
2. **依赖倒置**: 依赖于抽象而不是具体实现
3. **开闭原则**: 易于扩展新的功能而不修改现有代码
4. **单一职责**: 每个类都有明确的职责范围

## 🔧 高级配置

### 重试配置

```yaml
knowgo:
  ai:
    retry:
      max-attempts: 3
      initial-interval: 1000ms
      max-interval: 10000ms
      multiplier: 2.0
      enable-jitter: true
```

### 熔断配置

```yaml
knowgo:
  ai:
    circuit-breaker:
      failure-rate-threshold: 50
      wait-duration-in-open-state: 30s
      sliding-window-size: 10
      minimum-number-of-calls: 5
```

### 监控配置

```yaml
knowgo:
  ai:
    metrics:
      enabled: true
      export-interval: 60s
      include-details: true
```

## 📊 性能优化

### 缓存策略
- 模板缓存：减少模板加载时间
- 内容缓存：避免重复处理相同内容
- 结果缓存：缓存AI模型响应

### 异步处理
- 异步执行：非阻塞的AI调用
- 批量处理：批量执行多个请求
- 并发控制：限制并发请求数量

### 资源管理
- 连接池：复用HTTP连接
- 内存管理：及时释放大对象
- 线程池：合理的线程配置

## 🔍 监控和调试

### 指标收集
- 请求指标：QPS、延迟、成功率
- 错误指标：错误率、错误类型分布
- 资源指标：内存使用、线程数

### 日志记录
- 请求日志：记录所有AI请求
- 错误日志：详细错误信息
- 性能日志：关键操作耗时

### 健康检查
- 服务健康：AI服务可用性
- 依赖健康：外部服务状态
- 资源健康：系统资源使用情况

## 🧪 测试

### 单元测试
```bash
mvn test
```

### 集成测试
```bash
mvn integration-test
```

### 性能测试
```bash
mvn test -Dtest=PerformanceTest
```

## 🤝 贡献指南

1. Fork 项目
2. 创建特性分支 (`git checkout -b feature/amazing-feature`)
3. 提交更改 (`git commit -m 'Add some amazing feature'`)
4. 推送到分支 (`git push origin feature/amazing-feature`)
5. 创建 Pull Request

## 📄 许可证

本项目采用 MIT 许可证 - 查看 [LICENSE](LICENSE) 文件了解详情。

## 🆘 支持

如果您遇到问题或有建议，请通过以下方式联系我们：

- 提交 Issue
- 发送邮件至: support@knowgo.com
- 访问文档: https://docs.knowgo.com

---

**Made with ❤️ by KnowGo Team**