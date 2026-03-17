package com.fw.know.go.notice.domain.constant;

/**
 * @Description
 * @Date 23/1/2026 上午10:17
 * @Author Leo
 */
public enum NoticeState {
    /**
     * 初始化
     */
    INIT,
    /**
     * 已发送成功
     */
    SUCCESS,
    /**
     * 发送失败
     */
    FAILED,
    /**
     * 已挂起
     */
    SUSPENDED
}
