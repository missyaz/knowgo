package com.fw.know.go.box.infrastructure.exception;

import com.fw.know.go.base.exception.ErrorCode;

/**
 * @Description
 * @Date 30/3/2026 上午10:20
 * @Author Leo
 */
public enum BlindBoxErrorCode implements ErrorCode {

    /**
     * 盲盒不存在
     */
    BLIND_BOX_NOT_EXIST("BLIND_BOX_NOT_EXIST", "盲盒不存在");

    private final String code;

    private final String message;

    BlindBoxErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
