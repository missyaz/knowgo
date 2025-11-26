package com.fw.know.go.ai.service.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @Description 音频转录响应
 * @Date 24/11/2025 下午4:07
 * @Author Leo
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AudioTranscriptionResponse {
    
    /**
     * 转录文本
     */
    private String text;
    
    /**
     * 任务状态
     */
    private String status;
    
    /**
     * 使用的模型
     */
    private String model;
    
    /**
     * 语言
     */
    private String language;
    
    /**
     * 持续时间（秒）
     */
    private Double duration;
    
    /**
     * 响应时间（毫秒）
     */
    private Long responseTime;
    
    /**
     * 额外信息
     */
    private Map<String, Object> extraInfo;
}