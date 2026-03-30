package com.fw.know.go.api.box.request;

import com.fw.know.go.api.collections.constant.CollectionRarity;
import com.fw.know.go.base.request.BaseRequest;
import lombok.*;

import java.math.BigDecimal;

/**
 * @Description
 * @Date 30/3/2026 上午10:49
 * @Author Leo
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class BlindBoxItemCreateRequest extends BaseRequest {

    /**
     * 藏品名称
     */
    private String collectionName;

    /**
     * 藏品封面
     */
    private String collectionCover;

    /**
     * 藏品详情
     */
    private String collectionDetail;

    /**
     * 参考价格
     */
    private BigDecimal referencePrice;

    /**
     * 稀有度
     */
    private CollectionRarity rarity;

    /**
     * 数量
     */
    private Long quantity;
}
