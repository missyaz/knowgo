package com.fw.know.go.tcc.entity;

/**
 * @Description 事务取消成功类型
 * @Date 16/4/2026 下午3:15
 * @Author Leo
 */
public enum TransCancelSuccessType {

    /**
     * 回滚成功: Try -> Cancel
     */
    CANCEL_AFTER_TRY_SUCCESS,

    /**
     * 回滚成功: Confirm -> Cancel
     */
    CANCEL_AFTER_CONFIRM_SUCCESS,

    /**
     * 空回滚
     */
    EMPTY_CANCEL,

    /**
     * 幂等回滚
     */
    DUPLICATED_CANCEL;

}
