package com.fw.know.go.ai.multimodal.impl;

import com.fw.know.go.ai.multimodal.MultimodalContent;
import com.fw.know.go.ai.multimodal.MultimodalContentHandler;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @Description 文件内容处理器
 * @Date 24/11/2025 下午5:08
 * @Author Leo
 */
@Slf4j
public class FileContentHandler implements MultimodalContentHandler {
    
    private static final List<String> SUPPORTED_FORMATS = Arrays.asList(
            "txt", "md", "html", "xml", "json", "csv", "log", "conf", "properties", "yaml", "yml"
    );
    
    private static final long MAX_FILE_SIZE = 50 * 1024 * 1024; // 50MB
    
    private static final Map<String, String> MIME_TYPES = new HashMap<>();
    
    static {
        MIME_TYPES.put("txt", "text/plain");
        MIME_TYPES.put("md", "text/markdown");
        MIME_TYPES.put("html", "text/html");
        MIME_TYPES.put("xml", "text/xml");
        MIME_TYPES.put("json", "application/json");
        MIME_TYPES.put("csv", "text/csv");
        MIME_TYPES.put("log", "text/plain");
        MIME_TYPES.put("conf", "text/plain");
        MIME_TYPES.put("properties", "text/plain");
        MIME_TYPES.put("yaml", "text/yaml");
        MIME_TYPES.put("yml", "text/yaml");
    }
    
    @Override
    public String getType() {
        return "file";
    }
    
    @Override
    public List<String> getSupportedFormats() {
        return SUPPORTED_FORMATS;
    }
    
    @Override
    public boolean validateContent(MultimodalContent content) {
        if (content == null || !"file".equals(content.getType())) {
            return false;
        }
        
        // 检查格式
        String format = content.getFormat();
        if (format != null && !SUPPORTED_FORMATS.contains(format.toLowerCase())) {
            return false;
        }
        
        // 检查是否有数据
        if (content.getData() == null && content.getUrl() == null && content.getFilePath() == null) {
            return false;
        }
        
        // 检查大小
        if (content.getSize() != null && content.getSize() > MAX_FILE_SIZE) {
            return false;
        }
        
        return true;
    }
    
    @Override
    public MultimodalContent preprocessContent(MultimodalContent content) {
        // 验证base64数据
        if (content.getData() != null) {
            try {
                // 检查是否是有效的base64
                Base64.getDecoder().decode(content.getData());
            } catch (IllegalArgumentException e) {
                log.warn("Invalid base64 file data");
                content.setData(null);
            }
        }
        
        // 设置默认格式
        if (content.getFormat() == null) {
            content.setFormat("txt");
        }
        
        // 估算大小
        if (content.getSize() == null && content.getData() != null) {
            // base64数据大约是原始数据的4/3
            long estimatedSize = (long) (content.getData().length() * 0.75);
            content.setSize(estimatedSize);
        }
        
        // 设置MIME类型
        if (content.getMimeType() == null) {
            content.setMimeType(MIME_TYPES.getOrDefault(content.getFormat().toLowerCase(), "text/plain"));
        }
        
        return content;
    }
    
    @Override
    public MultimodalContent postprocessContent(MultodalContent content) {
        // 添加文件元数据
        if (content.getMetadata() == null) {
            content.setMetadata(new HashMap<>());
        }
        
        // 添加基本属性
        content.getMetadata().put("type", "file");
        content.getMetadata().put("format", content.getFormat());
        content.getMetadata().put("mimeType", content.getMimeType());
        
        if (content.getSize() != null) {
            content.getMetadata().put("sizeInKB", content.getSize() / 1024);
            content.getMetadata().put("sizeInMB", content.getSize() / (1024.0 * 1024.0));
        }
        
        // 统计行数、单词数、字符数
        if (content.getData() != null) {
            try {
                String textContent = new String(Base64.getDecoder().decode(content.getData()), "UTF-8");
                
                // 行数
                long lineCount = textContent.lines().count();
                content.getMetadata().put("lineCount", lineCount);
                
                // 单词数
                long wordCount = Arrays.stream(textContent.split("\\s+"))
                        .filter(word -> word.length() > 0)
                        .count();
                content.getMetadata().put("wordCount", wordCount);
                
                // 字符数
                long charCount = textContent.length();
                content.getMetadata().put("charCount", charCount);
                
                // 非空白字符数
                long nonWhitespaceCharCount = textContent.replaceAll("\\s+", "").length();
                content.getMetadata().put("nonWhitespaceCharCount", nonWhitespaceCharCount);
                
            } catch (Exception e) {
                log.warn("Failed to analyze file content", e);
            }
        }
        
        return content;
    }
    
    @Override
    public long getContentSize(MultimodalContent content) {
        if (content.getSize() != null) {
            return content.getSize();
        }
        
        if (content.getData() != null) {
            // base64数据大约是原始数据的4/3
            return (long) (content.getData().length() * 0.75);
        }
        
        return 0;
    }
    
    @Override
    public boolean isEmpty(MultimodalContent content) {
        if (content == null) {
            return true;
        }
        
        // 检查是否有有效数据
        return (content.getData() == null || content.getData().isEmpty()) &&
               (content.getUrl() == null || content.getUrl().isEmpty()) &&
               (content.getFilePath() == null || content.getFilePath().isEmpty());
    }
    
    @Override
    public MultimodalContent convertFormat(MultimodalContent content, String targetFormat) {
        if (!SUPPORTED_FORMATS.contains(targetFormat.toLowerCase())) {
            log.warn("Unsupported target format: {}", targetFormat);
            return content;
        }
        
        // 创建转换后的内容
        MultimodalContent convertedContent = MultimodalContent.builder()
                .type(content.getType())
                .format(targetFormat)
                .data(content.getData())
                .url(content.getUrl())
                .filePath(content.getFilePath())
                .description(content.getDescription())
                .metadata(content.getMetadata())
                .createdTime(content.getCreatedTime())
                .enabled(content.getEnabled())
                .mimeType(MIME_TYPES.getOrDefault(targetFormat.toLowerCase(), "text/plain"))
                .build();
        
        log.info("File format converted from {} to {}", content.getFormat(), targetFormat);
        
        return convertedContent;
    }
    
    @Override
    public int getPriority() {
        return 80;
    }
    
    /**
     * 从文件路径读取内容
     */
    public String readFromFile(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        if (!Files.exists(path)) {
            throw new FileNotFoundException("File not found: " + filePath);
        }
        
        return Files.readString(path);
    }
    
    /**
     * 将内容写入文件
     */
    public void writeToFile(String filePath, String content) throws IOException {
        Path path = Paths.get(filePath);
        Files.createDirectories(path.getParent());
        Files.writeString(path, content);
    }
    
    /**
     * 获取文件信息
     */
    public Map<String, Object> getFileInfo(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        if (!Files.exists(path)) {
            throw new FileNotFoundException("File not found: " + filePath);
        }
        
        Map<String, Object> info = new HashMap<>();
        info.put("exists", true);
        info.put("size", Files.size(path));
        info.put("isDirectory", Files.isDirectory(path));
        info.put("isRegularFile", Files.isRegularFile(path));
        info.put("isReadable", Files.isReadable(path));
        info.put("isWritable", Files.isWritable(path));
        info.put("lastModifiedTime", Files.getLastModifiedTime(path).toMillis());
        info.put("fileName", path.getFileName().toString());
        info.put("parent", path.getParent().toString());
        info.put("absolutePath", path.toAbsolutePath().toString());
        
        return info;
    }
}