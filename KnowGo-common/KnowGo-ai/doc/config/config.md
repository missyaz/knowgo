# KnowGo AI 模块配置文档

## 1. 配置概述

KnowGo AI模块提供了丰富的配置选项，用于定制AI服务的行为、中间件功能和多模态支持等。本文档详细介绍了所有可用的配置参数及其默认值。

## 2. 配置格式

KnowGo AI模块支持两种配置格式：

1. **Properties格式**：使用application.properties文件
2. **YAML格式**：使用application.yml文件

## 3. 核心配置

### 3.1 AI服务配置

#### 3.1.1 基本服务配置

| 配置项 | 类型 | 默认值 | 描述 |
|--------|------|--------|------|
| knowgo.ai.service.type | String | openai | AI服务类型，支持openai、azure-openai、custom等 |
| knowgo.ai.service.api-key | String | 无 | AI服务API密钥 |
| knowgo.ai.service.base-url | String | 无 | AI服务基础URL |
| knowgo.ai.service.model | String | gpt-3.5-turbo | 默认使用的AI模型 |
| knowgo.ai.service.custom-implementation | String | 无 | 自定义AI服务实现类的全限定名 |

#### 3.1.2 OpenAI服务配置

| 配置项 | 类型 | 默认值 | 描述 |
|--------|------|--------|------|
| knowgo.ai.service.openai.api-key | String | 无 | OpenAI API密钥 |
| knowgo.ai.service.openai.base-url | String | https://api.openai.com/v1 | OpenAI API基础URL |
| knowgo.ai.service.openai.model | String | gpt-3.5-turbo | 默认使用的OpenAI模型 |
| knowgo.ai.service.openai.timeout | Integer | 30000 | 请求超时时间（毫秒） |

#### 3.1.3 Azure OpenAI服务配置

| 配置项 | 类型 | 默认值 | 描述 |
|--------|------|--------|------|
| knowgo.ai.service.azure-openai.api-key | String | 无 | Azure OpenAI API密钥 |
| knowgo.ai.service.azure-openai.base-url | String | 无 | Azure OpenAI API基础URL |
| knowgo.ai.service.azure-openai.deployment-name | String | 无 | Azure OpenAI部署名称 |
| knowgo.ai.service.azure-openai.api-version | String | 2023-05-15 | Azure OpenAI API版本 |

### 3.2 默认执行参数配置

| 配置项 | 类型 | 默认值 | 描述 |
|--------|------|--------|------|
| knowgo.ai.default.temperature | Double | 0.7 | 默认温度参数（0-2，值越高创造力越强） |
| knowgo.ai.default.top-p | Double | 0.9 | 默认Top-P参数（0-1，控制输出的多样性） |
| knowgo.ai.default.max-tokens | Integer | 1000 | 默认最大令牌数 |
| knowgo.ai.default.presence-penalty | Double | 0.0 | 默认存在惩罚（-2到2，鼓励新主题） |
| knowgo.ai.default.frequency-penalty | Double | 0.0 | 默认频率惩罚（-2到2，减少重复内容） |
| knowgo.ai.default.stop-sequences | List<String> | 无 | 默认停止序列列表 |
| knowgo.ai.default.n | Integer | 1 | 默认生成的响应数量 |

### 3.3 模板管理配置

| 配置项 | 类型 | 默认值 | 描述 |
|--------|------|--------|------|
| knowgo.ai.template.manager.type | String | in-memory | 模板管理器类型，支持in-memory、database等 |
| knowgo.ai.template.cache.enabled | Boolean | true | 是否启用模板缓存 |
| knowgo.ai.template.cache.ttl | Integer | 3600 | 模板缓存TTL（秒） |
| knowgo.ai.template.validation.enabled | Boolean | true | 是否启用模板验证 |

### 3.4 多模态内容配置

| 配置项 | 类型 | 默认值 | 描述 |
|--------|------|--------|------|
| knowgo.ai.multimodal.enabled | Boolean | true | 是否启用多模态支持 |
| knowgo.ai.multimodal.max-image-size | Long | 10485760 | 最大图片大小（字节） |
| knowgo.ai.multimodal.max-audio-size | Long | 104857600 | 最大音频大小（字节） |
| knowgo.ai.multimodal.max-video-size | Long | 1073741824 | 最大视频大小（字节） |
| knowgo.ai.multimodal.max-files | Integer | 5 | 最大文件数量 |
| knowgo.ai.multimodal.supported-image-types | List<String> | [image/jpeg, image/png, image/gif] | 支持的图片类型 |
| knowgo.ai.multimodal.supported-audio-types | List<String> | [audio/mpeg, audio/wav] | 支持的音频类型 |
| knowgo.ai.multimodal.supported-video-types | List<String> | [video/mp4, video/quicktime] | 支持的视频类型 |

