package com.fw.know.go.api.goods.model;

import com.fw.know.go.api.goods.constant.GoodsState;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

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

    /**
     * 商品名称
      @return 商品名称
     */
    public abstract String getGoodsName();

    /**
     * 商品图片URL
     * @return 商品图片URL
     */
    public abstract String getGoodsPicUrl();

    /**
     * 卖家ID
     * @return 卖家ID
     */
    public abstract String getSellerId();

    /**
     * 版本
     * @return 版本
     */
    public abstract Integer getVersion();

    public Boolean available() {
        return state == GoodsState.NOT_FOR_SALE;
    }

    /**
     * 商品价格
     * @return 商品价格
     */
    public abstract BigDecimal getPrice();

    /**
     * 是否可预约
     * @return 是否可预约
     */
    public abstract Boolean canBook();

    /**
     * 是否立即预约
     * @return 是否立即预约
     */
    public abstract Boolean canBookNow();

    /**
     * 是否预约
     * @return 是否预约
     */
    public abstract Boolean hasBooked();

    /**
     * 商品预约开始时间
     * @return  商品预约开始时间
     */
    public abstract Date getBookStartTime();

    /**
     * 商品预约结束时间
     * @return 商品预约结束时间
     */
    public abstract Date getBookEndTime();
}
