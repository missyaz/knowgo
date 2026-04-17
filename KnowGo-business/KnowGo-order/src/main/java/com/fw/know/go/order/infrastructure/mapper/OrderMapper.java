package com.fw.know.go.order.infrastructure.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fw.know.go.order.domain.entity.TradeOrder;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Description 订单
 * @Date 17/4/2026 下午1:49
 * @Author Leo
 */
@Mapper
public interface OrderMapper extends BaseMapper<TradeOrder> {

    /**
     * 根据幂等号查询订单
     * @param identifier 幂等号
     * @param buyerId 买家ID
     * @return 订单
     */
    TradeOrder selectByIdentifier(String identifier, String buyerId);
}
