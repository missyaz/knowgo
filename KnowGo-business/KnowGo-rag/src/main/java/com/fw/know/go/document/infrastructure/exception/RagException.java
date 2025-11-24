package com.fw.know.go.document.infrastructure.exception;

import com.fw.know.go.base.exception.BizException;
import com.fw.know.go.base.exception.ErrorCode;

/**
 * @Author Leo
 * @Date 2025/11/3 20:57
 * @Description 文档模块异常
 */
public class RagException extends BizException {

    public RagException(ErrorCode errorCode) {
        super(errorCode);
    }

    public RagException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }

    public RagException(Throwable cause, ErrorCode errorCode) {
        super(cause, errorCode);
    }

    public RagException(String message, Throwable cause, ErrorCode errorCode) {
        super(message, cause, errorCode);
    }
}
