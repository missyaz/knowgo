package com.fw.know.go.tcc.entity;

import com.fw.know.go.datasource.domain.entity.BaseEntity;
import com.fw.know.go.tcc.request.TccRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @Description 事务日志
 * @Date 16/4/2026 下午3:12
 * @Author Leo
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransactionLog extends BaseEntity {

    /**
     * 事务ID
     */
    private String transactionId;

    /**
     * 业务场景
     */
    private String businessScene;

    /**
     * 业务模块
     */
    private String businessModule;

    /**
     * 状态
     */
    private TransActionLogState state;

    /**
     * Cancel的类型
     */
    private TransCancelSuccessType cancelType;

    public TransactionLog(TccRequest tccRequest, TransActionLogState transActionLogState) {
        this.state = transActionLogState;
        this.transactionId = tccRequest.getTransactionId();
        this.businessScene = tccRequest.getBusinessScene();
        this.businessModule = tccRequest.getBusinessModule();
    }

    public TransactionLog(TccRequest tccRequest, TransActionLogState state, TransCancelSuccessType cancelType) {
        this.state = state;
        this.cancelType = cancelType;
        this.transactionId = tccRequest.getTransactionId();
        this.businessScene = tccRequest.getBusinessScene();
        this.businessModule = tccRequest.getBusinessModule();
    }
}
