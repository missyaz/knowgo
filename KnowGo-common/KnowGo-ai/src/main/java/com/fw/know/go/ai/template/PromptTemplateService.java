package com.fw.know.go.ai.template;

/**
 * @Description 提示词模板服务接口
 * @Date 24/11/2025 下午4:20
 * @Author Leo
 */
public interface PromptTemplateService {
    
    /**
     * 获取模板
     * @param templateName 模板名称
     * @return 模板内容
     */
    String getTemplate(String templateName);
    
    /**
     * 渲染模板
     * @param templateName 模板名称
     * @param parameters 参数
     * @return 渲染后的内容
     */
    String renderTemplate(String templateName, java.util.Map<String, Object> parameters);
    
    /**
     * 注册模板
     * @param templateName 模板名称
     * @param templateContent 模板内容
     */
    void registerTemplate(String templateName, String templateContent);
    
    /**
     * 删除模板
     * @param templateName 模板名称
     */
    void deleteTemplate(String templateName);
    
    /**
     * 检查模板是否存在
     * @param templateName 模板名称
     * @return 是否存在
     */
    boolean hasTemplate(String templateName);
    
    /**
     * 获取所有模板名称
     * @return 模板名称列表
     */
    java.util.List<String> getTemplateNames();
    
    /**
     * 重新加载模板
     */
    void reloadTemplates();
}