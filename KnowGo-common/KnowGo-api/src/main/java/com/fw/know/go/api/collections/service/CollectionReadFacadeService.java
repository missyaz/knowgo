package com.fw.know.go.api.collections.service;

import com.fw.know.go.api.collections.model.CollectionVO;
import com.fw.know.go.base.response.SingleResponse;

/**
 * @Author Leo
 * @Date 2026/3/29 13:31
 * @Description 藏品读取聚合服务
 */
public interface CollectionReadFacadeService {

    /**
     * @Description 根据藏品ID查询藏品详情
     * @param collectionId 藏品ID
     * @return 藏品详情
     */
    SingleResponse<CollectionVO> queryById(Long collectionId);
}
