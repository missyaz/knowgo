package com.fw.know.go.ai;

import lombok.Getter;

/**
 * @Description 大模型启用模式
 * @Date 11/11/2025 下午1:15
 * @Author Leo
 */
@Getter
public enum EnableMode {

    /**
     * OpenAI
     */
    OPENAI("openai"),
    /**
     * DashScope
     */
    DASHSCOPE("dashscope");

    private final String value;

    EnableMode(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
