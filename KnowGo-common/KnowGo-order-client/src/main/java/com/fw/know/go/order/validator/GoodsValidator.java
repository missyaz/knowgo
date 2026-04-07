package com.fw.know.go.order.validator;

import com.fw.know.go.api.goods.constant.GoodsState;
import com.fw.know.go.api.goods.model.BaseGoodsVO;
import com.fw.know.go.api.goods.service.GoodsFacadeService;
import com.fw.know.go.api.order.request.OrderCreateRequest;
import com.fw.know.go.order.OrderException;

import static com.fw.know.go.api.order.constant.OrderErrorCode.GOODS_NOT_AVAILABLE;
import static com.fw.know.go.api.order.constant.OrderErrorCode.GOODS_PRICE_CHANGED;

/**
 * @Description 商品校验器
 * @Date 7/4/2026 上午10:07
 * @Author Leo
 */
public class GoodsValidator extends BaseOrderCreateValidator{

    private final GoodsFacadeService goodsFacadeService;

    public GoodsValidator(GoodsFacadeService goodsFacadeService) {
        this.goodsFacadeService = goodsFacadeService;
    }

    @Override
    protected void doValidate(OrderCreateRequest request) throws OrderException {
        BaseGoodsVO goods = goodsFacadeService.getGoods(request.getGoodsId(), request.getGoodsType());

        // 如果商品不是可售状态，则返回失败
        // PS：可售状态为什么要包含SOLD_OUT呢？因为商品查询的接口中去查询了 Redis 的最新库存，而 Redis 的库存在下单时可能已经扣减过刚好为0了，所以这里要包含 SOLD_OUT
        if (goods.getState() != GoodsState.SELLING && goods.getState() != GoodsState.SOLD_OUT){
            throw new OrderException(GOODS_NOT_AVAILABLE);
        }

        if (goods.getPrice().compareTo(request.getItemPrice()) != 0){
            throw new OrderException(GOODS_PRICE_CHANGED);
        }
    }
}
