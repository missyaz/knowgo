package com.fw.know.go.document.infrastructure.exception;

import com.fw.know.go.base.exception.ErrorCode;

/**
 * @Author Leo
 * @Date 2025/11/3 20:57
 * @Description 文档模块错误码
 */
public enum RagErrorCode implements ErrorCode {

    /**
     * 文档为空
     */
    DOCUMENT_EMPTY("DOCUMENT_EMPTY", "上传的文档为空"),

    /**
     * 解析文档失败
     */
    PARSE_ERROR("DOCUMENT_PARSE_ERROR", "解析文档失败"),

    /**
     * 问题为空
     */
    QUESTION_EMPTY("QUESTION_EMPTY", "提问的问题为空"),
    ;

    private final String code;

    private final String message;

    RagErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String getCode() {
        return this.code;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
