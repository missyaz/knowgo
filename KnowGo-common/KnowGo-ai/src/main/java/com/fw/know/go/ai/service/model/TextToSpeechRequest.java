package com.fw.know.go.ai.service.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @Description 文本转语音请求
 * @Date 24/11/2025 下午4:09
 * @Author Leo
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TextToSpeechRequest {
    
    /**
     * 要转换的文本
     */
    private String text;
    
    /**
     * 模型名称
     */
    private String model;
    
    /**
     * 语音
     */
    private String voice;
    
    /**
     * 响应格式（mp3, opus, aac, flac, wav, pcm）
     */
    private String responseFormat;
    
    /**
     * 语速（0.25-4.0）
     */
    private Double speed;
    
    /**
     * 额外参数
     */
    private Map<String, Object> extraParams;
}