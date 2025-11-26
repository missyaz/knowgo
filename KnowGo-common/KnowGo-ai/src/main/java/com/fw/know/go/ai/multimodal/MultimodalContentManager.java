package com.fw.know.go.ai.multimodal;

/**
 * @Description 多模态内容管理器
 * @Date 24/11/2025 下午4:59
 * @Author Leo
 */
public interface MultimodalContentManager {
    
    /**
     * 注册内容处理器
     * @param handler 处理器
     */
    void registerHandler(MultimodalContentHandler handler);
    
    /**
     * 移除内容处理器
     * @param type 类型
     */
    void removeHandler(String type);
    
    /**
     * 获取内容处理器
     * @param type 类型
     * @return 处理器
     */
    MultimodalContentHandler getHandler(String type);
    
    /**
     * 处理内容
     * @param content 内容
     * @return 处理后的内容
     */
    MultimodalContent processContent(MultimodalContent content);
    
    /**
     * 验证内容
     * @param content 内容
     * @return 是否有效
     */
    boolean validateContent(MultimodalContent content);
    
    /**
     * 转换内容格式
     * @param content 内容
     * @param targetFormat 目标格式
     * @return 转换后的内容
     */
    MultimodalContent convertContent(MultimodalContent content, String targetFormat);
    
    /**
     * 获取内容信息
     * @param content 内容
     * @return 内容信息
     */
    ContentInfo getContentInfo(MultimodalContent content);
    
    /**
     * 压缩内容
     * @param content 内容
     * @return 压缩后的内容
     */
    MultimodalContent compressContent(MultimodalContent content);
    
    /**
     * 解压缩内容
     * @param content 内容
     * @return 解压缩后的内容
     */
    MultimodalContent decompressContent(MultimodalContent content);
    
    /**
     * 获取支持的内容类型
     * @return 类型列表
     */
    java.util.List<String> getSupportedTypes();
    
    /**
     * 获取支持的格式
     * @param type 类型
     * @return 格式列表
     */
    java.util.List<String> getSupportedFormats(String type);
    
    /**
     * 内容信息
     */
    @lombok.Data
    @lombok.Builder
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    class ContentInfo {
        /**
         * 内容类型
         */
        private String type;
        
        /**
         * 内容格式
         */
        private String format;
        
        /**
         * 内容大小
         */
        private Long size;
        
        /**
         * 是否有效
         */
        private Boolean valid;
        
        /**
         * 验证消息
         */
        private String validationMessage;
        
        /**
         * 处理建议
         */
        private String processingSuggestion;
        
        /**
         * 元数据
         */
        private java.util.Map<String, Object> metadata;
    }
}