package com.fw.know.go.api.goods.service;

import com.fw.know.go.api.goods.request.GoodsSaleRequest;
import com.fw.know.go.api.goods.response.GoodsSaleResponse;

/**
 * @Description 藏品事务服务接口
 * @Date 7/4/2026 下午3:08
 * @Author Leo
 */
public interface GoodsTransactionFacadeService {

    /**
     * 锁定库存
     * @param request 请求
     * @return 响应
     */
    public GoodsSaleResponse tryDecreaseInventory(GoodsSaleRequest request);

    /**
     * 解锁并扣减库存
     * @param request 请求
     * @return 响应
     */
    public GoodsSaleResponse confirmDecreaseInventory(GoodsSaleRequest request);
}
