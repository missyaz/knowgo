package com.fw.know.go.ai.prompt.impl;

import com.fw.know.go.ai.prompt.PromptTemplate;
import com.fw.know.go.ai.prompt.PromptTemplateManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @Description 提示词模板管理器实现
 * @Date 24/11/2025 下午5:15
 * @Author Leo
 */
@Slf4j
@Component
public class PromptTemplateManagerImpl implements PromptTemplateManager {
    
    private final Map<String, PromptTemplate> templates = new ConcurrentHashMap<>();
    private final Map<String, String> nameToIdMap = new ConcurrentHashMap<>();
    
    public PromptTemplateManagerImpl() {
        // 初始化默认模板
        initializeDefaultTemplates();
    }
    
    @Override
    public PromptTemplate saveTemplate(PromptTemplate template) {
        if (template == null) {
            throw new IllegalArgumentException("Template cannot be null");
        }
        
        // 生成ID（如果不存在）
        if (template.getId() == null || template.getId().isEmpty()) {
            template.setId(UUID.randomUUID().toString());
        }
        
        // 设置时间戳
        LocalDateTime now = LocalDateTime.now();
        if (template.getCreatedTime() == null) {
            template.setCreatedTime(now);
        }
        template.setUpdatedTime(now);
        
        // 设置默认值
        if (template.getEnabled() == null) {
            template.setEnabled(true);
        }
        if (template.getUsageCount() == null) {
            template.setUsageCount(0L);
        }
        if (template.getRating() == null) {
            template.setRating(0.0);
        }
        
        // 保存模板
        templates.put(template.getId(), template);
        nameToIdMap.put(template.getName(), template.getId());
        
        log.info("Template saved: {} ({})", template.getName(), template.getId());
        
        return template;
    }
    
    @Override
    public Optional<PromptTemplate> getTemplate(String id) {
        return Optional.ofNullable(templates.get(id));
    }
    
    @Override
    public Optional<PromptTemplate> getTemplateByName(String name) {
        String id = nameToIdMap.get(name);
        return id != null ? getTemplate(id) : Optional.empty();
    }
    
    @Override
    public List<PromptTemplate> getAllTemplates() {
        return new ArrayList<>(templates.values());
    }
    
    @Override
    public List<PromptTemplate> getEnabledTemplates() {
        return templates.values().stream()
                .filter(template -> Boolean.TRUE.equals(template.getEnabled()))
                .collect(Collectors.toList());
    }
    
    @Override
    public List<PromptTemplate> getTemplatesByCategory(String category) {
        return templates.values().stream()
                .filter(template -> category.equals(template.getCategory()))
                .collect(Collectors.toList());
    }
    
    @Override
    public List<PromptTemplate> getTemplatesByType(PromptTemplate.TemplateType type) {
        return templates.values().stream()
                .filter(template -> type.equals(template.getTemplateType()))
                .collect(Collectors.toList());
    }
    
    @Override
    public List<PromptTemplate> getTemplatesByTag(String tag) {
        return templates.values().stream()
                .filter(template -> template.getTags() != null && template.getTags().contains(tag))
                .collect(Collectors.toList());
    }
    
    @Override
    public boolean deleteTemplate(String id) {
        PromptTemplate template = templates.remove(id);
        if (template != null) {
            nameToIdMap.remove(template.getName());
            log.info("Template deleted: {} ({})", template.getName(), id);
            return true;
        }
        return false;
    }
    
    @Override
    public boolean enableTemplate(String id) {
        PromptTemplate template = templates.get(id);
        if (template != null) {
            template.setEnabled(true);
            template.setUpdatedTime(LocalDateTime.now());
            log.info("Template enabled: {} ({})", template.getName(), id);
            return true;
        }
        return false;
    }
    
    @Override
    public boolean disableTemplate(String id) {
        PromptTemplate template = templates.get(id);
        if (template != null) {
            template.setEnabled(false);
            template.setUpdatedTime(LocalDateTime.now());
            log.info("Template disabled: {} ({})", template.getName(), id);
            return true;
        }
        return false;
    }
    
    @Override
    public String renderTemplate(String templateId, Map<String, Object> parameters) {
        PromptTemplate template = templates.get(templateId);
        if (template == null) {
            throw new IllegalArgumentException("Template not found: " + templateId);
        }
        
        if (!template.getEnabled()) {
            throw new IllegalStateException("Template is disabled: " + templateId);
        }
        
        // 验证参数
        if (!template.validateParameters(parameters)) {
            throw new IllegalArgumentException("Invalid template parameters");
        }
        
        // 渲染模板
        String result = template.render(parameters);
        
        // 更新使用次数
        incrementUsageCount(templateId);
        
        log.debug("Template rendered: {} with {} parameters", template.getName(), 
                parameters != null ? parameters.size() : 0);
        
        return result;
    }
    
