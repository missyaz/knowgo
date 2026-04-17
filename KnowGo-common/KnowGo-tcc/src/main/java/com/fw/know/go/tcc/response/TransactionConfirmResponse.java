package com.fw.know.go.tcc.response;

import com.fw.know.go.tcc.entity.TransConfirmSuccessType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Description 事务确认响应
 * @Date 16/4/2026 下午3:30
 * @Author Leo
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionConfirmResponse implements Serializable {

    private Boolean success;

    private String errorCode;

    private String errorMessage;

    private TransConfirmSuccessType transConfirmSuccessType;

    public TransactionConfirmResponse(Boolean success, TransConfirmSuccessType transConfirmSuccessType) {
        this.success = success;
        this.transConfirmSuccessType = transConfirmSuccessType;
    }

    public TransactionConfirmResponse(Boolean success, String errorCode, String errorMessage) {
        this.success = success;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}
