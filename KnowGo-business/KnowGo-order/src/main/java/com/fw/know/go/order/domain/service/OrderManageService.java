package com.fw.know.go.order.domain.service;

import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fw.know.go.api.order.request.OrderCreateRequest;
import com.fw.know.go.api.order.response.OrderResponse;
import com.fw.know.go.base.exception.BizException;
import com.fw.know.go.base.exception.RepoErrorCode;
import com.fw.know.go.order.domain.entity.TradeOrder;
import com.fw.know.go.order.domain.entity.TradeOrderStream;
import com.fw.know.go.order.infrastructure.mapper.OrderMapper;
import com.fw.know.go.order.infrastructure.mapper.OrderStreamMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Description 订单服务
 * @Date 17/4/2026 下午1:47
 * @Author Leo
 */
@Service
@RequiredArgsConstructor
public class OrderManageService extends ServiceImpl<OrderMapper, TradeOrder>{

   private final OrderMapper orderMapper;

   private final OrderStreamMapper orderStreamMapper;

   /**
     * 创建订单
     * @param request 创建订单请求
     * @return 响应
   */
   @Transactional(rollbackFor = Exception.class)
   public OrderResponse create(OrderCreateRequest request){
      TradeOrder existOrder = orderMapper.selectByIdentifier(request.getIdentifier(), request.getBuyerId());
      if (existOrder != null){
         // 不为空，直接返回
         return new OrderResponse.OrderResponseBuilder().orderId(existOrder.getOrderId()).buildSuccess();
      }

      // 为空，创建订单
      TradeOrder tradeOrder = doCreate(request);

      // 发布事件
      return new OrderResponse.OrderResponseBuilder().orderId(tradeOrder.getOrderId()).buildSuccess();
   }

   private TradeOrder doCreate(OrderCreateRequest request){
      TradeOrder tradeOrder = TradeOrder.createOrder(request);

      boolean result = save(tradeOrder);
      Assert.isTrue(result, () -> new BizException(RepoErrorCode.INSERT_FAILED));

      // 记录流水
      TradeOrderStream tradeOrderStream = new TradeOrderStream(tradeOrder, request.getOrderEvent(), request.getIdentifier());
      result = orderStreamMapper.insert(tradeOrderStream) == 1;
      Assert.isTrue(result, () -> new BizException(RepoErrorCode.INSERT_FAILED));
      return tradeOrder;
   }
}
