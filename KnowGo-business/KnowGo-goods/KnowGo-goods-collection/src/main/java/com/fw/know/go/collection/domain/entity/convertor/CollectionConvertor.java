package com.fw.know.go.collection.domain.entity.convertor;

import com.fw.know.go.api.collections.constant.CollectionStateEnum;
import com.fw.know.go.api.collections.model.CollectionVO;
import com.fw.know.go.api.collections.request.CollectionCreateRequest;
import com.fw.know.go.api.goods.constant.GoodsState;
import com.fw.know.go.collection.domain.entity.Collection;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.factory.Mappers;

import java.util.Date;

/**
 * @Description
 * @Date 30/3/2026 上午9:33
 * @Author Leo
 */
@Mapper(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface CollectionConvertor {

    class Holder {
        static final CollectionConvertor INSTANCE = Mappers.getMapper(CollectionConvertor.class);
    }

    static CollectionConvertor getInstance() {
        return Holder.INSTANCE;
    }

//    CollectionConvertor INSTANCE = Mappers.getMapper(CollectionConvertor.class);

    @Mapping(target = "inventory", source = "request.saleableInventory")
    @Mapping(target = "state", expression = "java(setState(request.getState(), request.getSaleTime(), request" +
            ".getSaleableInventory()))")
    CollectionVO mapToVo(Collection request);

    /**
     * 设置商品状态的方法
     * 这是一个默认方法，用于根据传入的状态、销售时间和可销售库存来设置商品状态
     *
     * @param state 商品状态枚举值，表示商品的不同状态
     * @param saleTime 销售时间，用于判断商品是否在销售期内
     * @param saleableInventory 可销售库存数量，表示商品当前可销售的库存
     * @return 返回一个GoodsState对象，表示商品最终的状态
     */
    public default GoodsState setState(CollectionStateEnum state, Date saleTime, Long saleableInventory) {
        // 调用CollectionVO类的getState静态方法，传入参数获取商品状态
        return CollectionVO.getState(state, saleTime, saleableInventory);
    }

    Collection mapToEntity(CollectionCreateRequest request);
}
