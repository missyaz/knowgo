package com.fw.know.go.api.goods.request;

import com.fw.know.go.api.goods.constant.GoodsEvent;

/**
 * @Description 冻结库存
 * @Date 16/4/2026 下午2:56
 * @Author Leo
 */
public record GoodsFreezeInventoryRequest(String identifier, Long goodsId, Integer quantity){

    public GoodsEvent eventType() {
        return GoodsEvent.FREEZE_INVENTORY;
    }
}
