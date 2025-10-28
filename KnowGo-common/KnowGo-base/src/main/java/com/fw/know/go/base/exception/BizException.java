package com.fw.know.go.base.exception;

import lombok.Getter;
import lombok.Setter;

/**
 * @Classname BizException
 * @Description 业务异常
 * @Date 28/10/2025 下午2:24
 * @Author  Leo
 */
@Getter
@Setter
public class BizException extends RuntimeException {

    private ErrorCode errorCode;

    public BizException(ErrorCode errorCode) {
      super(errorCode.getMessage());
      this.errorCode = errorCode;
    }

    public BizException(String message, ErrorCode errorCode) {
      super(message);
      this.errorCode = errorCode;
    }

    public BizException(Throwable cause, ErrorCode errorCode) {
      super(cause);
      this.errorCode = errorCode;
    }

    public BizException(String message, Throwable cause, ErrorCode errorCode) {
      super(message, cause);
      this.errorCode = errorCode;
    }
}
