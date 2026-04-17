package com.fw.know.go.tcc.entity;

/**
 * @Description 事务确认成功类型
 * @Date 16/4/2026 下午3:18
 * @Author Leo
 */
public enum TransConfirmSuccessType {

    /**
     * 确认成功
     */
    CONFIRM_SUCCESS,

    /**
     * 幂等确认
     */
    DUPLICATED_CONFIRM;
}