    @Override
    public String renderTemplateByName(String templateName, Map<String, Object> parameters) {
        Optional<PromptTemplate> template = getTemplateByName(templateName);
        if (template.isPresent()) {
            return renderTemplate(template.get().getId(), parameters);
        }
        throw new IllegalArgumentException("Template not found: " + templateName);
    }
    
    @Override
    public boolean validateTemplateParameters(String templateId, Map<String, Object> parameters) {
        PromptTemplate template = templates.get(templateId);
        return template != null && template.validateParameters(parameters);
    }
    
    @Override
    public Map<String, String> getTemplateParameters(String templateId) {
        PromptTemplate template = templates.get(templateId);
        return template != null ? template.getParameters() : null;
    }
    
    @Override
    public List<PromptTemplate> searchTemplates(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllTemplates();
        }
        
        String lowerKeyword = keyword.toLowerCase();
        
        return templates.values().stream()
                .filter(template -> 
                    (template.getName() != null && template.getName().toLowerCase().contains(lowerKeyword)) ||
                    (template.getDescription() != null && template.getDescription().toLowerCase().contains(lowerKeyword)) ||
                    (template.getTags() != null && template.getTags().toLowerCase().contains(lowerKeyword)) ||
                    (template.getCategory() != null && template.getCategory().toLowerCase().contains(lowerKeyword)) ||
                    (template.getContent() != null && template.getContent().toLowerCase().contains(lowerKeyword))
                )
                .collect(Collectors.toList());
    }
    
    @Override
    public List<PromptTemplate> getTemplatesByModelType(String modelType) {
        return templates.values().stream()
                .filter(template -> modelType.equals(template.getModelType()))
                .collect(Collectors.toList());
    }
    
    @Override
    public PromptTemplate cloneTemplate(String templateId, String newName) {
        PromptTemplate original = templates.get(templateId);
        if (original == null) {
            throw new IllegalArgumentException("Template not found: " + templateId);
        }
        
        PromptTemplate cloned = original.clone();
        cloned.setName(newName);
        cloned.setVersion(original.getVersion() + "-cloned");
        cloned.setCreatedTime(LocalDateTime.now());
        cloned.setUpdatedTime(LocalDateTime.now());
        cloned.setUsageCount(0L);
        cloned.setRating(0.0);
        
        return saveTemplate(cloned);
    }
    
    @Override
    public TemplateStatistics getTemplateStatistics() {
        TemplateStatistics stats = new TemplateStatistics();
        
        List<PromptTemplate> allTemplates = getAllTemplates();
        List<PromptTemplate> enabledTemplates = getEnabledTemplates();
        
        stats.setTotalTemplates(allTemplates.size());
        stats.setEnabledTemplates(enabledTemplates.size());
        stats.setDisabledTemplates(allTemplates.size() - enabledTemplates.size());
        
        // 按分类统计
        Map<String, Long> byCategory = allTemplates.stream()
                .collect(Collectors.groupingBy(
                        template -> template.getCategory() != null ? template.getCategory() : "uncategorized",
                        Collectors.counting()
                ));
        stats.setTemplatesByCategory(byCategory);
        
        // 按类型统计
        Map<String, Long> byType = allTemplates.stream()
                .collect(Collectors.groupingBy(
                        template -> template.getTemplateType() != null ? template.getTemplateType().name() : "unknown",
                        Collectors.counting()
                ));
        stats.setTemplatesByType(byType);
        
        // 按模型类型统计
        Map<String, Long> byModelType = allTemplates.stream()
                .collect(Collectors.groupingBy(
                        template -> template.getModelType() != null ? template.getModelType() : "unknown",
                        Collectors.counting()
                ));
        stats.setTemplatesByModelType(byModelType);
        
        // 平均评分
        double avgRating = allTemplates.stream()
                .mapToDouble(template -> template.getRating() != null ? template.getRating() : 0.0)
                .average()
                .orElse(0.0);
        stats.setAverageRating(avgRating);
        
        // 总使用次数
        long totalUsage = allTemplates.stream()
                .mapToLong(template -> template.getUsageCount() != null ? template.getUsageCount() : 0L)
                .sum();
        stats.setTotalUsageCount(totalUsage);
        
        return stats;
    }
    
    @Override
    public void incrementUsageCount(String templateId) {
        PromptTemplate template = templates.get(templateId);
        if (template != null) {
            template.setUsageCount((template.getUsageCount() != null ? template.getUsageCount() : 0L) + 1);
        }
    }
    
    @Override
    public void updateTemplateRating(String templateId, double rating) {
        PromptTemplate template = templates.get(templateId);
        if (template != null) {
            double currentRating = template.getRating() != null ? template.getRating() : 0.0;
            long usageCount = template.getUsageCount() != null ? template.getUsageCount() : 0L;
            
            // 计算新的平均评分
            double newRating = (currentRating * usageCount + rating) / (usageCount + 1);
            template.setRating(newRating);
        }
    }
    
    @Override
    public List<PromptTemplate> getPopularTemplates(int limit) {
        return templates.values().stream()
                .sorted((a, b) -> {
                    Long aCount = a.getUsageCount() != null ? a.getUsageCount() : 0L;
                    Long bCount = b.getUsageCount() != null ? b.getUsageCount() : 0L;
                    return bCount.compareTo(aCount); // 降序
                })
                .limit(limit)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<PromptTemplate> getTopRatedTemplates(int limit) {
        return templates.values().stream()
                .filter(template -> template.getRating() != null && template.getRating() > 0)
                .sorted((a, b) -> {
                    Double aRating = a.getRating();
                    Double bRating = b.getRating();
                    return bRating.compareTo(aRating); // 降序
                })
                .limit(limit)
                .collect(Collectors.toList());
    }
    
    /**
     * 初始化默认模板
     */
    private void initializeDefaultTemplates() {
        // 系统提示词模板
        saveTemplate(PromptTemplate.builder()
                .name("system-default")
                .description("默认系统提示词")
                .content("You are a helpful AI assistant. Please provide accurate and helpful responses.")
                .category("system")
                .tags("system,default,assistant")
                .templateType(PromptTemplate.TemplateType.SYSTEM)
                .version("1.0")
                .enabled(true)
                .createdBy("system")
                .build());
        
        // 代码生成模板
        saveTemplate(PromptTemplate.builder()
                .name("code-generator")
                .description("代码生成模板")
                .content("Please generate {language} code for the following requirement: {requirement}\n\nRequirements:\n{requirements}\n\nAdditional constraints:\n{constraints}")
                .category("technical")
                .tags("code,generation,programming")
                .templateType(PromptTemplate.TemplateType.INSTRUCTION)
                .parameters(Map.of(
                        "language", "Java",
                        "requirement", "",
                        "requirements", "",
                        "constraints", ""
                ))
                .version("1.0")
                .enabled(true)
                .createdBy("system")
                .build());
        
        // 翻译模板
        saveTemplate(PromptTemplate.builder()
                .name("translator")
                .description("翻译模板")
                .content("Please translate the following text from {sourceLanguage} to {targetLanguage}:\n\n{text}\n\nContext: {context}")
                .category("business")
                .tags("translation,language")
                .templateType(PromptTemplate.TemplateType.INSTRUCTION)
                .parameters(Map.of(
                        "sourceLanguage", "English",
                        "targetLanguage", "Chinese",
                        "text", "",
                        "context", ""
                ))
                .version("1.0")
                .enabled(true)
                .createdBy("system")
                .build());
        
        // 创意写作模板
        saveTemplate(PromptTemplate.builder()
                .name("creative-writer")
                .description("创意写作模板")
                .content("Please write a {type} about {topic} in {style} style.\n\nRequirements:\n{requirements}\n\nLength: {length}\n\nTarget audience: {audience}")
                .category("creative")
                .tags("writing,creative,content")
                .templateType(PromptTemplate.TemplateType.CREATIVE)
                .parameters(Map.of(
                        "type", "story",
                        "topic", "",
                        "style", "casual",
                        "requirements", "",
                        "length", "500 words",
                        "audience", "general"
                ))
                .version("1.0")
                .enabled(true)
                .createdBy("system")
                .build());
        
        // 问答模板
        saveTemplate(PromptTemplate.builder()
                .name("qa-assistant")
                .description("问答助手模板")
                .content("Question: {question}\n\nContext: {context}\n\nPlease provide a comprehensive and accurate answer.")
                .category("business")
                .tags("qa,question,answer,knowledge")
                .templateType(PromptTemplate.TemplateType.CHAT)
                .parameters(Map.of(
                        "question", "",
                        "context", ""
                ))
                .version("1.0")
                .enabled(true)
                .createdBy("system")
                .build());
        
        log.info("Default templates initialized. Total templates: {}", templates.size());
    }
}