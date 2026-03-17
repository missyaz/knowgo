package com.fw.know.go.notice.infrastructure.exception;

import com.fw.know.go.base.exception.ErrorCode;

/**
 * @Description
 * @Date 23/1/2026 下午1:29
 * @Author Leo
 */
public enum NoticeErrorCode implements ErrorCode {

    /**
     * 通知保存失败
     */
    NOTICE_SAVE_FAILED("NOTICE_SAVE_FAILED", "通知保存失败");

    private final String code;

    private final String message;

    NoticeErrorCode(String code, String message) {
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
