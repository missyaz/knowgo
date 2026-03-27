package com.fw.know.go.api.goods.model;

import com.fw.know.go.api.goods.constant.GoodsState;

import java.io.Serializable;

/**
 * @Description
 * @Date 26/3/2026 下午3:08
 * @Author Leo
 */
public abstract class BaseGoodsVO implements Serializable {

    /**
     * 商品状态
     */
    private GoodsState state;

    public GoodsState getState() {
        return state;
    }

    public void setState(GoodsState state) {
        this.state = state;
    }
}
