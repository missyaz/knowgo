package com.fw.know.go.ai.multimodal;

/**
 * @Description 多模态内容处理器接口
 * @Date 24/11/2025 下午4:55
 * @Author Leo
 */
public interface MultimodalContentHandler {
    
    /**
     * 获取处理器类型
     * @return 类型（text, image, audio, video）
     */
    String getType();
    
    /**
     * 获取支持的格式
     * @return 格式列表
     */
    java.util.List<String> getSupportedFormats();
    
    /**
     * 验证内容
     * @param content 内容
     * @return 是否有效
     */
    boolean validateContent(MultimodalContent content);
    
    /**
     * 预处理内容
     * @param content 内容
     * @return 处理后的内容
     */
    MultimodalContent preprocessContent(MultimodalContent content);
    
    /**
     * 后处理内容
     * @param content 内容
     * @return 处理后的内容
     */
    MultimodalContent postprocessContent(MultimodalContent content);
    
    /**
     * 获取内容大小
     * @param content 内容
     * @return 大小（字节）
     */
    long getContentSize(MultimodalContent content);
    
    /**
     * 检查内容是否为空
     * @param content 内容
     * @return 是否为空
     */
    boolean isEmpty(MultimodalContent content);
    
    /**
     * 转换内容格式
     * @param content 内容
     * @param targetFormat 目标格式
     * @return 转换后的内容
     */
    MultimodalContent convertFormat(MultimodalContent content, String targetFormat);
    
    /**
     * 获取处理器优先级
     * @return 优先级
     */
    int getPriority();
}