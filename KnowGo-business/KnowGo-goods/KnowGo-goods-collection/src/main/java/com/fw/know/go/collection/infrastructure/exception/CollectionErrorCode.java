package com.fw.know.go.collection.infrastructure.exception;

import com.fw.know.go.base.exception.ErrorCode;

/**
 * @Description
 * @Date 30/3/2026 上午9:28
 * @Author Leo
 */
public enum CollectionErrorCode implements ErrorCode {

    /**
     * 藏品不存在
     */
    COLLECTION_NOT_EXIST("COLLECTION_NOT_EXIST", "藏品不存在"),;

    private final String code;

    private final String message;

    CollectionErrorCode(String code, String message) {
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
