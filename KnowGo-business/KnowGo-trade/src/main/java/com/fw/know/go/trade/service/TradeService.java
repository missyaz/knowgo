package com.fw.know.go.trade.service;

import com.fw.know.go.trade.param.BuyParam;
import jakarta.validation.Valid;

/**
 * @Description
 * @Date 2/4/2026 上午10:29
 * @Author Leo
 */
public interface TradeService {

    /**
     * 普通下单
     * @param buyParam 下单参数
     * @return 订单号
     */
    String normalBuy(BuyParam buyParam);
}
