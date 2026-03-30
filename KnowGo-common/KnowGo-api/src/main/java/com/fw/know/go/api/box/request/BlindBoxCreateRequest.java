package com.fw.know.go.api.box.request;

import lombok.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @Description
 * @Date 30/3/2026 上午10:39
 * @Author Leo
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class BlindBoxCreateRequest extends BaseBlindBoxRequest{

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
     * 盲盒价格
     */
    private BigDecimal price;

    /**
     * 盲盒数量
     */
    private Long quantity;

    /**
     * 盲盒创建时间
     */
    private Date createTime;

    /**
     * 盲盒发售时间
     */
    private Date saleTime;

    /**
     * 盲盒分配规则
     */
    private String allocateRule;

    /**
     * 盲盒创建者Id
     */
    private String creatorId;

    /**
     * 盲盒是否预约
     */
    private Boolean canBook;

    /**
     * 盲盒预约开始时间
     */
    private Date bookStartTime;

    /**
     * 盲盒预约结束时间
     */
    private Date bookEndTime;

    /**
     * 盲盒商品列表
     */
    private List<BlindBoxItemCreateRequest> blindBoxItemCreateRequests;

}
