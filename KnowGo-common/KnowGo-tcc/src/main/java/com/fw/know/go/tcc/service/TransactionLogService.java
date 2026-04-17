package com.fw.know.go.tcc.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fw.know.go.tcc.entity.*;
import com.fw.know.go.tcc.mapper.TransactionLogMapper;
import com.fw.know.go.tcc.request.TccRequest;
import com.fw.know.go.tcc.response.TransactionCancelResponse;
import com.fw.know.go.tcc.response.TransactionConfirmResponse;
import com.fw.know.go.tcc.response.TransactionTryResponse;

/**
 * @Description 事务日志服务
 * @Date 16/4/2026 下午3:33
 * @Author Leo
 */
public class TransactionLogService extends ServiceImpl<TransactionLogMapper, TransactionLog> {

    /**
     * TCC事务的Try
     * @param tccRequest Tcc请求
     * @return 响应
     */
    public TransactionTryResponse tryTransaction(TccRequest tccRequest){
        TransactionLog existTransLog = this.getExistTransLog(tccRequest);
        if(existTransLog == null){
            // 不存在分布式事务,则新建分布式事务
            TransactionLog transactionLog = new TransactionLog(tccRequest, TransActionLogState.TRY);
            if (this.save(transactionLog)){
                return new TransactionTryResponse(true, TransTrySuccessType.TRY_SUCCESS);
            }
            return new TransactionTryResponse(false, "TRY_FAILED", "TRY_FAILED");
        }

        // 幂等
        return new TransactionTryResponse(true, TransTrySuccessType.DUPLICATED_TRY);
    }

    /**
     * TCC事务的Confirm
     * @param tccRequest Tcc请求
     * @return 响应
     */
    public TransactionConfirmResponse confirmTransaction(TccRequest tccRequest){
        TransactionLog existTransLog = this.getExistTransLog(tccRequest);
        if(existTransLog == null){
            // 不存在分布式事务
            throw new UnsupportedOperationException("transaction can not be confirmed");
        }
        // 只有状态是TRY才能进行Confirm
        if (existTransLog.getState() == TransActionLogState.TRY){
            existTransLog.setState(TransActionLogState.CONFIRM);
            if (this.updateById(existTransLog)){
                return new TransactionConfirmResponse(true, TransConfirmSuccessType.CONFIRM_SUCCESS);
            }
            return new TransactionConfirmResponse(false, "CONFIRM_FAILED", "CONFIRM_FAILED");
        }

        // 幂等
        if (existTransLog.getState() == TransActionLogState.CONFIRM){
            return new TransactionConfirmResponse(true, TransConfirmSuccessType.DUPLICATED_CONFIRM);
        }

        throw new UnsupportedOperationException("transaction can not be confirmed : " + existTransLog.getState());
    }


    /**
     * TCC事务的Cancel
     * @param tccRequest Tcc请求
     * @return 响应
     */
    public TransactionCancelResponse cancelTransaction(TccRequest tccRequest){
        TransactionLog existTransLog = this.getExistTransLog(tccRequest);
        // 如果还没有Try，则直接记录一条Cancel数据，避免发生空回滚，并解决悬挂问题
        // TODO：如果不记录，是不是会导致悬挂问题，需要测试一下
        if (existTransLog == null){
            TransactionLog transactionLog = new TransactionLog(tccRequest, TransActionLogState.CANCEL, TransCancelSuccessType.EMPTY_CANCEL);
            if (this.save(transactionLog)){
                return new TransactionCancelResponse(true, TransCancelSuccessType.EMPTY_CANCEL);
            }
            return new TransactionCancelResponse(false, "EMPTY_CANCEL_FAILED", "EMPTY_CANCEL_FAILED");
        }

        // 针对Try回滚
        if (existTransLog.getState() == TransActionLogState.TRY){
            existTransLog.setState(TransActionLogState.CANCEL);
            existTransLog.setCancelType(TransCancelSuccessType.CANCEL_AFTER_TRY_SUCCESS);
            if (this.updateById(existTransLog)){
                return new TransactionCancelResponse(true, TransCancelSuccessType.CANCEL_AFTER_TRY_SUCCESS);
            }
            return new TransactionCancelResponse(false, "CANCEL_FAILED", "CANCEL_FAILED");
        }
        // 针对Confirm回滚
        if (existTransLog.getState() == TransActionLogState.CONFIRM){
            existTransLog.setState(TransActionLogState.CANCEL);
            existTransLog.setCancelType(TransCancelSuccessType.CANCEL_AFTER_CONFIRM_SUCCESS);
            if (this.updateById(existTransLog)){
                return new TransactionCancelResponse(true, TransCancelSuccessType.CANCEL_AFTER_CONFIRM_SUCCESS);
            }
            return new TransactionCancelResponse(false, "CANCEL_FAILED", "CANCEL_FAILED");
        }

        // 幂等
        if (existTransLog.getState() == TransActionLogState.CANCEL){
            return new TransactionCancelResponse(true, TransCancelSuccessType.DUPLICATED_CANCEL);
        }

        return new TransactionCancelResponse(false, "CANCEL_FAILED", "CANCEL_FAILED");
    }

    private TransactionLog getExistTransLog(TccRequest tccRequest){
        QueryWrapper<TransactionLog> transactionLogQueryWrapper = new QueryWrapper<>();
        transactionLogQueryWrapper.eq("transaction_id",tccRequest.getTransactionId());
        transactionLogQueryWrapper.eq("business_scene",tccRequest.getBusinessScene());
        transactionLogQueryWrapper.eq("business_module",tccRequest.getBusinessModule());

        return this.getOne(transactionLogQueryWrapper);
    }
}
