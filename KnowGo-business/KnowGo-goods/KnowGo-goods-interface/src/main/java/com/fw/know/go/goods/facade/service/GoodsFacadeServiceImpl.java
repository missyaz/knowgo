package com.fw.know.go.goods.facade.service;

import com.fw.know.go.api.goods.constant.GoodsType;
import com.fw.know.go.api.goods.model.BaseGoodsVO;
import com.fw.know.go.api.goods.service.GoodsFacadeService;
import org.apache.dubbo.config.annotation.DubboService;
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

    @Override
    public BaseGoodsVO getGoods(String goodsId, GoodsType goodsType) {
        return switch (goodsType) {
            case COLLECTION -> {
                // TODO： 藏品 Dubbo 查询实现
            }

            case BLIND_BOX -> {
                // TODO： 盲盒 Dubbo 查询实现
            }

            default -> throw new UnsupportedOperationException(ERROR_CODE_UNSUPPORTED_GOODS_TYPE);
        };
    }
}
