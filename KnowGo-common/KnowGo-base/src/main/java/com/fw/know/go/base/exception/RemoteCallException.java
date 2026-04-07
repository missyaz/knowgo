package com.fw.know.go.base.exception;

/**
 * @Description
 * @Date 7/4/2026 下午1:38
 * @Author Leo
 */
public class RemoteCallException extends BizException {

    public RemoteCallException(ErrorCode errorCode) {
        super(errorCode);
    }

    public RemoteCallException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }

    public RemoteCallException(Throwable cause, ErrorCode errorCode) {
        super(cause, errorCode);
    }

    public RemoteCallException(String message, Throwable cause, ErrorCode errorCode) {
        super(message, cause, errorCode);
    }

    public RemoteCallException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, ErrorCode errorCode) {
        super(message, cause, enableSuppression, writableStackTrace, errorCode);
    }
}
