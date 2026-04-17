package com.fw.know.go.collection.domain.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fw.know.go.api.goods.request.GoodsFreezeInventoryRequest;
import com.fw.know.go.collection.domain.entity.Collection;

/**
 * @Author Leo
 * @Date 2026/3/29 19:34
 * @Description 藏品服务
 */
public interface CollectionService extends IService<Collection> {

    /**
     * 根据ID查询藏品
     * @param collectionId 藏品ID
     * @return 藏品
     */
    public Collection queryById(Long collectionId);

    /**
     * 冻结库存
     * @param request 冻结库存请求
     * @return true/false
     */
    public Boolean freezeInventory(GoodsFreezeInventoryRequest request);
}
