package com.fw.know.go.api.goods.request;

import com.fw.know.go.api.goods.constant.GoodsEvent;
import com.fw.know.go.base.request.BaseRequest;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @Description 通用的商品请求参数
 * @Date 7/4/2026 下午2:26
 * @Author Leo
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class BaseGoodsRequest extends BaseRequest {

    /**
     * 幂等号
     */
    @NotNull(message = "identifier is not null")
    private String identifier;

    /**
     * 藏品ID
     */
    private Long goodsId;

    /**
     * 藏品类型
     *
     * @link GoodsType
     */
    private String goodsType;

    /**
     * 获取事件类型
     * @return 事件类型
     */
    public abstract GoodsEvent getEventType();
}
