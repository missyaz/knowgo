package com.fw.know.go.box.domain.entity.convertor;

import cn.hutool.core.util.BooleanUtil;
import com.fw.know.go.api.box.constant.BlindBoxStateEnum;
import com.fw.know.go.api.box.model.BlindBoxVO;
import com.fw.know.go.api.box.request.BlindBoxCreateRequest;
import com.fw.know.go.api.goods.constant.GoodsState;
import com.fw.know.go.box.domain.entity.BlindBox;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.factory.Mappers;

import java.util.Date;

/**
 * @Description
 * @Date 30/3/2026 上午11:04
 * @Author Leo
 */
@Mapper(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface BlindBoxConvertor {

    BlindBoxConvertor INSTANCE = Mappers.getMapper(BlindBoxConvertor.class);

    @Mapping(target = "inventory", source = "request.saleableInventory")
    @Mapping(target = "state", expression = "java(setState(request.getState(), request.getSaleTime(), request.getSaleableInventory()))")
    public BlindBoxVO mapToVo(BlindBox request);

    /**
     * 设置状态
     * @param state 盲盒状态
     * @param saleTime 上架时间
     * @param saleableInventory 可售库存
     * @return 商品状态
     */
    public default GoodsState setState(BlindBoxStateEnum state, Date saleTime, Long saleableInventory) {
        return BlindBoxVO.getState(state, saleTime, saleableInventory);
    }

    /**
     * 转换为实体
     *
     * @param request 创建请求类
     * @return 实体
     */
    @Mapping(target = "canBook", source = "canBook", qualifiedByName = "mapBooleanToInteger")
    public BlindBox mapToEntity(BlindBoxCreateRequest request);

    /**
     * 将布尔值转换为对应的整数值
     * @param value 要转换的布尔值
     * @return 如果value为true则返回1，为false则返回0
     */
    @Named("mapBooleanToInteger")
    default Integer mapBooleanToInteger(Boolean value) {
        // 使用BooleanUtil工具类将布尔值转换为整数值
        return BooleanUtil.toInteger(value);
    }
}
