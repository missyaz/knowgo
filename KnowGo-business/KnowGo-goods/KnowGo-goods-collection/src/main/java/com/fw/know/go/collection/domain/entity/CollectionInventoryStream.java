package com.fw.know.go.collection.domain.entity;

import com.fw.know.go.api.collections.constant.CollectionStateEnum;
import com.fw.know.go.api.goods.constant.GoodsEvent;
import com.fw.know.go.datasource.domain.entity.BaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * @Description 藏品库存流水信息
 * @Date 16/4/2026 下午5:04
 * @Author Leo
 */
@Getter
@Setter
@NoArgsConstructor
public class CollectionInventoryStream extends BaseEntity {

    /**
     * 流水类型
     */
    private GoodsEvent streamType;

    /**
     * 幂等号
     */
    private String identifier;

    /**
     * 变更数量
     */
    private Integer changeQuantity;

    /**
     * 藏品ID
     */
    private Long collectionId;

    /**
     * 价格
     */
    private BigDecimal price;

    /**
     * 藏品数量
     */
    private Integer quantity;

    /**
     * 可售库存
     */
    private Long saleableInventory;

    /**
     * 已占库存
     * @deprecated
     */
    @Deprecated
    private Long occupiedInventory;

    /**
     * 冻结库存
     */
    private Long frozenInventory;

    /**
     * 状态
     */
    private CollectionStateEnum state;

    /**
     * 扩展信息
     */
    private String extendInfo;

    public CollectionInventoryStream(Collection collection, String identifier, GoodsEvent streamType, Integer quantity){
        this.collectionId = collection.getId();
        this.price = collection.getPrice();
        this.quantity = collection.getQuantity();
        this.saleableInventory = collection.getSaleableInventory();
        this.frozenInventory = collection.getFrozenInventory();
        this.state = collection.getState();
        this.streamType = streamType;
        this.identifier = identifier;
        this.changeQuantity = quantity;
        super.setLockVersion(collection.getLockVersion());
        super.setDeleted(collection.getDeleted());
    }
}
