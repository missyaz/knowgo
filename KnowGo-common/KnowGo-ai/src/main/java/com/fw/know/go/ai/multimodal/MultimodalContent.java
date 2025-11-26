package com.fw.know.go.ai.multimodal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description 多模态内容
 * @Date 24/11/2025 下午4:57
 * @Author Leo
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MultimodalContent {
    
    /**
     * 内容类型（text, image, audio, video）
     */
    private String type;
    
    /**
     * 内容格式（txt, jpg, png, mp3, mp4等）
     */
    private String format;
    
    /**
     * 内容数据（base64编码或URL）
     */
    private String data;
    
    /**
     * 内容URL（如果是网络资源）
     */
    private String url;
    
    /**
     * 文件路径（如果是本地文件）
     */
    private String filePath;
    
    /**
     * 内容大小（字节）
     */
    private Long size;
    
    /**
     * 内容描述
     */
    private String description;
    
    /**
     * 内容元数据
     */
    private java.util.Map<String, Object> metadata;
    
    /**
     * 创建时间
     */
    private Long createdTime;
    
    /**
     * 是否启用
     */
    private Boolean enabled;
    
    /**
     * 内容质量（0-100）
     */
    private Integer quality;
    
    /**
     * 内容宽度（图片/视频）
     */
    private Integer width;
    
    /**
     * 内容高度（图片/视频）
     */
    private Integer height;
    
    /**
     * 内容时长（音频/视频，毫秒）
     */
    private Long duration;
    
    /**
     * 采样率（音频）
     */
    private Integer sampleRate;
    
    /**
     * 比特率（音频/视频）
     */
    private Integer bitRate;
    
    /**
     * 帧率（视频）
     */
    private Float frameRate;
    
    /**
     * 语言（文本/音频）
     */
    private String language;
    
    /**
     * 编码格式
     */
    private String encoding;
    
    /**
     * 校验和
     */
    private String checksum;
    
    /**
     * 压缩类型
     */
    private String compression;
    
    /**
     * 内容创建者
     */
    private String creator;
    
    /**
     * 内容版权信息
     */
    private String copyright;
}