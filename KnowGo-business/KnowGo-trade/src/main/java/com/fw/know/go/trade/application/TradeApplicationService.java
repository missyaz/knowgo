package com.fw.know.go.trade.application;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Assert;
import com.fw.know.go.api.goods.request.GoodsSaleRequest;
import com.fw.know.go.api.goods.service.GoodsTransactionFacadeService;
import com.fw.know.go.api.order.OrderTransactionFacadeService;
import com.fw.know.go.api.order.request.OrderConfirmRequest;
import com.fw.know.go.api.order.request.OrderCreateAndConfirmRequest;
import com.fw.know.go.api.order.response.OrderResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @Description
 * @Date 7/4/2026 下午2:08
 * @Author Leo
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TradeApplicationService {

    public static final int MAX_RETRY_TIMES = 2;

    private final GoodsTransactionFacadeService goodsTransactionFacadeService;

    private final OrderTransactionFacadeService orderTransactionFacadeService;

    /**
     * 普通交易，基于TCC实现分布式一致性
     * <p>
     *     Try -> Confirm : Try成功，执行Confirm
     *     Try -> Cancel : Try失败，执行Cancel
     *     Try -> Confirm -> Cancel : Try成功，Confirm失败，执行Cancel
     * </p>
     * @param orderCreateAndConfirmRequest 订单创建确认请求
     * @return 订单响应
     */
    public OrderResponse normalBuy(OrderCreateAndConfirmRequest orderCreateAndConfirmRequest){

        boolean isTrySuccess = true;

        // Try
        try {
            GoodsSaleRequest goodsSaleRequest = new GoodsSaleRequest(orderCreateAndConfirmRequest);
            Boolean result = goodsTransactionFacadeService.tryDecreaseInventory(goodsSaleRequest).getSuccess();
            Assert.isTrue(result, "decrease inventory failed");

            result = orderTransactionFacadeService.tryOrder(orderCreateAndConfirmRequest, "normalBuy").getSuccess();
            Assert.isTrue(result, "order create failed");
        } catch (Exception e) {
            isTrySuccess = false;
            log.error("normalBuy try failed, ", e);
        }

        // Try失败，发【废单消息】，异步进行逆向补偿
        if (!isTrySuccess){
            // 消息监听： NormalBuyMsgListener
            // TODO: 发送废单消息
        }

        // Confirm
        boolean isConfirmSuccess = false;
        int retryConfirmCount = 0;

        // 最大努力执行，失败最多尝试2次，（Dubbo也会有重试机制，在服务突然不可用，超时等情况下会重试2次）
        while (!isConfirmSuccess && retryConfirmCount < MAX_RETRY_TIMES){
            try {
                GoodsSaleRequest goodsSaleRequest = new GoodsSaleRequest(orderCreateAndConfirmRequest);
                isConfirmSuccess = goodsTransactionFacadeService.confirmDecreaseInventory(goodsSaleRequest).getSuccess();
                Assert.isTrue(isConfirmSuccess, "confirmDecreaseInventory failed");

                OrderConfirmRequest orderConfirmRequest = new OrderConfirmRequest();
                BeanUtil.copyProperties(orderCreateAndConfirmRequest, orderConfirmRequest);
                isConfirmSuccess = orderTransactionFacadeService.confirmOrder(orderConfirmRequest, "normalBuy").getSuccess();
                Assert.isTrue(isConfirmSuccess, "confirmOrder failed");
            }
            catch (Exception e){
                retryConfirmCount++;
                isConfirmSuccess = false;
                log.error("normalBuy confirm failed, ", e);
            }
        }

        // Confirm失败，发【疑似废单消息】进行延迟检查
        if (!isConfirmSuccess){
            // 消息监听： NormalBuyMsgListener
            // TODO: 发送废单消息
        }

        return new OrderResponse.OrderResponseBuilder().orderId(orderCreateAndConfirmRequest.getOrderId()).buildSuccess();
    }
}
