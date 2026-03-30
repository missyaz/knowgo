package com.fw.know.go.box.infrastructure.exception;

import com.fw.know.go.base.exception.BizException;
import com.fw.know.go.base.exception.ErrorCode;

/**
 * @Description
 * @Date 30/3/2026 上午10:20
 * @Author Leo
 */
public class BlindBoxException extends BizException {
    public BlindBoxException(ErrorCode errorCode) {
        super(errorCode);
    }

    public BlindBoxException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }

    public BlindBoxException(String message, Throwable cause, ErrorCode errorCode) {
        super(message, cause, errorCode);
    }

    public BlindBoxException(Throwable cause, ErrorCode errorCode) {
        super(cause, errorCode);
    }

    public BlindBoxException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, ErrorCode errorCode) {
        super(message, cause, enableSuppression, writableStackTrace, errorCode);
    }
}
