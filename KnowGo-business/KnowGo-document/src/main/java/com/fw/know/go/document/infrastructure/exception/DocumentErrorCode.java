package com.fw.know.go.document.infrastructure.exception;

import com.fw.know.go.base.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author Leo
 * @Date 2025/11/3 20:57
 * @Description 文档模块错误码
 */
public enum DocumentErrorCode implements ErrorCode {

    /**
     * 解析文档失败
     */
    PARSE_ERROR("DOCUMENT_PARSE_ERROR", "解析文档失败");

    private final String code;

    private final String message;

    DocumentErrorCode(String code, String message) {
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