## 4. 中间件配置

### 4.1 重试中间件配置

| 配置项 | 类型 | 默认值 | 描述 |
|--------|------|--------|------|
| knowgo.ai.middleware.retry.enabled | Boolean | true | 是否启用重试中间件 |
| knowgo.ai.middleware.retry.max-attempts | Integer | 3 | 最大重试次数 |
| knowgo.ai.middleware.retry.delay | Integer | 1000 | 重试延迟（毫秒） |
| knowgo.ai.middleware.retry.max-delay | Integer | 30000 | 最大重试延迟（毫秒） |
| knowgo.ai.middleware.retry.backoff-multiplier | Double | 2.0 | 退避乘数 |
| knowgo.ai.middleware.retry.retryable-exceptions | List<String> | [IOException, TimeoutException] | 可重试的异常类型 |

### 4.2 熔断中间件配置

| 配置项 | 类型 | 默认值 | 描述 |
|--------|------|--------|------|
| knowgo.ai.middleware.circuit-breaker.enabled | Boolean | true | 是否启用熔断中间件 |
| knowgo.ai.middleware.circuit-breaker.failure-threshold | Integer | 50 | 失败阈值（百分比） |
| knowgo.ai.middleware.circuit-breaker.wait-duration-in-open-state | Long | 60000 | 熔断打开状态持续时间（毫秒） |
| knowgo.ai.middleware.circuit-breaker.ring-buffer-size-in-half-open-state | Integer | 10 | 半开状态下的环形缓冲区大小 |
| knowgo.ai.middleware.circuit-breaker.ring-buffer-size-in-closed-state | Integer | 100 | 关闭状态下的环形缓冲区大小 |
| knowgo.ai.middleware.circuit-breaker.record-exceptions | List<String> | [Exception] | 记录为失败的异常类型 |
| knowgo.ai.middleware.circuit-breaker.ignore-exceptions | List<String> | [IllegalArgumentException] | 忽略的异常类型 |

### 4.3 限流中间件配置

| 配置项 | 类型 | 默认值 | 描述 |
|--------|------|--------|------|
| knowgo.ai.middleware.rate-limiter.enabled | Boolean | true | 是否启用限流中间件 |
| knowgo.ai.middleware.rate-limiter.requests-per-second | Integer | 10 | 每秒最大请求数 |
| knowgo.ai.middleware.rate-limiter.burst-capacity | Integer | 20 | 突发容量 |
| knowgo.ai.middleware.rate-limiter.queue-capacity | Integer | 10 | 队列容量 |
| knowgo.ai.middleware.rate-limiter.timeout | Integer | 500 | 队列超时时间（毫秒） |

### 4.4 监控中间件配置

| 配置项 | 类型 | 默认值 | 描述 |
|--------|------|--------|------|
| knowgo.ai.middleware.monitoring.enabled | Boolean | true | 是否启用监控中间件 |
| knowgo.ai.middleware.monitoring.metrics.enabled | Boolean | true | 是否启用指标收集 |
| knowgo.ai.middleware.monitoring.logging.enabled | Boolean | true | 是否启用日志记录 |
| knowgo.ai.middleware.monitoring.tracing.enabled | Boolean | false | 是否启用分布式跟踪 |

### 4.5 安全中间件配置

| 配置项 | 类型 | 默认值 | 描述 |
|--------|------|--------|------|
| knowgo.ai.middleware.security.enabled | Boolean | true | 是否启用安全中间件 |
| knowgo.ai.middleware.security.parameter-validation.enabled | Boolean | true | 是否启用参数验证 |
| knowgo.ai.middleware.security.sensitive-data-masking.enabled | Boolean | true | 是否启用敏感数据屏蔽 |
| knowgo.ai.middleware.security.sensitive-parameters | List<String> | [api-key, password, secret] | 敏感参数列表 |

## 5. 缓存配置

