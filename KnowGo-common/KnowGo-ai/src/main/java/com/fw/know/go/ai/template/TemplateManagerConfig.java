package com.fw.know.go.ai.template;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * @Description 模板管理器配置
 * @Date 24/11/2025 下午4:30
 * @Author Leo
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TemplateManagerConfig {
    
    /**
     * 模板存储路径
     */
    private String templateStoragePath;
    
    /**
     * 模板文件扩展名
     */
    private String templateFileExtension;
    
    /**
     * 是否启用缓存
     */
    private Boolean enableCache;
    
    /**
     * 缓存过期时间（秒）
     */
    private Long cacheExpirationSeconds;
    
    /**
     * 模板扫描间隔（秒）
     */
    private Long templateScanIntervalSeconds;
    
    /**
     * 支持的模板类型
     */
    private List<String> supportedTemplateTypes;
    
    /**
     * 默认模板配置
     */
    private Map<String, DefaultTemplateConfig> defaultTemplates;
    
    /**
     * 验证配置
     */
    private ValidationConfig validation;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DefaultTemplateConfig {
        /**
         * 模板内容
         */
        private String content;
        
        /**
         * 模板描述
         */
        private String description;
        
        /**
         * 模板类型
         */
        private String templateType;
        
        /**
         * 是否启用
         */
        private Boolean enabled;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ValidationConfig {
        /**
         * 是否启用参数验证
         */
        private Boolean enableParameterValidation;
        
        /**
         * 最大模板大小
         */
        private Long maxTemplateSize;
        
        /**
         * 最大参数数量
         */
        private Integer maxParameterCount;
        
        /**
         * 参数名称正则表达式
         */
        private String parameterNamePattern;
        
        /**
         * 是否启用模板语法检查
         */
        private Boolean enableSyntaxCheck;
    }
}