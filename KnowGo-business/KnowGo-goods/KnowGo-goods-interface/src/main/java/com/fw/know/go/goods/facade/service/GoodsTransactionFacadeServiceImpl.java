package com.fw.know.go.goods.facade.service;

import cn.hutool.core.lang.Assert;
import com.fw.know.go.api.goods.constant.GoodsType;
import com.fw.know.go.api.goods.request.GoodsFreezeInventoryRequest;
import com.fw.know.go.api.goods.request.GoodsSaleRequest;
import com.fw.know.go.api.goods.response.GoodsSaleResponse;
import com.fw.know.go.api.goods.service.GoodsTransactionFacadeService;
import com.fw.know.go.box.domain.service.BlindBoxService;
import com.fw.know.go.collection.domain.service.CollectionService;
import com.fw.know.go.rpc.facade.Facade;
import com.fw.know.go.tcc.entity.TransTrySuccessType;
import com.fw.know.go.tcc.request.TccRequest;
import com.fw.know.go.tcc.response.TransactionTryResponse;
import com.fw.know.go.tcc.service.TransactionLogService;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Description
 * @Date 8/4/2026 下午2:21
 * @Author Leo
 */
@DubboService(version = "1.0.0")
@RequiredArgsConstructor
public class GoodsTransactionFacadeServiceImpl implements GoodsTransactionFacadeService {

    private static final String ERROR_CODE_UNSUPPORTED_GOODS_TYPE = "UNSUPPORTED_GOODS_TYPE";

    private final CollectionService collectionService;

    private final BlindBoxService blindBoxService;

    private final TransactionLogService transactionLogService;

    @Override
    @Facade
    @Transactional(rollbackFor = Exception.class)
    public GoodsSaleResponse tryDecreaseInventory(GoodsSaleRequest request) {
        // Try阶段，锁库存
        GoodsFreezeInventoryRequest freezeInventiryRequest = new GoodsFreezeInventoryRequest(request.getBizNo(),
                request.getGoodsId(),
                request.getQuantity());

        GoodsType goodsType = GoodsType.valueOf(request.getGoodsType());

        TransactionTryResponse transactionTryResponse = transactionLogService.tryTransaction(new TccRequest(request.getBizNo(), "normalBuy",
                goodsType.name()));
        Assert.isTrue(transactionTryResponse.getSuccess(), "transaction try failed");

        if (transactionTryResponse.getTransTrySuccessType() == TransTrySuccessType.TRY_SUCCESS){
            // 成功获取事务
            Boolean freezeResult = switch (goodsType){
                case COLLECTION -> collectionService.freezeInventory(freezeInventiryRequest);
                case BLIND_BOX -> blindBoxService.freezeInventory(freezeInventiryRequest);
                default -> throw new IllegalArgumentException(ERROR_CODE_UNSUPPORTED_GOODS_TYPE);
            };
            Assert.isTrue(freezeResult, "freeze inventory failed");
            GoodsSaleResponse goodsSaleResponse = new GoodsSaleResponse();
            goodsSaleResponse.setSuccess(Boolean.TRUE);
            return goodsSaleResponse;
        }

        return new GoodsSaleResponse.GoodsResponseBuilder().buildSuccess();
    }

    @Override
    @Facade
    @Transactional(rollbackFor = Exception.class)
    public GoodsSaleResponse confirmDecreaseInventory(GoodsSaleRequest request) {
        // Confirm阶段，解锁库存并进行库存的减
        return new GoodsSaleResponse.GoodsResponseBuilder().buildSuccess();
    }
}