| 配置项 | 类型 | 默认值 | 描述 |
|--------|------|--------|------|
| knowgo.ai.cache.enabled | Boolean | true | 是否启用结果缓存 |
| knowgo.ai.cache.type | String | in-memory | 缓存类型，支持in-memory、redis等 |
| knowgo.ai.cache.ttl | Integer | 3600 | 缓存TTL（秒） |
| knowgo.ai.cache.max-size | Long | 1000 | 缓存最大条目数 |
| knowgo.ai.cache.key-prefix | String | knowgo-ai:cache: | 缓存键前缀 |

### 5.1 Redis缓存配置

| 配置项 | 类型 | 默认值 | 描述 |
|--------|------|--------|------|
| knowgo.ai.cache.redis.host | String | localhost | Redis服务器主机 |
| knowgo.ai.cache.redis.port | Integer | 6379 | Redis服务器端口 |
| knowgo.ai.cache.redis.password | String | 无 | Redis密码 |
| knowgo.ai.cache.redis.database | Integer | 0 | Redis数据库索引 |
| knowgo.ai.cache.redis.timeout | Integer | 5000 | Redis连接超时（毫秒） |

## 6. 异步执行配置

| 配置项 | 类型 | 默认值 | 描述 |
|--------|------|--------|------|
| knowgo.ai.async.enabled | Boolean | true | 是否启用异步执行支持 |
| knowgo.ai.async.core-pool-size | Integer | 5 | 线程池核心大小 |
| knowgo.ai.async.max-pool-size | Integer | 20 | 线程池最大大小 |
| knowgo.ai.async.queue-capacity | Integer | 100 | 任务队列容量 |
| knowgo.ai.async.keep-alive-seconds | Integer | 60 | 线程存活时间（秒） |
| knowgo.ai.async.thread-name-prefix | String | knowgo-ai-async- | 线程名称前缀 |

## 7. 错误处理配置

| 配置项 | 类型 | 默认值 | 描述 |
|--------|------|--------|------|
| knowgo.ai.error.handling.enabled | Boolean | true | 是否启用统一错误处理 |
| knowgo.ai.error.handling.hide-details | Boolean | false | 是否隐藏错误详情（生产环境建议开启） |
| knowgo.ai.error.handling.max-error-stack-depth | Integer | 10 | 错误堆栈最大深度 |
| knowgo.ai.error.handling.custom-error-messages.enabled | Boolean | false | 是否启用自定义错误消息 |

## 8. 监控和日志配置

### 8.1 指标配置

| 配置项 | 类型 | 默认值 | 描述 |
|--------|------|--------|------|
| knowgo.ai.metrics.enabled | Boolean | true | 是否启用指标收集 |
| knowgo.ai.metrics.exporter | String | prometheus | 指标导出器类型，支持prometheus、influx等 |
| knowgo.ai.metrics.prefix | String | knowgo_ai_ | 指标前缀 |

### 8.2 日志配置

| 配置项 | 类型 | 默认值 | 描述 |
|--------|------|--------|------|
| knowgo.ai.logging.enabled | Boolean | true | 是否启用详细日志 |
| knowgo.ai.logging.level | String | INFO | 日志级别 |
| knowgo.ai.logging.request.enabled | Boolean | false | 是否记录请求详情 |
| knowgo.ai.logging.response.enabled | Boolean | false | 是否记录响应详情 |
| knowgo.ai.logging.masking.enabled | Boolean | true | 是否屏蔽敏感信息 |

## 9. 高级配置

### 9.1 性能优化配置

| 配置项 | 类型 | 默认值 | 描述 |
|--------|------|--------|------|
| knowgo.ai.performance.batch-size | Integer | 10 | 批量处理大小 |
| knowgo.ai.performance.prefetch-size | Integer | 5 | 预取大小 |
| knowgo.ai.performance.connection-pool-size | Integer | 10 | 连接池大小 |
| knowgo.ai.performance.connection-timeout | Integer | 5000 | 连接超时时间（毫秒） |

### 9.2 扩展配置

| 配置项 | 类型 | 默认值 | 描述 |
|--------|------|--------|------|
| knowgo.ai.extensions.enabled | Boolean | true | 是否启用扩展点 |
| knowgo.ai.extensions.scan-packages | List<String> | [com.fw.know.go.ai.extension] | 扩展扫描包列表 |

