package com.fw.know.go.tcc.response;

import com.fw.know.go.tcc.entity.TransCancelSuccessType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Description 事务回滚响应
 * @Date 16/4/2026 下午3:30
 * @Author Leo
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionCancelResponse implements Serializable {

    private Boolean success;

    private String errorCode;

    private String errorMessage;

    private TransCancelSuccessType transCancelSuccessType;

    public TransactionCancelResponse(Boolean success, TransCancelSuccessType transCancelSuccessType) {
        this.success = success;
        this.transCancelSuccessType = transCancelSuccessType;
    }

    public TransactionCancelResponse(Boolean success, String errorCode, String errorMessage) {
        this.success = success;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}
