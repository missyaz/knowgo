package com.fw.know.go.collection.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fw.know.go.api.collections.constant.CollectionStateEnum;
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
@TableName(value = "collection")
public class Collection {

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

    public static Collection create(){

    }
}
