package com.fw.know.go.collection.infrastructure.exception;

import com.fw.know.go.base.exception.ErrorCode;

/**
 * @Description
 * @Date 30/3/2026 上午9:28
 * @Author Leo
 */
public enum CollectionErrorCode implements ErrorCode {

    /**
     * 藏品信息保存失败
     */
    COLLECTION_SAVE_FAILED("COLLECTION_SAVE_FAILED", "藏品信息保存失败"),

    /**
     * 藏品不存在
     */
    COLLECTION_NOT_EXIST("COLLECTION_NOT_EXIST", "藏品不存在"),
    /**
     * 藏品流水信息保存失败
     */
    COLLECTION_STREAM_SAVE_FAILED("COLLECTION_STREAM_SAVE_FAILED", "藏品流水信息保存失败"),;

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
