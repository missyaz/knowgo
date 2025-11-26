package com.fw.know.go.ai.service.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * @Description 聊天请求
 * @Date 24/11/2025 下午3:42
 * @Author Leo
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatRequest {
    
    /**
     * 消息列表
     */
    private List<Message> messages;
    
    /**
     * 模型名称
     */
    private String model;
    
    /**
     * 温度参数（0-2）
     */
    private Double temperature;
    
    /**
     * 最大token数
     */
    private Integer maxTokens;
    
    /**
     * 是否流式输出
     */
    private Boolean stream;
    
    /**
     * 系统提示词
     */
    private String systemPrompt;
    
    /**
     * 工具调用
     */
    private List<Tool> tools;
    
    /**
     * 额外参数
     */
    private Map<String, Object> extraParams;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Message {
        /**
         * 角色（system, user, assistant, tool）
         */
        private String role;
        
        /**
         * 内容
         */
        private String content;
        
        /**
         * 消息名称
         */
        private String name;
        
        /**
         * 工具调用
         */
        private List<ToolCall> toolCalls;
        
        /**
         * 工具调用ID
         */
        private String toolCallId;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Tool {
        /**
         * 工具类型
         */
        private String type;
        
        /**
         * 工具函数
         */
        private ToolFunction function;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ToolFunction {
        /**
         * 函数名称
         */
        private String name;
        
        /**
         * 函数描述
         */
        private String description;
        
        /**
         * 函数参数
         */
        private Map<String, Object> parameters;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ToolCall {
        /**
         * 调用ID
         */
        private String id;
        
        /**
         * 工具类型
         */
        private String type;
        
        /**
         * 函数
         */
        private ToolFunction function;
    }
}