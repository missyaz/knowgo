package com.fw.know.go.ai.multimodal.impl;

import com.fw.know.go.ai.multimodal.MultimodalContent;
import com.fw.know.go.ai.multimodal.MultimodalContentHandler;
import com.fw.know.go.ai.multimodal.MultimodalContentManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description 多模态内容管理器实现
 * @Date 24/11/2025 下午5:00
 * @Author Leo
 */
@Slf4j
@Component
public class MultimodalContentManagerImpl implements MultimodalContentManager {
    
    private final Map<String, MultimodalContentHandler> handlers = new ConcurrentHashMap<>();
    private final Map<String, List<MultimodalContentHandler>> typeHandlers = new ConcurrentHashMap<>();
    
    @PostConstruct
    public void init() {
        registerDefaultHandlers();
        log.info("MultimodalContentManager initialized");
    }
    
    @Override
    public void registerHandler(MultimodalContentHandler handler) {
        String type = handler.getType();
        handlers.put(type, handler);
        
        typeHandlers.computeIfAbsent(type, k -> new ArrayList<>()).add(handler);
        typeHandlers.get(type).sort(Comparator.comparingInt(MultimodalContentHandler::getPriority));
        
        log.info("Registered multimodal content handler: {} for type: {}", 
                handler.getClass().getSimpleName(), type);
    }
    
    @Override
    public void removeHandler(String type) {
        MultimodalContentHandler handler = handlers.remove(type);
        if (handler != null) {
            List<MultimodalContentHandler> typeList = typeHandlers.get(type);
            if (typeList != null) {
                typeList.remove(handler);
                if (typeList.isEmpty()) {
                    typeHandlers.remove(type);
                }
            }
            log.info("Removed multimodal content handler for type: {}", type);
        }
    }
    
    @Override
    public MultimodalContentHandler getHandler(String type) {
        return handlers.get(type);
    }
    
    @Override
    public MultimodalContent processContent(MultimodalContent content) {
        if (content == null) {
            return null;
        }
        
        String type = content.getType();
        if (type == null) {
            log.warn("Content type is null, skipping processing");
            return content;
        }
        
        List<MultimodalContentHandler> typeHandlerList = typeHandlers.get(type);
        if (typeHandlerList == null || typeHandlerList.isEmpty()) {
            log.warn("No handler found for content type: {}", type);
            return content;
        }
        
        MultimodalContent processedContent = content;
        
        // 预处理
        for (MultimodalContentHandler handler : typeHandlerList) {
            try {
                if (handler.validateContent(processedContent)) {
                    processedContent = handler.preprocessContent(processedContent);
                }
            } catch (Exception e) {
                log.error("Error preprocessing content with handler: {}", 
                        handler.getClass().getSimpleName(), e);
            }
        }
        
        // 后处理
        for (MultimodalContentHandler handler : typeHandlerList) {
            try {
                if (handler.validateContent(processedContent)) {
                    processedContent = handler.postprocessContent(processedContent);
                }
            } catch (Exception e) {
                log.error("Error postprocessing content with handler: {}", 
                        handler.getClass().getSimpleName(), e);
            }
        }
        
        return processedContent;
    }
    
    @Override
    public boolean validateContent(MultimodalContent content) {
        if (content == null) {
            return false;
        }
        
        String type = content.getType();
        if (type == null) {
            return false;
        }
        
        MultimodalContentHandler handler = handlers.get(type);
        if (handler == null) {
            log.warn("No handler found for content type: {}", type);
            return false;
        }
        
        try {
            return handler.validateContent(content);
        } catch (Exception e) {
            log.error("Error validating content with handler: {}", 
                    handler.getClass().getSimpleName(), e);
            return false;
        }
    }
    
    @Override
    public MultimodalContent convertContent(MultimodalContent content, String targetFormat) {
        if (content == null || targetFormat == null) {
            return content;
        }
        
        String type = content.getType();
        MultimodalContentHandler handler = handlers.get(type);
        if (handler == null) {
            log.warn("No handler found for content type: {}", type);
            return content;
        }
        
        try {
            return handler.convertFormat(content, targetFormat);
        } catch (Exception e) {
            log.error("Error converting content format with handler: {}", 
                    handler.getClass().getSimpleName(), e);
            return content;
        }
    }
    
    @Override
    public ContentInfo getContentInfo(MultimodalContent content) {
        if (content == null) {
            return ContentInfo.builder()
                    .valid(false)
                    .validationMessage("Content is null")
                    .build();
        }
        
        String type = content.getType();
        if (type == null) {
            return ContentInfo.builder()
                    .valid(false)
                    .validationMessage("Content type is null")
                    .build();
        }
        
        MultimodalContentHandler handler = handlers.get(type);
        if (handler == null) {
            return ContentInfo.builder()
                    .type(type)
                    .format(content.getFormat())
                    .size(content.getSize())
                    .valid(false)
                    .validationMessage("No handler found for type: " + type)
                    .build();
        }
        
        boolean valid = handler.validateContent(content);
        long size = handler.getContentSize(content);
        
        return ContentInfo.builder()
                .type(type)
                .format(content.getFormat())
                .size(size)
                .valid(valid)
                .validationMessage(valid ? "Content is valid" : "Content validation failed")
                .metadata(content.getMetadata())
                .build();
    }
    
    @Override
    public MultimodalContent compressContent(MultimodalContent content) {
        // TODO: 实现内容压缩逻辑
        log.info("Compressing content of type: {}", content.getType());
        return content;
    }
    
    @Override
    public MultimodalContent decompressContent(MultimodalContent content) {
        // TODO: 实现内容解压缩逻辑
        log.info("Decompressing content of type: {}", content.getType());
        return content;
    }
    
    @Override
    public List<String> getSupportedTypes() {
        return new ArrayList<>(handlers.keySet());
    }
    
    @Override
    public List<String> getSupportedFormats(String type) {
        MultimodalContentHandler handler = handlers.get(type);
        if (handler == null) {
            return Collections.emptyList();
        }
        return handler.getSupportedFormats();
    }
    
    /**
     * 注册默认处理器
     */
    private void registerDefaultHandlers() {
        // 文本处理器
        registerHandler(new TextContentHandler());
        
        // 图片处理器
        registerHandler(new ImageContentHandler());
        
        // 音频处理器
        registerHandler(new AudioContentHandler());
        
        // 视频处理器
        registerHandler(new VideoContentHandler());
    }
}