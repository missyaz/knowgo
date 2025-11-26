package com.fw.know.go.ai.service.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * @Description 聊天响应
 * @Date 24/11/2025 下午3:47
 * @Author Leo
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatResponse {
    
    /**
     * 选择列表
     */
    private List<Choice> choices;
    
    /**
     * 使用的模型
     */
    private String model;
    
    /**
     * 消耗的token数
     */
    private TokenUsage tokenUsage;
    
    /**
     * 响应时间（毫秒）
     */
    private Long responseTime;
    
    /**
     * 额外信息
     */
    private Map<String, Object> extraInfo;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Choice {
        /**
         * 索引
         */
        private Integer index;
        
        /**
         * 消息
         */
        private Message message;
        
        /**
         * 完成原因
         */
        private String finishReason;
        
        /**
         * 对数概率
         */
        private Double logprobs;
        
        @Data
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class Message {
            /**
             * 角色
             */
            private String role;
            
            /**
             * 内容
             */
            private String content;
            
            /**
             * 工具调用
             */
            private List<ToolCall> toolCalls;
            
            /**
             * 工具调用ID
             */
            private String toolCallId;
            
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
                     * 参数
                     */
                    private String arguments;
                }
            }
        }
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TokenUsage {
        /**
         * 提示词token数
         */
        private Integer promptTokens;
        
        /**
         * 生成token数
         */
        private Integer completionTokens;
        
        /**
         * 总token数
         */
        private Integer totalTokens;
    }
}