package com.fw.know.go.notice.infrastructure.exception;

import com.fw.know.go.base.exception.BizException;
import com.fw.know.go.base.exception.ErrorCode;

/**
 * @Description
 * @Date 23/1/2026 下午1:28
 * @Author Leo
 */
public class NoticeException extends BizException {

    public NoticeException(ErrorCode errorCode) {
        super(errorCode);
    }

    public NoticeException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }

    public NoticeException(Throwable cause, ErrorCode errorCode) {
        super(cause, errorCode);
    }

    public NoticeException(String message, Throwable cause, ErrorCode errorCode) {
        super(message, cause, errorCode);
    }
}
