package com.fw.know.go.tcc.response;

import com.fw.know.go.tcc.entity.TransTrySuccessType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * @Description 事务尝试响应
 * @Date 16/4/2026 下午3:28
 * @Author Leo
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionTryResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Boolean success;

    private String errorCode;

    private String errorMessage;

    private TransTrySuccessType transTrySuccessType;

    public TransactionTryResponse(Boolean success, TransTrySuccessType transTrySuccessType) {
        this.success = success;
        this.transTrySuccessType = transTrySuccessType;
    }

    public TransactionTryResponse(Boolean success, String errorCode, String errorMessage) {
        this.success = success;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}
