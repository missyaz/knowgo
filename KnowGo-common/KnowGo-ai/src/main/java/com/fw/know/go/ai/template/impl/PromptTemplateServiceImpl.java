package com.fw.know.go.ai.template.impl;

import com.fw.know.go.ai.template.PromptTemplate;
import com.fw.know.go.ai.template.PromptTemplateService;
import lombok.extern.slf4j.Slf4j;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.StringResourceLoader;
import org.apache.velocity.runtime.resource.util.StringResourceRepository;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description 提示词模板服务实现
 * @Date 24/11/2025 下午4:25
 * @Author Leo
 */
@Slf4j
@Service
public class PromptTemplateServiceImpl implements PromptTemplateService {
    
    private final Map<String, PromptTemplate> templates = new ConcurrentHashMap<>();
    private VelocityEngine velocityEngine;
    private StringResourceRepository resourceRepository;
    
    @PostConstruct
    public void init() {
        // 初始化Velocity引擎
        velocityEngine = new VelocityEngine();
        velocityEngine.setProperty(RuntimeConstants.RESOURCE_LOADER, "string");
        velocityEngine.setProperty("string.resource.loader.class", StringResourceLoader.class.getName());
        velocityEngine.setProperty("string.resource.loader.repository.name", "promptTemplateRepository");
        velocityEngine.setProperty(RuntimeConstants.RUNTIME_LOG_LOGSYSTEM_CLASS, "org.apache.velocity.runtime.log.Log4JLogChute");
        velocityEngine.setProperty("runtime.log.logsystem.log4j.logger", "velocity");
        
        try {
            velocityEngine.init();
            resourceRepository = StringResourceLoader.getRepository("promptTemplateRepository");
            log.info("PromptTemplateService initialized successfully");
        } catch (Exception e) {
            log.error("Failed to initialize PromptTemplateService", e);
            throw new RuntimeException("Failed to initialize PromptTemplateService", e);
        }
        
        // 加载默认模板
        loadDefaultTemplates();
    }
    
    @Override
    public String getTemplate(String templateName) {
        PromptTemplate template = templates.get(templateName);
        return template != null ? template.getContent() : null;
    }
    
    @Override
    public String renderTemplate(String templateName, Map<String, Object> parameters) {
        PromptTemplate template = templates.get(templateName);
        if (template == null) {
            throw new IllegalArgumentException("Template not found: " + templateName);
        }
        
        if (!template.getEnabled()) {
            throw new IllegalStateException("Template is disabled: " + templateName);
        }
        
        try {
            // 创建Velocity上下文
            VelocityContext context = new VelocityContext();
            if (parameters != null) {
                parameters.forEach(context::put);
            }
            
            // 渲染模板
            StringWriter writer = new StringWriter();
            Template velocityTemplate = velocityEngine.getTemplate(templateName);
            velocityTemplate.merge(context, writer);
            
            return writer.toString();
        } catch (Exception e) {
            log.error("Failed to render template: {}", templateName, e);
            throw new RuntimeException("Failed to render template: " + templateName, e);
        }
    }
    
    @Override
    public void registerTemplate(String templateName, String templateContent) {
        registerTemplate(templateName, templateContent, "velocity", "Default template");
    }
    
    @Override
    public void deleteTemplate(String templateName) {
        templates.remove(templateName);
        if (resourceRepository != null) {
            resourceRepository.removeStringResource(templateName);
        }
        log.info("Template deleted: {}", templateName);
    }
    
    @Override
    public boolean hasTemplate(String templateName) {
        return templates.containsKey(templateName);
    }
    
    @Override
    public List<String> getTemplateNames() {
        return List.copyOf(templates.keySet());
    }
    
    @Override
    public void reloadTemplates() {
        templates.clear();
        if (resourceRepository != null) {
            resourceRepository.removeStringResource("*");
        }
        loadDefaultTemplates();
        log.info("Templates reloaded");
    }
    
    /**
     * 注册模板（完整信息）
     */
    public void registerTemplate(String templateName, String templateContent, String templateType, String description) {
        PromptTemplate template = PromptTemplate.builder()
                .name(templateName)
                .content(templateContent)
                .description(description)
                .templateType(templateType)
                .version("1.0")
                .enabled(true)
                .createdTime(System.currentTimeMillis())
                .updatedTime(System.currentTimeMillis())
                .build();
        
        templates.put(templateName, template);
        
        // 注册到Velocity
        if (resourceRepository != null) {
            resourceRepository.putStringResource(templateName, templateContent);
        }
        
        log.info("Template registered: {}", templateName);
    }
    
    /**
     * 加载默认模板
     */
    private void loadDefaultTemplates() {
        // RAG问答模板
        registerTemplate("rag_qa", 
            "你是一个智能助手，请基于提供的上下文信息回答问题。\n\n" +
            "上下文信息：\n$context\n\n" +
            "用户问题：$question\n\n" +
            "请提供准确、简洁的回答。如果上下文信息不足以回答问题，请明确说明。",
            "velocity", "RAG问答模板");
        
        // 文档总结模板
        registerTemplate("document_summary",
            "请对以下文档内容进行总结：\n\n$document_content\n\n" +
            "要求：\n" +
            "1. 提取关键信息\n" +
            "2. 总结主要观点\n" +
            "3. 保持简洁明了\n\n" +
            "总结：",
            "velocity", "文档总结模板");
        
        // 代码解释模板
        registerTemplate("code_explanation",
            "请解释以下代码的功能和工作原理：\n\n" +
            "```$language\n$code\n```\n\n" +
            "请提供：\n" +
            "1. 代码功能概述\n" +
            "2. 关键逻辑解释\n" +
            "3. 可能的改进建议\n\n" +
            "解释：",
            "velocity", "代码解释模板");
        
        // 智能客服模板
        registerTemplate("customer_service",
            "你是客服助手，请友好地回答客户问题。\n\n" +
            "客户问题：$customer_question\n\n" +
            "相关文档信息：$related_docs\n\n" +
            "请基于提供的信息，以专业、友好的语气回答客户问题。" +
            "如果信息不足，请礼貌地说明并引导客户提供更多信息。",
            "velocity", "智能客服模板");
        
        log.info("Default templates loaded");
    }
}