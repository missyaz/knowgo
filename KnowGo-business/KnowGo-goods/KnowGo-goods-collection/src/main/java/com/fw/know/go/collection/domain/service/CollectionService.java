package com.fw.know.go.collection.domain.service;

import com.baomidou.mybatisplus.extension.service.IService;
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
}
