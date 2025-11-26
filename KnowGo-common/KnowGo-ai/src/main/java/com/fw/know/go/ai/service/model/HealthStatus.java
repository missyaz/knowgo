package com.fw.know.go.ai.service.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description 健康状态
 * @Date 24/11/2025 下午4:15
 * @Author Leo
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HealthStatus {
    
    /**
     * 服务状态（UP, DOWN, DEGRADED）
     */
    private String status;
    
    /**
     * 详细信息
     */
    private String message;
    
    /**
     * 响应时间（毫秒）
     */
    private Long responseTime;
    
    /**
     * 最后检查时间
     */
    private Long lastCheckTime;
    
    /**
     * 模型状态列表
     */
    private java.util.List<ModelHealth> modelHealths;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ModelHealth {
        /**
         * 模型名称
         */
        private String modelName;
        
        /**
         * 模型状态
         */
        private String status;
        
        /**
         * 消息
         */
        private String message;
        
        /**
         * 响应时间
         */
        private Long responseTime;
    }
}