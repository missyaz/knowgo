package com.fw.know.go.box.domain.entity;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fw.know.go.api.box.constant.BlindAllotBoxRule;
import com.fw.know.go.api.box.constant.BlindBoxStateEnum;
import com.fw.know.go.api.box.request.BlindBoxCreateRequest;
import com.fw.know.go.box.domain.entity.convertor.BlindBoxConvertor;
import com.fw.know.go.datasource.domain.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Description 盲盒信息
 * @Date 30/3/2026 上午10:29
 * @Author Leo
 */
@Getter
@Setter
@TableName(value = "blind_box", schema = "goods_schema")
public class BlindBox extends BaseEntity {

    /**
     * 盲盒名称
     */
    private String name;

    /**
     * 盲盒封面
     */
    private String cover;

    /**
     * 盲盒详情
     */
    private String detail;

    /**
     * 价格
     */
    private BigDecimal price;

    /**
     * 盲盒数量
     */
    private Long quantity;

    /**
     * 幂等号
     */
    private String identifier;

    /**
     * 状态
     */
    private BlindBoxStateEnum state;

    /**
     * 可售库存
     */
    private Long saleableInventory;

    /**
     * 已占库存
     * @deprecated 这个字段不再使用，详见 CollecitonSerivce.confirmSale
     */
    @Deprecated
    private Long occupiedInventory;

    /**
     * 冻结库存
     */
    private Long frozenInventory;

    /**
     * 盲盒创建时间
     */
    private Date createTime;

    /**
     * 盲盒发售时间
     */
    private Date saleTime;

    /**
     * 上链时间
     */
    private Date syncChainTime;

    /**
     * 盲盒分配规则
     */
    private BlindAllotBoxRule allocateRule;

    /**
     * 盲盒创建者ID
     */
    private String creatorId;

    /**
     * 藏品配置
     */
    private String collectionConfigs;

    /**
     * 预约开始时间
     */
    private Date bookStartTime;

    /**
     * 预约结束时间
     */
    private Date bookEndTime;

    /**
     * 是否预约
     */
    private Integer canBook;

    public static BlindBox create(BlindBoxCreateRequest request){
        BlindBox blindBox = BlindBoxConvertor.INSTANCE.mapToEntity(request);
        blindBox.setOccupiedInventory(0L);
        blindBox.setSaleableInventory(request.getQuantity());
        blindBox.setState(BlindBoxStateEnum.INIT);
        blindBox.setCollectionConfigs(JSON.toJSONString(request.getBlindBoxItemCreateRequests()));
        blindBox.setLockVersion(1);
        return blindBox;
    }
}