## 10. 配置示例

### 10.1 Properties配置示例

```properties
# AI服务配置
knowgo.ai.service.type=openai
knowgo.ai.service.api-key=your-api-key
knowgo.ai.service.base-url=https://api.openai.com/v1
knowgo.ai.service.model=gpt-3.5-turbo

# 默认执行参数
knowgo.ai.default.temperature=0.7
knowgo.ai.default.top-p=0.9
knowgo.ai.default.max-tokens=1000

# 中间件配置
knowgo.ai.middleware.retry.enabled=true
knowgo.ai.middleware.retry.max-attempts=3
knowgo.ai.middleware.retry.delay=1000

knowgo.ai.middleware.circuit-breaker.enabled=true
knowgo.ai.middleware.circuit-breaker.failure-threshold=50

knowgo.ai.middleware.rate-limiter.enabled=true
knowgo.ai.middleware.rate-limiter.requests-per-second=10

# 多模态配置
knowgo.ai.multimodal.enabled=true
knowgo.ai.multimodal.max-image-size=10485760

# 缓存配置
knowgo.ai.cache.enabled=true
knowgo.ai.cache.type=in-memory
knowgo.ai.cache.ttl=3600

# 异步配置
knowgo.ai.async.enabled=true
knowgo.ai.async.core-pool-size=5
knowgo.ai.async.max-pool-size=20
```

### 10.2 YAML配置示例

```yaml
knowgo:
  ai:
    service:
      type: openai
      api-key: your-api-key
      base-url: https://api.openai.com/v1
      model: gpt-3.5-turbo
    
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
      rate-limiter:
        enabled: true
        requests-per-second: 10
    
    multimodal:
      enabled: true
      max-image-size: 10485760
    
    cache:
      enabled: true
      type: in-memory
      ttl: 3600
    
    async:
      enabled: true
      core-pool-size: 5
      max-pool-size: 20
```

## 11. 自定义配置

### 11.1 自定义AI服务配置

如果需要使用自定义的AI服务实现，可以配置：

```properties
knowgo.ai.service.type=custom
knowgo.ai.service.custom-implementation=com.example.CustomAiService
```

### 11.2 自定义中间件配置

可以通过编程方式添加自定义中间件：

```java
@Configuration
public class AiMiddlewareConfig {
    
    @Bean
    public AiMiddlewareChain middlewareChain() {
        AiMiddlewareChain chain = new DefaultAiMiddlewareChain();
        chain.addMiddleware(new CustomMiddleware());
        return chain;
    }
}
```

## 12. 配置优先级

KnowGo AI模块的配置优先级如下（从高到低）：

1. 编程方式配置（通过API设置）
2. 环境变量配置（KNOWGO_AI_*）
3. 应用配置文件（application.yml/application.properties）
4. 默认配置（代码中定义的默认值）

## 13. 环境变量映射

配置项可以通过环境变量进行设置，环境变量名需要将配置项转换为大写，使用下划线分隔，并添加`KNOWGO_AI_`前缀。例如：

| 配置项 | 环境变量 |
|--------|----------|
| knowgo.ai.service.api-key | KNOWGO_AI_SERVICE_API_KEY |
| knowgo.ai.default.temperature | KNOWGO_AI_DEFAULT_TEMPERATURE |
| knowgo.ai.middleware.retry.enabled | KNOWGO_AI_MIDDLEWARE_RETRY_ENABLED |

## 14. 动态配置

KnowGo AI模块支持动态配置更新，可以通过以下方式实现：

1. 使用Spring Cloud Config等配置中心
2. 实现`AiConfigurationListener`接口监听配置变更
3. 通过API手动更新配置

```java
// 监听配置变更
@Bean
public AiConfigurationListener configurationListener() {
    return new AiConfigurationListener() {
        @Override
        public void onConfigurationChanged(AiConfiguration oldConfig, AiConfiguration newConfig) {
            // 处理配置变更
        }
    };
}
```

## 15. 配置验证

KnowGo AI模块提供了配置验证功能，可以在应用启动时验证配置的有效性：

```java
@Bean
public CommandLineRunner validateConfiguration(AiConfigurationValidator validator) {
    return args -> {
        ValidationResult result = validator.validate();
        if (!result.isValid()) {
            throw new IllegalStateException("Invalid AI configuration: " + result.getErrors());
        }
    };
}
```