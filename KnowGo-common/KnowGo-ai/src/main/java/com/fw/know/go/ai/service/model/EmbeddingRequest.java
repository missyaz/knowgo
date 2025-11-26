package com.fw.know.go.ai.service.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * @Description 嵌入请求
 * @Date 24/11/2025 下午3:52
 * @Author Leo
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmbeddingRequest {
    
    /**
     * 输入文本列表
     */
    private List<String> inputs;
    
    /**
     * 模型名称
     */
    private String model;
    
    /**
     * 编码格式
     */
    private String encodingFormat;
    
    /**
     * 维度
     */
    private Integer dimensions;
    
    /**
     * 额外参数
     */
    private Map<String, Object> extraParams;
}