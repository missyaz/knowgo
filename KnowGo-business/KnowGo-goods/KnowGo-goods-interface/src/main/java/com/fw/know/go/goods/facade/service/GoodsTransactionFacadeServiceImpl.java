package com.fw.know.go.goods.facade.service;

import com.fw.know.go.api.goods.request.GoodsSaleRequest;
import com.fw.know.go.api.goods.response.GoodsSaleResponse;
import com.fw.know.go.api.goods.service.GoodsTransactionFacadeService;
import com.fw.know.go.box.domain.service.BlindBoxService;
import com.fw.know.go.collection.domain.service.CollectionService;
import com.fw.know.go.rpc.facade.Facade;
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

    @Override
    @Facade
    @Transactional(rollbackFor = Exception.class)
    public GoodsSaleResponse tryDecreaseInventory(GoodsSaleRequest request) {
        // Try阶段，锁库存
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
