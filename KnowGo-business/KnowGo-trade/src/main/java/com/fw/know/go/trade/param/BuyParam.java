package com.fw.know.go.trade.param;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * @Description
 * @Date 2/4/2026 上午10:27
 * @Author Leo
 */
@Getter
@Setter
public class BuyParam {

    @NotNull(message = "goodsId is null")
    private String goodsId;

    @NotNull(message = "goodsType is null ")
    private String goodsType;

    /**
     * 商品数量
     */
    @Min(value = 1)
    private int itemCount;
}
