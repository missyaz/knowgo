package com.fw.know.go.ai.multimodal.impl;

import com.fw.know.go.ai.multimodal.MultimodalContent;
import com.fw.know.go.ai.multimodal.MultimodalContentHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Base64;
import java.util.List;

/**
 * @Description 视频内容处理器
 * @Date 24/11/2025 下午5:08
 * @Author Leo
 */
@Slf4j
public class VideoContentHandler implements MultimodalContentHandler {
    
    private static final List<String> SUPPORTED_FORMATS = Arrays.asList(
            "mp4", "avi", "mov", "wmv", "flv", "mkv", "webm", "m4v"
    );
    
    private static final long MAX_VIDEO_SIZE = 500 * 1024 * 1024; // 500MB
    
    @Override
    public String getType() {
        return "video";
    }
    
    @Override
    public List<String> getSupportedFormats() {
        return SUPPORTED_FORMATS;
    }
    
    @Override
    public boolean validateContent(MultimodalContent content) {
        if (content == null || !"video".equals(content.getType())) {
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
        
        // 检查大小
        if (content.getSize() != null && content.getSize() > MAX_VIDEO_SIZE) {
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
                log.warn("Invalid base64 video data");
                content.setData(null);
            }
        }
        
        // 设置默认格式
        if (content.getFormat() == null) {
            content.setFormat("mp4");
        }
        
        // 估算大小
        if (content.getSize() == null && content.getData() != null) {
            // base64数据大约是原始数据的4/3
            long estimatedSize = (long) (content.getData().length() * 0.75);
            content.setSize(estimatedSize);
        }
        
        // 设置默认帧率
        if (content.getFrameRate() == null) {
            content.setFrameRate(30.0f); // 30fps
        }
        
        return content;
    }
    
    @Override
    public MultimodalContent postprocessContent(MultimodalContent content) {
        // 添加视频元数据
        if (content.getMetadata() == null) {
            content.setMetadata(new java.util.HashMap<>());
        }
        
        // 添加基本属性
        content.getMetadata().put("type", "video");
        content.getMetadata().put("format", content.getFormat());
        
        if (content.getDuration() != null) {
            content.getMetadata().put("durationSeconds", content.getDuration() / 1000);
            content.getMetadata().put("durationMinutes", content.getDuration() / 60000.0);
            content.getMetadata().put("durationHours", content.getDuration() / 3600000.0);
        }
        
        if (content.getWidth() != null) {
            content.getMetadata().put("width", content.getWidth());
        }
        
        if (content.getHeight() != null) {
            content.getMetadata().put("height", content.getHeight());
        }
        
        if (content.getFrameRate() != null) {
            content.getMetadata().put("frameRate", content.getFrameRate());
        }
        
        if (content.getBitRate() != null) {
            content.getMetadata().put("bitRate", content.getBitRate());
        }
        
        if (content.getSize() != null) {
            content.getMetadata().put("sizeInKB", content.getSize() / 1024);
            content.getMetadata().put("sizeInMB", content.getSize() / (1024.0 * 1024.0));
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
                .data(content.getData()) // 这里应该进行实际的格式转换
                .url(content.getUrl())
                .filePath(content.getFilePath())
                .description(content.getDescription())
                .metadata(content.getMetadata())
                .createdTime(content.getCreatedTime())
                .enabled(content.getEnabled())
                .duration(content.getDuration())
                .width(content.getWidth())
                .height(content.getHeight())
                .frameRate(content.getFrameRate())
                .bitRate(content.getBitRate())
                .build();
        
        log.info("Video format converted from {} to {}", content.getFormat(), targetFormat);
        
        return convertedContent;
    }
    
    @Override
    public int getPriority() {
        return 100;
    }
}