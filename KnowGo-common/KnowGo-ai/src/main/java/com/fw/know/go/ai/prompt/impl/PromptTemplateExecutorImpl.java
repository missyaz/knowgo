package com.fw.know.go.ai.prompt.impl;

import com.fw.know.go.ai.middleware.AIMiddleware;
import com.fw.know.go.ai.middleware.MiddlewareChain;
import com.fw.know.go.ai.middleware.MiddlewareChainConfig;
import com.fw.know.go.ai.prompt.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @Description 提示词模板执行器实现
 * @Date 24/11/2025 下午5:20
 * @Author Leo
 */
@Slf4j
@Component
public class PromptTemplateExecutorImpl implements PromptTemplateExecutor {
    
    @Autowired
    private PromptTemplateManager templateManager;
    
    @Autowired
    private MiddlewareChain middlewareChain;
    
    private final ExecutorService executorService = Executors.newFixedThreadPool(10);
    
    // 执行统计
    private final AtomicLong totalExecutions = new AtomicLong(0);
    private final AtomicLong successfulExecutions = new AtomicLong(0);
    private final AtomicLong failedExecutions = new AtomicLong(0);
    private final AtomicLong cachedExecutions = new AtomicLong(0);
    private final AtomicLong totalExecutionTime = new AtomicLong(0);
    
    // 执行状态跟踪
    private final Map<String, ExecutionStatus> executionStatuses = new ConcurrentHashMap<>();
    
    @Override
    public PromptExecutionResponse execute(PromptExecutionRequest request) {
        long startTime = System.currentTimeMillis();
        totalExecutions.incrementAndGet();
        
        try {
            // 验证请求
            if (!validateRequest(request)) {
                return PromptExecutionResponse.failure(
                        request.getRequestId(),
                        "Invalid request parameters",
                        "INVALID_REQUEST"
                );
            }
            
            // 设置执行状态
            executionStatuses.put(request.getRequestId(), ExecutionStatus.RUNNING);
            
            // 获取模板
            PromptTemplate template = getTemplate(request);
            if (template == null) {
                return PromptExecutionResponse.failure(
                        request.getRequestId(),
                        "Template not found",
                        "TEMPLATE_NOT_FOUND"
                );
            }
            
            // 渲染模板
            String renderedPrompt = renderTemplate(template, request);
            
            // 构建最终提示词
            String finalPrompt = buildFinalPrompt(renderedPrompt, request);
            
            // 执行模型调用（这里简化处理，实际应该调用AI模型）
            String modelResponse = executeModelCall(finalPrompt, request);
            
            // 构建成功响应
            PromptExecutionResponse response = PromptExecutionResponse.success(
                    request.getRequestId(),
                    finalPrompt,
                    modelResponse
            );
            
            // 填充响应详情
            response.setTemplateId(template.getId());
            response.setTemplateName(template.getName());
            response.setModelName(request.getModelName());
            response.setModelType(request.getModelType());
            response.setRenderedPrompt(finalPrompt);
            response.setSystemPrompt(request.getFinalSystemPrompt());
            response.setUserPrompt(request.getFinalUserPrompt());
            response.setAssistantPrompt(request.getFinalAssistantPrompt());
            response.setUsedParameters(request.getParameters());
            
            // 模拟令牌使用
            response.setTokenUsage(PromptExecutionResponse.TokenUsage.builder()
                    .promptTokens(finalPrompt.length() / 4) // 粗略估算
                    .completionTokens(modelResponse.length() / 4)
                    .totalTokens((finalPrompt.length() + modelResponse.length()) / 4)
                    .cost(0.001) // 模拟成本
                    .build());
            
            successfulExecutions.incrementAndGet();
            executionStatuses.put(request.getRequestId(), ExecutionStatus.COMPLETED);
            
            log.info("Template executed successfully: {} ({})", template.getName(), request.getRequestId());
            
            return response;
            
        } catch (Exception e) {
            failedExecutions.incrementAndGet();
            executionStatuses.put(request.getRequestId(), ExecutionStatus.FAILED);
            
            log.error("Template execution failed: " + request.getRequestId(), e);
            
            return PromptExecutionResponse.failure(
                    request.getRequestId(),
                    e.getMessage(),
                    "EXECUTION_ERROR"
            );
        } finally {
            long executionTime = System.currentTimeMillis() - startTime;
            totalExecutionTime.addAndGet(executionTime);
        }
    }
    
    @Override
    public CompletableFuture<PromptExecutionResponse> executeAsync(PromptExecutionRequest request) {
        return CompletableFuture.supplyAsync(() -> execute(request), executorService);
    }
    
    @Override
    public String executeAndRender(PromptExecutionRequest request) {
        PromptExecutionResponse response = execute(request);
        if (response.isSuccess()) {
            return response.getRenderedPrompt();
        } else {
            throw new RuntimeException("Template execution failed: " + response.getErrorMessage());
        }
    }
    
