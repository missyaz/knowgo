package com.fw.know.go.collection.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fw.know.go.api.collections.constant.CollectionStateEnum;
import com.fw.know.go.api.collections.request.CollectionCreateRequest;
import com.fw.know.go.collection.domain.entity.convertor.CollectionConvertor;
import com.fw.know.go.datasource.domain.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author Leo
 * @Date 2026/3/29 19:35
 * @Description 藏品实体
 */
@Getter
@Setter
@ToString
@TableName(value = "collection", schema = "goods_schema")
public class Collection extends BaseEntity {

    /**
     * 藏品名称
     */
    private String name;

    /**
     * 藏品封面
     */
    private String cover;

    /**
     * 藏品分类ID
     */
    private String classId;

    /**
     * 藏品价格
     */
    private BigDecimal price;

    /**
     * 藏品数量
     */
    private Integer quantity;

    /**
     * 藏品详情
     */
    private String detail;

    /**
     * 可售库存
     */
    private Long saleableInventory;

    /**
     * 已占库存
     */
    private Long occupiedInventory;

    /**
     * 被冻结库存
     */
    private Long frozenInventory;

    /**
     * 状态
     */
    private CollectionStateEnum state;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 发售时间
     */
    private Date saleTime;

    /**
     * 藏品上链时间
     */
    private Date syncChainTime;

    /**
     * 创建人ID
     */
    private String creatorId;

    /**
     * 版本
     */
    private Integer version;

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

    public static Collection create(CollectionCreateRequest request){
        Collection collection = CollectionConvertor.getInstance().mapToEntity(request);
        collection.setOccupiedInventory(0L);
        collection.setSaleableInventory(request.getQuantity());
        collection.setState(CollectionStateEnum.INIT);
        collection.setVersion(1);
        return collection;
    }

    public Collection remove(){
        this.state = CollectionStateEnum.REMOVED;
        return this;
    }
}
