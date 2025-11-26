package com.fw.know.go.ai.prompt;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * @Description 提示词模板
 * @Date 24/11/2025 下午5:15
 * @Author Leo
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PromptTemplate {
    
    /**
     * 模板ID
     */
    private String id;
    
    /**
     * 模板名称
     */
    private String name;
    
    /**
     * 模板描述
     */
    private String description;
    
    
    /**
     * 模板内容
     */
    private String content;
    
    /**
     * 模板参数
     */
    private Map<String, String> parameters;
    
    /**
     * 模板标签
     */
    private String tags;
    
    /**
     * 模板分类
     */
    private String category;
    
    /**
     * 模板版本
     */
    private String version;
    
    /**
     * 是否启用
     */
    private Boolean enabled;
    
    /**
     * 创建时间
     */
    private LocalDateTime createdTime;
    
    /**
     * 更新时间
     */
    private LocalDateTime updatedTime;
    
    /**
     * 创建人
     */
    private String createdBy;
    
    /**
     * 更新人
     */
    private String updatedBy;
    
    /**
     * 模板类型
     */
    private TemplateType templateType;
    
    /**
     * 模型类型
     */
    private String modelType;
    
    /**
     * 系统提示词
     */
    private String systemPrompt;
    
    /**
     * 用户提示词
     */
    private String userPrompt;
    
    /**
     * 助手提示词
     */
    private String assistantPrompt;
    
    /**
     * 示例输入
     */
    private String exampleInput;
    
    /**
     * 示例输出
     */
    private String exampleOutput;
    
    /**
     * 使用次数
     */
    private Long usageCount;
    
    /**
     * 评分
     */
    private Double rating;
    
    /**
     * 模板类型枚举
     */
    public enum TemplateType {
        SYSTEM,     // 系统提示词
        USER,       // 用户提示词
        ASSISTANT,  // 助手提示词
        CHAT,       // 对话模板
        COMPLETION, // 补全模板
        INSTRUCTION, // 指令模板
        ROLE_PLAY,   // 角色扮演模板
        CREATIVE,    // 创意模板
        TECHNICAL,   // 技术模板
        BUSINESS     // 业务模板
    }
    
    /**
     * 渲染模板
     */
    public String render(Map<String, Object> params) {
        String result = this.content;
        
        if (params != null && !params.isEmpty()) {
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                String placeholder = "{" + entry.getKey() + "}";
                String value = entry.getValue() != null ? entry.getValue().toString() : "";
                result = result.replace(placeholder, value);
            }
        }
        
        return result;
    }
    
    /**
     * 渲染模板（带默认值）
     */
    public String renderWithDefaults(Map<String, Object> params) {
        Map<String, Object> mergedParams = new HashMap<>();
        
        // 先添加模板参数的默认值
        if (this.parameters != null) {
            mergedParams.putAll(this.parameters);
        }
        
        // 再添加传入的参数（覆盖默认值）
        if (params != null) {
            mergedParams.putAll(params);
        }
        
        return render(mergedParams);
    }
    
    /**
     * 获取模板参数
     */
    public Set<String> getTemplateParameters() {
        Set<String> params = new HashSet<>();
        
        if (this.content != null) {
            // 简单的参数提取，查找 {parameterName} 格式的占位符
            int start = 0;
            while (start < content.length()) {
                int openBrace = content.indexOf('{', start);
                if (openBrace == -1) break;
                
                int closeBrace = content.indexOf('}', openBrace);
                if (closeBrace == -1) break;
                
                String paramName = content.substring(openBrace + 1, closeBrace);
                if (!paramName.isEmpty() && !paramName.contains("{") && !paramName.contains("}")) {
                    params.add(paramName);
                }
                
                start = closeBrace + 1;
            }
        }
        
        return params;
    }
    
    /**
     * 验证模板参数
     */
    public boolean validateParameters(Map<String, Object> params) {
        if (params == null) {
            return true;
        }
        
        Set<String> requiredParams = getTemplateParameters();
        
        for (String requiredParam : requiredParams) {
            if (!params.containsKey(requiredParam) || params.get(requiredParam) == null) {
                return false;
            }
        }
        
        return true;
    }
    
    /**
     * 克隆模板
     */
    public PromptTemplate clone() {
        return PromptTemplate.builder()
                .name(this.name)
                .description(this.description)
                .content(this.content)
                .parameters(this.parameters != null ? new HashMap<>(this.parameters) : null)
                .tags(this.tags)
                .category(this.category)
                .version(this.version)
                .enabled(this.enabled)
                .templateType(this.templateType)
                .modelType(this.modelType)
                .systemPrompt(this.systemPrompt)
                .userPrompt(this.userPrompt)
                .assistantPrompt(this.assistantPrompt)
                .exampleInput(this.exampleInput)
                .exampleOutput(this.exampleOutput)
                .createdTime(LocalDateTime.now())
                .updatedTime(LocalDateTime.now())
                .build();
    }
}