    @Override
    public Map<String, PromptExecutionResponse> executeBatch(Map<String, PromptExecutionRequest> requests) {
        Map<String, PromptExecutionResponse> responses = new ConcurrentHashMap<>();
        
        requests.entrySet().parallelStream().forEach(entry -> {
            try {
                PromptExecutionResponse response = execute(entry.getValue());
                responses.put(entry.getKey(), response);
            } catch (Exception e) {
                log.error("Batch execution failed for request: " + entry.getKey(), e);
                responses.put(entry.getKey(), PromptExecutionResponse.failure(
                        entry.getValue().getRequestId(),
                        e.getMessage(),
                        "BATCH_EXECUTION_ERROR"
                ));
            }
        });
        
        return responses;
    }
    
    @Override
    public boolean validateRequest(PromptExecutionRequest request) {
        if (request == null) {
            return false;
        }
        
        // 验证基本参数
        if (!request.validate()) {
            return false;
        }
        
        // 验证模板存在性
        PromptTemplate template = getTemplate(request);
        if (template == null) {
            return false;
        }
        
        // 验证模板参数
        if (request.getParameters() != null && !template.validateParameters(request.getParameters())) {
            return false;
        }
        
        return true;
    }
    
    @Override
    public ExecutionStatistics getExecutionStatistics() {
        ExecutionStatistics stats = new ExecutionStatistics();
        
        long total = totalExecutions.get();
        long successful = successfulExecutions.get();
        long failed = failedExecutions.get();
        long cached = cachedExecutions.get();
        long totalTime = totalExecutionTime.get();
        
        stats.setTotalExecutions(total);
        stats.setSuccessfulExecutions(successful);
        stats.setFailedExecutions(failed);
        stats.setCachedExecutions(cached);
        stats.setTotalExecutionTime(totalTime);
        
        if (total > 0) {
            stats.setSuccessRate((double) successful / total * 100);
            stats.setCacheHitRate((double) cached / total * 100);
            stats.setAverageExecutionTime(totalTime / total);
        }
        
        return stats;
    }
    
    @Override
    public boolean cancelExecution(String requestId) {
        ExecutionStatus currentStatus = executionStatuses.get(requestId);
        if (currentStatus == ExecutionStatus.RUNNING) {
            executionStatuses.put(requestId, ExecutionStatus.CANCELLED);
            return true;
        }
        return false;
    }
    
    @Override
    public ExecutionStatus getExecutionStatus(String requestId) {
        return executionStatuses.getOrDefault(requestId, ExecutionStatus.PENDING);
    }
    
    /**
     * 获取模板
     */
    private PromptTemplate getTemplate(PromptExecutionRequest request) {
        if (request.getTemplateId() != null) {
            return templateManager.getTemplate(request.getTemplateId()).orElse(null);
        } else if (request.getTemplateName() != null) {
            return templateManager.getTemplateByName(request.getTemplateName()).orElse(null);
        }
        return null;
    }
    
    /**
     * 渲染模板
     */
    private String renderTemplate(PromptTemplate template, PromptExecutionRequest request) {
        if (request.getParameters() != null) {
            return template.renderWithDefaults(request.getParameters());
        }
        return template.render(null);
    }
    
    /**
     * 构建最终提示词
     */
    private String buildFinalPrompt(String renderedPrompt, PromptExecutionRequest request) {
        StringBuilder finalPrompt = new StringBuilder();
        
        // 添加系统提示词
        String systemPrompt = request.getFinalSystemPrompt();
        if (systemPrompt != null && !systemPrompt.isEmpty()) {
            finalPrompt.append("System: ").append(systemPrompt).append("\n\n");
        }
        
        // 添加助手提示词
        String assistantPrompt = request.getFinalAssistantPrompt();
        if (assistantPrompt != null && !assistantPrompt.isEmpty()) {
            finalPrompt.append("Assistant: ").append(assistantPrompt).append("\n\n");
        }
        
        // 添加上下文
        if (request.getContext() != null && !request.getContext().isEmpty()) {
            finalPrompt.append("Context: ").append(request.getContext()).append("\n\n");
        }
        
        // 添加历史消息
        if (request.getMessageHistory() != null && !request.getMessageHistory().isEmpty()) {
            finalPrompt.append("History: ").append(request.getMessageHistory()).append("\n\n");
        }
        
        // 添加渲染后的模板内容
        finalPrompt.append(renderedPrompt);
        
        return finalPrompt.toString();
    }
    
    /**
     * 执行模型调用（模拟实现）
     */
    private String executeModelCall(String prompt, PromptExecutionRequest request) {
        // 这里应该调用实际的AI模型
        // 现在返回模拟响应
        
        try {
            // 模拟模型处理时间
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        return "This is a simulated response to: " + prompt.substring(0, Math.min(50, prompt.length())) + "...";
    }
}