package com.fw.know.go.api.goods.service;

import com.fw.know.go.api.goods.constant.GoodsType;
import com.fw.know.go.api.goods.model.BaseGoodsVO;

/**
 * @Description 商品服务
 * @Date 26/3/2026 下午3:07
 * @Author Leo
 */
public interface GoodsFacadeService {


    public BaseGoodsVO getGoods(String goodsId, GoodsType goodsType);
}
