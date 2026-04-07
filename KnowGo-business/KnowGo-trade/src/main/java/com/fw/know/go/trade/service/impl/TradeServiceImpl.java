package com.fw.know.go.trade.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.fw.know.go.api.common.constant.BusinessCode;
import com.fw.know.go.api.goods.constant.GoodsType;
import com.fw.know.go.api.goods.model.BaseGoodsVO;
import com.fw.know.go.api.goods.service.GoodsFacadeService;
import com.fw.know.go.api.order.request.OrderCreateAndConfirmRequest;
import com.fw.know.go.api.order.response.OrderResponse;
import com.fw.know.go.api.user.constant.UserType;
import com.fw.know.go.base.utils.RemoteCallWrapper;
import com.fw.know.go.order.OrderException;
import com.fw.know.go.order.sharding.id.DistributeID;
import com.fw.know.go.order.sharding.id.WorkerIdHolder;
import com.fw.know.go.order.validator.OrderCreateValidator;
import com.fw.know.go.trade.application.TradeApplicationService;
import com.fw.know.go.trade.exception.TradeErrorCode;
import com.fw.know.go.trade.exception.TradeException;
import com.fw.know.go.trade.param.BuyParam;
import com.fw.know.go.trade.service.TradeService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;

import static com.fw.know.go.web.filter.TokenFilter.TOKEN_THREAD_LOCAL;

/**
 * @Description
 * @Date 2/4/2026 上午10:29
 * @Author Leo
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TradeServiceImpl implements TradeService {

    private final GoodsFacadeService goodsFacadeService;

    private final OrderCreateValidator orderCreateValidatorChain;

    private final TradeApplicationService tradeApplicationService;

    @Override
    public String normalBuy(BuyParam buyParam) {
        OrderCreateAndConfirmRequest orderCreateAndConfirmRequest = getOrderCreateAndConfirmRequest(buyParam);
        // 校验创建确认订单参数
        orderCreateValidatorChain.validate(orderCreateAndConfirmRequest);
        OrderResponse orderResponse = RemoteCallWrapper.call(tradeApplicationService::normalBuy, orderCreateAndConfirmRequest,
                "createOrder");

        if (orderResponse.getSuccess()){
            // 同步写Redis，如果失败，不阻塞流程，靠binlog同步保障
        }
        return orderCreateAndConfirmRequest.getOrderId();
    }

    @NotNull
    private OrderCreateAndConfirmRequest getOrderCreateAndConfirmRequest(BuyParam buyParam){
        String userId = StpUtil.getLoginIdAsString();
        // 分布式ID
        String orderId = DistributeID.generateWithSnowflake(BusinessCode.TRADE_ORDER, WorkerIdHolder.WORKER_ID, userId);
        // 创建订单
        OrderCreateAndConfirmRequest orderCreateAndConfirmRequest = new OrderCreateAndConfirmRequest();
        orderCreateAndConfirmRequest.setOrderId(orderId);
        orderCreateAndConfirmRequest.setIdentifier(TOKEN_THREAD_LOCAL.get());
        orderCreateAndConfirmRequest.setBuyerId(userId);
        orderCreateAndConfirmRequest.setGoodsId(buyParam.getGoodsId());
        orderCreateAndConfirmRequest.setGoodsType(GoodsType.valueOf(buyParam.getGoodsType()));
        orderCreateAndConfirmRequest.setItemCount(buyParam.getItemCount());
        BaseGoodsVO goods = goodsFacadeService.getGoods(buyParam.getGoodsId(), GoodsType.valueOf(buyParam.getGoodsType()));
        if (goods == null || !goods.available()){
            throw new TradeException(TradeErrorCode.GOODS_NOT_FOR_SALE);
        }
        orderCreateAndConfirmRequest.setItemPrice(goods.getPrice());
        orderCreateAndConfirmRequest.setSellerId(goods.getSellerId());
        orderCreateAndConfirmRequest.setGoodsName(goods.getGoodsName());
        orderCreateAndConfirmRequest.setGoodsPicUrl(goods.getGoodsPicUrl());
        orderCreateAndConfirmRequest.setSanpshotVersion(goods.getVersion());
        orderCreateAndConfirmRequest.setOrderAmount(orderCreateAndConfirmRequest.getItemPrice().multiply(new BigDecimal(orderCreateAndConfirmRequest.getItemCount())));
        orderCreateAndConfirmRequest.setOperator(UserType.PLATFORM.name());
        orderCreateAndConfirmRequest.setOperatorType(UserType.PLATFORM);
        orderCreateAndConfirmRequest.setOperateTime(new Date());
        return orderCreateAndConfirmRequest;
    }
}
