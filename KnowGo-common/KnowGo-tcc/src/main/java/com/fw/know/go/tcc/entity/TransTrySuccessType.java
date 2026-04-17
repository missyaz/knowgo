package com.fw.know.go.tcc.entity;

/**
 * @Description 事务尝试成功类型
 * @Date 16/4/2026 下午3:19
 * @Author Leo
 */
public enum TransTrySuccessType {

    /**
     * Try成功
     */
    TRY_SUCCESS,

    /**
     * 幂等Try
     */
    DUPLICATED_TRY;
}
