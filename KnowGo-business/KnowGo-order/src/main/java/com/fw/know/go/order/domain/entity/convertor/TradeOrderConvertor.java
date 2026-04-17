package com.fw.know.go.order.domain.entity.convertor;

import com.fw.know.go.api.order.request.OrderCreateRequest;
import com.fw.know.go.order.domain.entity.TradeOrder;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.factory.Mappers;

/**
 * @Description
 * @Date 2/4/2026 上午9:49
 * @Author Leo
 */
@Mapper(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface TradeOrderConvertor {

    TradeOrderConvertor INSTANCE = Mappers.getMapper(TradeOrderConvertor.class);

    /**
     * 转换为实体
     * @param request 请求
     * @return 订单
     */
    public TradeOrder mapToEntity(OrderCreateRequest request);
}
