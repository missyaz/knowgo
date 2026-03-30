package com.fw.know.go.collection.domain.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fw.know.go.collection.domain.entity.Collection;
import com.fw.know.go.collection.domain.service.CollectionService;
import com.fw.know.go.collection.infrastructure.mapper.CollectionMapper;
import org.springframework.stereotype.Service;

/**
 * @Description
 * @Date 30/3/2026 下午1:10
 * @Author Leo
 */
@Service
public class CollectionServiceImpl extends ServiceImpl<CollectionMapper, Collection> implements CollectionService {

    @Override
    public Collection queryById(Long collectionId) {
        return this.getById(collectionId);
    }
}
