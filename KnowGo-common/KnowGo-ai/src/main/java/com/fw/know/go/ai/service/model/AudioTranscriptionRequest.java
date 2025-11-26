package com.fw.know.go.ai.service.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @Description 音频转录请求
 * @Date 24/11/2025 下午4:05
 * @Author Leo
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AudioTranscriptionRequest {
    
    /**
     * 音频数据（base64编码或文件路径）
     */
    private String audioData;
    
    /**
     * 音频格式
     */
    private String audioFormat;
    
    /**
     * 模型名称
     */
    private String model;
    
    /**
     * 语言
     */
    private String language;
    
    /**
     * 提示词
     */
    private String prompt;
    
    /**
     * 响应格式（json, text, srt, verbose_json, vtt）
     */
    private String responseFormat;
    
    /**
     * 温度参数
     */
    private Double temperature;
    
    /**
     * 额外参数
     */
    private Map<String, Object> extraParams;
}