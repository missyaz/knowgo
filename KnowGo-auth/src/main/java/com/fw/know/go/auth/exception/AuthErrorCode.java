package com.fw.know.go.auth.exception;

import com.fw.know.go.base.exception.ErrorCode;

/**
 * @Description
 * @Date 24/2/2026 上午10:46
 * @Author Leo
 */
public enum AuthErrorCode implements ErrorCode {
    /**
     * 验证码错误
     */
    VERIFICATION_CODE_WRONG("VERIFICATION_CODE_WRONG", "验证码错误"),

    /**
     * 注册失败
     */
    REGISTER_ERROR("REGISTER_ERROR", "注册失败"),
    ;

    private final String code;

    private final String message;

    AuthErrorCode(String code, String message) {
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
