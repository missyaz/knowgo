package com.fw.know.go.base.exception;

import lombok.Getter;

/**
 * @Description
 * @Date 7/4/2026 下午2:16
 * @Author Leo
 */
@Getter
public enum BizErrorCode implements ErrorCode {

    /**
     * 重复请求
     */
    DUPLICATED("DUPLICATED", "重复请求"),

    /**
     * 远程调用返回结果为空
     */
    REMOTE_CALL_RESPONSE_IS_NULL("REMOTE_CALL_RESPONSE_IS_NULL", "远程调用返回结果为空"),

    /**
     * 远程调用返回结果失败
     */
    REMOTE_CALL_RESPONSE_IS_FAILED("REMOTE_CALL_RESPONSE_IS_FAILED", "远程调用返回结果失败");

    private final String code;

    private final String message;

    BizErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
