package com.fw.know.go.ai.service.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @Description 文本转语音响应
 * @Date 24/11/2025 下午4:11
 * @Author Leo
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TextToSpeechResponse {
    
    /**
     * 音频数据（base64编码）
     */
    private String audioData;
    
    /**
     * 音频格式
     */
    private String audioFormat;
    
    /**
     * 使用的模型
     */
    private String model;
    
    /**
     * 响应时间（毫秒）
     */
    private Long responseTime;
    
    /**
     * 额外信息
     */
    private Map<String, Object> extraInfo;
}