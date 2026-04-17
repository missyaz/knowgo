package com.fw.know.go.collection.domain.service.impl;

import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fw.know.go.api.goods.request.GoodsFreezeInventoryRequest;
import com.fw.know.go.collection.domain.entity.Collection;
import com.fw.know.go.collection.domain.entity.CollectionInventoryStream;
import com.fw.know.go.collection.domain.service.CollectionService;
import com.fw.know.go.collection.infrastructure.exception.CollectionException;
import com.fw.know.go.collection.infrastructure.mapper.CollectionInventoryStreamMapperService;
import com.fw.know.go.collection.infrastructure.mapper.CollectionMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.fw.know.go.collection.infrastructure.exception.CollectionErrorCode.*;

/**
 * @Description
 * @Date 30/3/2026 下午1:10
 * @Author Leo
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CollectionServiceImpl extends ServiceImpl<CollectionMapper, Collection> implements CollectionService {

    private final CollectionInventoryStreamMapperService collectionInventoryStreamMapperService;

    private final CollectionMapper collectionMapper;

    @Override
    public Collection queryById(Long collectionId) {
        return this.getById(collectionId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean freezeInventory(GoodsFreezeInventoryRequest request) {
        // 流水校验
        CollectionInventoryStream collectionInventoryStream = collectionInventoryStreamMapperService.selectByIdentifier(request.identifier(), request.eventType().name(),
                request.goodsId());
        if (collectionInventoryStream != null){
            // 已经冻结
            return true;
        }

        // 查询最新的藏品信息
        Collection collection = this.getById(request.goodsId());

        // 新增冻结库存流水
        CollectionInventoryStream stream = new CollectionInventoryStream(collection, request.identifier(), request.eventType(), request.quantity());
        boolean result = collectionInventoryStreamMapperService.save(stream);
        Assert.isTrue(result, () -> new CollectionException(COLLECTION_STREAM_SAVE_FAILED));

        // 核心：冻结库存
        // Tips: 使用数据库保证事务原子性，保证库存的冻结操作是原子的
        // TODO： 故障演练：使用程序应用计算，再更新数据库库存，测试事务的原子性
        int count = collectionMapper.freezeInventory(request.goodsId(), request.quantity());
        Assert.isTrue(count == 1, () -> new CollectionException(COLLECTION_NOT_EXIST));

        return true;
    }
}
