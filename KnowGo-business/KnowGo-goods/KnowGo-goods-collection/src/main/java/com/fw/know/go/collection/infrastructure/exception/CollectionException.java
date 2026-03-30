package com.fw.know.go.collection.infrastructure.exception;

import com.fw.know.go.base.exception.BizException;
import com.fw.know.go.base.exception.ErrorCode;

/**
 * @Description
 * @Date 30/3/2026 上午9:28
 * @Author Leo
 */
public class CollectionException extends BizException {
    public CollectionException(ErrorCode errorCode) {
        super(errorCode);
    }

    public CollectionException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }

    public CollectionException(String message, Throwable cause, ErrorCode errorCode) {
        super(message, cause, errorCode);
    }

    public CollectionException(Throwable cause, ErrorCode errorCode) {
        super(cause, errorCode);
    }

    public CollectionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, ErrorCode errorCode) {
        super(message, cause, enableSuppression, writableStackTrace, errorCode);
    }
}
