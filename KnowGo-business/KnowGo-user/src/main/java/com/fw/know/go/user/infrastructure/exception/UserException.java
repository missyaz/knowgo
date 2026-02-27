package com.fw.know.go.user.infrastructure.exception;

import com.fw.know.go.base.exception.BizException;
import com.fw.know.go.base.exception.ErrorCode;

/**
 * @Description
 * @Date 25/2/2026 下午5:46
 * @Author Leo
 */
public class UserException extends BizException {
    public UserException(ErrorCode errorCode) {
        super(errorCode);
    }

    public UserException(String message, ErrorCode errorCode){
        super(message,errorCode);
    }

    public UserException(String message, Throwable cause, ErrorCode errorCode){
        super(message,cause,errorCode);
    }

    public UserException(Throwable cause, ErrorCode errorCode){
        super(cause,errorCode);
    }

    public UserException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, ErrorCode errorCode){
        super(message,cause,enableSuppression,writableStackTrace,errorCode);
    }
}
