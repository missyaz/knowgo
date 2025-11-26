package com.fw.know.go.ai.prompt;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @Description 提示词模板管理器
 * @Date 24/11/2025 下午5:15
 * @Author Leo
 */
public interface PromptTemplateManager {
    
    /**
     * 保存模板
     */
    PromptTemplate saveTemplate(PromptTemplate template);
    
    /**
     * 获取模板
     */
    Optional<PromptTemplate> getTemplate(String id);
    
    /**
     * 获取模板（按名称）
     */
    Optional<PromptTemplate> getTemplateByName(String name);
    
    /**
     * 获取所有模板
     */
    List<PromptTemplate> getAllTemplates();
    
    /**
     * 获取启用的模板
     */
    List<PromptTemplate> getEnabledTemplates();
    
    /**
     * 按分类获取模板
     */
    List<PromptTemplate> getTemplatesByCategory(String category);
    
    /**
     * 按类型获取模板
     */
    List<PromptTemplate> getTemplatesByType(PromptTemplate.TemplateType type);
    
    /**
     * 按标签获取模板
     */
    List<PromptTemplate> getTemplatesByTag(String tag);
    
    /**
     * 删除模板
     */
    boolean deleteTemplate(String id);
    
    /**
     * 启用模板
     */
    boolean enableTemplate(String id);
    
    /**
     * 禁用模板
     */
    boolean disableTemplate(String id);
    
    /**
     * 渲染模板
     */
    String renderTemplate(String templateId, Map<String, Object> parameters);
    
    /**
     * 渲染模板（按名称）
     */
    String renderTemplateByName(String templateName, Map<String, Object> parameters);
    
    /**
     * 验证模板参数
     */
    boolean validateTemplateParameters(String templateId, Map<String, Object> parameters);
    
    /**
     * 获取模板参数
     */
    Map<String, String> getTemplateParameters(String templateId);
    
    /**
     * 搜索模板
     */
    List<PromptTemplate> searchTemplates(String keyword);
    
    /**
     * 按模型类型获取模板
     */
    List<PromptTemplate> getTemplatesByModelType(String modelType);
    
    /**
     * 克隆模板
     */
    PromptTemplate cloneTemplate(String templateId, String newName);
    
    /**
     * 获取模板统计信息
     */
    TemplateStatistics getTemplateStatistics();
    
    /**
     * 更新模板使用次数
     */
    void incrementUsageCount(String templateId);
    
    /**
     * 更新模板评分
     */
    void updateTemplateRating(String templateId, double rating);
    
    /**
     * 获取热门模板
     */
    List<PromptTemplate> getPopularTemplates(int limit);
    
    /**
     * 获取高评分模板
     */
    List<PromptTemplate> getTopRatedTemplates(int limit);
    
    /**
     * 模板统计信息
     */
    class TemplateStatistics {
        private long totalTemplates;
        private long enabledTemplates;
        private long disabledTemplates;
        private Map<String, Long> templatesByCategory;
        private Map<String, Long> templatesByType;
        private Map<String, Long> templatesByModelType;
        private double averageRating;
        private long totalUsageCount;
        
        // Getters and setters
        public long getTotalTemplates() {
            return totalTemplates;
        }
        
        public void setTotalTemplates(long totalTemplates) {
            this.totalTemplates = totalTemplates;
        }
        
        public long getEnabledTemplates() {
            return enabledTemplates;
        }
        
        public void setEnabledTemplates(long enabledTemplates) {
            this.enabledTemplates = enabledTemplates;
        }
        
        public long getDisabledTemplates() {
            return disabledTemplates;
        }
        
        public void setDisabledTemplates(long disabledTemplates) {
            this.disabledTemplates = disabledTemplates;
        }
        
        public Map<String, Long> getTemplatesByCategory() {
            return templatesByCategory;
        }
        
        public void setTemplatesByCategory(Map<String, Long> templatesByCategory) {
            this.templatesByCategory = templatesByCategory;
        }
        
        public Map<String, Long> getTemplatesByType() {
            return templatesByType;
        }
        
        public void setTemplatesByType(Map<String, Long> templatesByType) {
            this.templatesByType = templatesByType;
        }
        
        public Map<String, Long> getTemplatesByModelType() {
            return templatesByModelType;
        }
        
        public void setTemplatesByModelType(Map<String, Long> templatesByModelType) {
            this.templatesByModelType = templatesByModelType;
        }
        
        public double getAverageRating() {
            return averageRating;
        }
        
        public void setAverageRating(double averageRating) {
            this.averageRating = averageRating;
        }
        
        public long getTotalUsageCount() {
            return totalUsageCount;
        }
        
        public void setTotalUsageCount(long totalUsageCount) {
            this.totalUsageCount = totalUsageCount;
        }
    }
}