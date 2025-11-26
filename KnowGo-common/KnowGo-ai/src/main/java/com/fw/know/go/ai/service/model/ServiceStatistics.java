package com.fw.know.go.ai.service.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @Description 服务统计信息
 * @Date 24/11/2025 下午4:17
 * @Author Leo
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceStatistics {
    
    /**
     * 总请求数
     */
    private Long totalRequests;
    
    /**
     * 成功请求数
     */
    private Long successfulRequests;
    
    /**
     * 失败请求数
     */
    private Long failedRequests;
    
    /**
     * 平均响应时间（毫秒）
     */
    private Double averageResponseTime;
    
    /**
     * 总token消耗
     */
    private TokenConsumption totalTokenConsumption;
    
    /**
     * 模型统计
     */
    private Map<String, ModelStatistics> modelStatistics;
    
    /**
     * 统计时间范围
     */
    private TimeRange timeRange;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TokenConsumption {
        /**
         * 总提示词token数
         */
        private Long totalPromptTokens;
        
        /**
         * 总生成token数
         */
        private Long totalCompletionTokens;
        
        /**
         * 总token数
         */
        private Long totalTokens;
        
        /**
         * 预估成本（美元）
         */
        private Double estimatedCost;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ModelStatistics {
        /**
         * 模型名称
         */
        private String modelName;
        
        /**
         * 请求数
         */
        private Long requestCount;
        
        /**
         * 成功数
         */
        private Long successCount;
        
        /**
         * 平均响应时间
         */
        private Double averageResponseTime;
        
        /**
         * token消耗
         */
        private TokenConsumption tokenConsumption;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TimeRange {
        /**
         * 开始时间
         */
        private Long startTime;
        
        /**
         * 结束时间
         */
        private Long endTime;
        
        /**
         * 时间范围描述
         */
        private String description;
    }
}