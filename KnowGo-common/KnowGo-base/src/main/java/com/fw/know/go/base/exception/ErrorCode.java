package com.fw.know.go.base.exception;

/**
 * @Classname ErrorCode
 * @Description 错误码
 * @Date 28/10/2025 下午2:25
 * @Author Leo
 */
public interface ErrorCode {

    /**
     * 获取错误码
     * @return 错误码
     */
    String getCode();

     /**
      * 获取错误描述
      * @return 错误描述
      */
    String getMessage();
}
