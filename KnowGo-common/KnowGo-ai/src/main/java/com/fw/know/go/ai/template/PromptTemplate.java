package com.fw.know.go.ai.template;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @Description 提示词模板
 * @Date 24/11/2025 下午4:22
 * @Author Leo
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PromptTemplate {
    
    /**
     * 模板名称
     */
    private String name;
    
    /**
     * 模板内容
     */
    private String content;
    
    /**
     * 模板描述
     */
    private String description;
    
    /**
     * 模板版本
     */
    private String version;
    
    /**
     * 模板参数
     */
    private Map<String, Parameter> parameters;
    
    /**
     * 模板类型（velocity, freemarker, mustache）
     */
    private String templateType;
    
    /**
     * 创建时间
     */
    private Long createdTime;
    
    /**
     * 更新时间
     */
    private Long updatedTime;
    
    /**
     * 是否启用
     */
    private Boolean enabled;
    
    /**
     * 标签
     */
    private java.util.List<String> tags;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Parameter {
        /**
         * 参数名称
         */
        private String name;
        
        /**
         * 参数类型
         */
        private String type;
        
        /**
         * 参数描述
         */
        private String description;
        
        /**
         * 是否必需
         */
        private Boolean required;
        
        /**
         * 默认值
         */
        private Object defaultValue;
        
        /**
         * 验证规则
         */
        private String validationRule;
    }
}