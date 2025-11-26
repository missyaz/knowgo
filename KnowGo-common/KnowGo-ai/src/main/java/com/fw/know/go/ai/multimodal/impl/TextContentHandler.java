package com.fw.know.go.ai.multimodal.impl;

import com.fw.know.go.ai.multimodal.MultimodalContent;
import com.fw.know.go.ai.multimodal.MultimodalContentHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;

/**
 * @Description 文本内容处理器
 * @Date 24/11/2025 下午5:02
 * @Author Leo
 */
@Slf4j
public class TextContentHandler implements MultimodalContentHandler {
    
    private static final List<String> SUPPORTED_FORMATS = Arrays.asList(
            "txt", "md", "html", "xml", "json", "csv", "pdf", "doc", "docx"
    );
    
    @Override
    public String getType() {
        return "text";
    }
    
    @Override
    public List<String> getSupportedFormats() {
        return SUPPORTED_FORMATS;
    }
    
    @Override
    public boolean validateContent(MultimodalContent content) {
        if (content == null || !"text".equals(content.getType())) {
            return false;
        }
        
        // 检查是否有数据
        if (content.getData() == null && content.getUrl() == null && content.getFilePath() == null) {
            return false;
        }
        
        // 检查格式
        String format = content.getFormat();
        if (format != null && !SUPPORTED_FORMATS.contains(format.toLowerCase())) {
            return false;
        }
        
        return true;
    }
    
    @Override
    public MultimodalContent preprocessContent(MultimodalContent content) {
        if (content.getData() != null) {
            // 文本清理和标准化
            String cleanedText = cleanText(content.getData());
            content.setData(cleanedText);
        }
        
        // 设置默认格式
        if (content.getFormat() == null) {
            content.setFormat("txt");
        }
        
        // 计算大小
        if (content.getSize() == null && content.getData() != null) {
            content.setSize((long) content.getData().getBytes().length);
        }
        
        return content;
    }
    
    @Override
    public MultimodalContent postprocessContent(MultimodalContent content) {
        // 后处理逻辑
        if (content.getMetadata() == null) {
            content.setMetadata(new java.util.HashMap<>());
        }
        
        // 添加文本统计信息
        if (content.getData() != null) {
            String text = content.getData();
            content.getMetadata().put("charCount", text.length());
            content.getMetadata().put("wordCount", countWords(text));
            content.getMetadata().put("lineCount", countLines(text));
        }
        
        return content;
    }
    
    @Override
    public long getContentSize(MultimodalContent content) {
        if (content.getSize() != null) {
            return content.getSize();
        }
        
        if (content.getData() != null) {
            return content.getData().getBytes().length;
        }
        
        return 0;
    }
    
    @Override
    public boolean isEmpty(MultimodalContent content) {
        if (content == null || content.getData() == null) {
            return true;
        }
        
        return content.getData().trim().isEmpty();
    }
    
    @Override
    public MultimodalContent convertFormat(MultimodalContent content, String targetFormat) {
        if (!SUPPORTED_FORMATS.contains(targetFormat.toLowerCase())) {
            log.warn("Unsupported target format: {}", targetFormat);
            return content;
        }
        
        // 简单的格式转换逻辑
        MultimodalContent convertedContent = MultimodalContent.builder()
                .type(content.getType())
                .format(targetFormat)
                .data(content.getData())
                .description(content.getDescription())
                .metadata(content.getMetadata())
                .createdTime(content.getCreatedTime())
                .enabled(content.getEnabled())
                .language(content.getLanguage())
                .encoding(content.getEncoding())
                .build();
        
        return convertedContent;
    }
    
    @Override
    public int getPriority() {
        return 100;
    }
    
    /**
     * 清理文本
     */
    private String cleanText(String text) {
        if (text == null) {
            return "";
        }
        
        // 移除多余的空白字符
        text = text.replaceAll("\\s+", " ");
        text = text.trim();
        
        // 移除特殊字符（根据需要调整）
        text = text.replaceAll("[^\\p{Print}\\p{Space}]", "");
        
        return text;
    }
    
    /**
     * 统计单词数
     */
    private int countWords(String text) {
        if (text == null || text.trim().isEmpty()) {
            return 0;
        }
        
        return text.trim().split("\\s+").length;
    }
    
    /**
     * 统计行数
     */
    private int countLines(String text) {
        if (text == null || text.isEmpty()) {
            return 0;
        }
        
        return text.split("\\n").length;
    }
}