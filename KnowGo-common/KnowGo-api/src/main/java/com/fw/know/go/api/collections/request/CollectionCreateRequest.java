package com.fw.know.go.api.collections.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author Leo
 * @Date 2026/3/29 19:52
 * @Description
 */
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CollectionCreateRequest extends BaseCollectionRequest{

    /**
     * 藏品名称
     */
    private String name;

    /**
     * 藏品封面
     */
    private String cover;

    /**
     * 藏品详情
     */
    private String detail;

    /**
     * 藏品价格
     */
    private BigDecimal price;

    /**
     * 藏品数量
     */
    private Long quantity;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 发售时间
     */
    private Date saleTime;

    /**
     * 藏品创建者ID
     */
    private String creatorId;

    /**
    * 藏品是否预约
     */
    @NotNull(message = "藏品是否预约不能为空")
    private Integer canBook;

    /**
     * 预约开始时间
     */
    private Date bookStartTime;

    /**
    * 预约结束时间
     */
    private Date bookEndTime;
}
