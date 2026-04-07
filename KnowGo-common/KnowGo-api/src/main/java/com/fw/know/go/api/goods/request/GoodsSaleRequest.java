package com.fw.know.go.api.goods.request;

import com.fw.know.go.api.collections.constant.GoodsSaleBizType;
import com.fw.know.go.api.goods.constant.GoodsEvent;
import com.fw.know.go.api.order.request.OrderCreateAndConfirmRequest;
import lombok.*;

import java.math.BigDecimal;

/**
 * @Description
 * @Date 7/4/2026 下午2:31
 * @Author Leo
 */
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class GoodsSaleRequest extends BaseGoodsRequest{

    /**
     * 藏品名称
     */
    private String name;

    /**
     * 藏品封面
     */
    private String cover;

    /**
     * 购入价格
     */
    private BigDecimal purchasePrice;

    /**
     * 持有人Id
     */
    private String userId;

    /**
     * 销售数量
     */
    private Integer quantity;

    /**
     * 业务单号
     */
    private String bizNo;

    /**
     * 业务类型
     *
     * @see GoodsSaleBizType
     */
    private String bizType;

    @Override
    public GoodsEvent getEventType() {
        return GoodsEvent.SALE;
    }

    public GoodsSaleRequest(OrderCreateAndConfirmRequest orderCreateAndConfirmRequest){
        this.userId = orderCreateAndConfirmRequest.getBuyerId();
        this.quantity = orderCreateAndConfirmRequest.getItemCount();
        this.bizNo = orderCreateAndConfirmRequest.getOrderId();
        super.setGoodsId(Long.valueOf(orderCreateAndConfirmRequest.getGoodsId()));
        super.setGoodsType(orderCreateAndConfirmRequest.getGoodsType().name());
        super.setIdentifier(orderCreateAndConfirmRequest.getOrderId());
    }
}
