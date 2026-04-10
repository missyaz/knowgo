package com.fw.know.go.goods.facade.service;

import com.fw.know.go.api.box.model.BlindBoxVO;
import com.fw.know.go.api.box.service.BlindBoxReadFacadeService;
import com.fw.know.go.api.collections.model.CollectionVO;
import com.fw.know.go.api.collections.service.CollectionReadFacadeService;
import com.fw.know.go.api.goods.constant.GoodsType;
import com.fw.know.go.api.goods.model.BaseGoodsVO;
import com.fw.know.go.api.goods.service.GoodsFacadeService;
import com.fw.know.go.base.response.SingleResponse;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author Leo
 * @Date 2026/3/28 16:07
 * @Description 商品聚合服务
 */
@Service
@DubboService(version = "1.0.0")
public class GoodsFacadeServiceImpl implements GoodsFacadeService {

    public static final String ERROR_CODE_UNSUPPORTED_GOODS_TYPE = "UNSUPPORTED_GOODS_TYPE";

    @Autowired
    private CollectionReadFacadeService collectionReadFacadeService;

    @Autowired
    private BlindBoxReadFacadeService blindBoxReadFacadeService;

    @Override
    public BaseGoodsVO getGoods(String goodsId, GoodsType goodsType) {
        return switch (goodsType) {
            case COLLECTION -> {
                SingleResponse<CollectionVO> response = collectionReadFacadeService.queryById(Long.valueOf(goodsId));
                if (response.getSuccess()){
                    yield response.getData();
                }
                yield null;
            }

            case BLIND_BOX -> {
                SingleResponse<BlindBoxVO> response = blindBoxReadFacadeService.queryById(Long.valueOf(goodsId));
                if (response.getSuccess()){
                    yield response.getData();
                }
                yield null;
            }

            default -> throw new UnsupportedOperationException(ERROR_CODE_UNSUPPORTED_GOODS_TYPE);
        };
    }
}
