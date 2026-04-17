package com.fw.know.go.order.infrastructure.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fw.know.go.order.domain.entity.TradeOrderStream;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Description 订单流水Mapper
 * @Date 17/4/2026 下午1:49
 * @Author Leo
 */
@Mapper
public interface OrderStreamMapper extends BaseMapper<TradeOrderStream> {
}
