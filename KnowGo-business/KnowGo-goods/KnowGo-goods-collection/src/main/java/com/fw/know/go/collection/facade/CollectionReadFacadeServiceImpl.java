package com.fw.know.go.collection.facade;

import com.fw.know.go.api.collections.model.CollectionVO;
import com.fw.know.go.api.collections.service.CollectionReadFacadeService;
import com.fw.know.go.base.response.SingleResponse;
import com.fw.know.go.collection.domain.entity.Collection;
import com.fw.know.go.collection.domain.entity.convertor.CollectionConvertor;
import com.fw.know.go.collection.domain.service.CollectionService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.fw.know.go.collection.infrastructure.exception.CollectionErrorCode.COLLECTION_NOT_EXIST;

/**
 * @Author Leo
 * @Date 2026/3/29 19:26
 * @Description
 */
@Service
@DubboService(version = "1.0.0")
public class CollectionReadFacadeServiceImpl implements CollectionReadFacadeService {

    @Autowired
    private CollectionService collectionService;

    @Override
    public SingleResponse<CollectionVO> queryById(Long collectionId) {
        Collection collection = collectionService.queryById(collectionId);
        if (collection == null) {
            return SingleResponse.fail(COLLECTION_NOT_EXIST.getCode(), COLLECTION_NOT_EXIST.getMessage());
        }
        // TODO: 库存查询

        CollectionVO collectionVO = CollectionConvertor.getInstance().mapToVo(collection);
        // TODO: 库存都先置为10
        collectionVO.setInventory(10L);
        collectionVO.setState(collection.getState(), collection.getSaleTime(), 0L);

        return SingleResponse.of(collectionVO);
    }
}
