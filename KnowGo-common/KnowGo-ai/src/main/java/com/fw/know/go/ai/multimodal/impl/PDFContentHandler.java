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
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * @Description PDF内容处理器
 * @Date 24/11/2025 下午5:08
 * @Author Leo
 */
@Slf4j
public class PDFContentHandler implements MultimodalContentHandler {
    
    private static final List<String> SUPPORTED_FORMATS = Arrays.asList("pdf");
    private static final long MAX_PDF_SIZE = 100 * 1024 * 1024; // 100MB
    
    @Override
    public String getType() {
        return "document";
    }
    
    @Override
    public List<String> getSupportedFormats() {
        return SUPPORTED_FORMATS;
    }
    
    @Override
    public boolean validateContent(MultimodalContent content) {
        if (content == null || !"document".equals(content.getType())) {
            return false;
        }
        
        // 检查格式
        String format = content.getFormat();
        if (format == null || !"pdf".equals(format.toLowerCase())) {
            return false;
        }
        
        // 检查是否有数据
        if (content.getData() == null && content.getUrl() == null && content.getFilePath() == null) {
            return false;
        }
        
        // 检查大小
        if (content.getSize() != null && content.getSize() > MAX_PDF_SIZE) {
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
                log.warn("Invalid base64 PDF data");
                content.setData(null);
            }
        }
        
        // 设置默认格式
        if (content.getFormat() == null) {
            content.setFormat("pdf");
        }
        
        // 估算大小
        if (content.getSize() == null && content.getData() != null) {
            // base64数据大约是原始数据的4/3
            long estimatedSize = (long) (content.getData().length() * 0.75);
            content.setSize(estimatedSize);
        }
        
        return content;
    }
    
    @Override
    public MultimodalContent postprocessContent(MultimodalContent content) {
        // 添加文档元数据
        if (content.getMetadata() == null) {
            content.setMetadata(new HashMap<>());
        }
        
        // 添加基本属性
        content.getMetadata().put("type", "document");
        content.getMetadata().put("format", "pdf");
        
        if (content.getSize() != null) {
            content.getMetadata().put("sizeInKB", content.getSize() / 1024);
            content.getMetadata().put("sizeInMB", content.getSize() / (1024.0 * 1024.0));
        }
        
        // 尝试提取PDF页数（简化实现）
        if (content.getData() != null) {
            try {
                int pageCount = extractPageCount(content.getData());
                content.getMetadata().put("pageCount", pageCount);
            } catch (Exception e) {
                log.warn("Failed to extract PDF page count", e);
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
        log.warn("PDF format conversion not supported from PDF to {}", targetFormat);
        return content;
    }
    
    @Override
    public int getPriority() {
        return 90;
    }
    
    /**
     * 简化版PDF页数提取
     * 注意：这是一个非常简化的实现，仅用于演示
     */
    private int extractPageCount(String base64Data) {
        try {
            byte[] pdfData = Base64.getDecoder().decode(base64Data);
            String pdfContent = new String(pdfData, "UTF-8");
            
            // 查找PDF文件中的页面计数标记
            // 这是一个简化的实现，实际的PDF解析需要专门的库
            int count = 0;
            String[] lines = pdfContent.split("\n");
            for (String line : lines) {
                if (line.contains("/Count") || line.contains("/Type /Page")) {
                    count++;
                }
            }
            
            return Math.max(count, 1);
        } catch (Exception e) {
            log.warn("Failed to extract PDF page count", e);
            return 1;
        }
    }
    
    /**
     * 压缩PDF内容
     */
    public byte[] compressPDF(byte[] pdfData) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (GZIPOutputStream gzipOut = new GZIPOutputStream(baos)) {
            gzipOut.write(pdfData);
        }
        return baos.toByteArray();
    }
    
    /**
     * 解压缩PDF内容
     */
    public byte[] decompressPDF(byte[] compressedData) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (GZIPInputStream gzipIn = new GZIPInputStream(new ByteArrayInputStream(compressedData))) {
            byte[] buffer = new byte[8192];
            int len;
            while ((len = gzipIn.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }
        }
        return baos.toByteArray();
    }
}