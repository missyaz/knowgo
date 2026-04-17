package com.fw.know.go.collection.infrastructure.mapper;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fw.know.go.collection.domain.entity.CollectionInventoryStream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @Description
 * @Date 16/4/2026 下午5:46
 * @Author Leo
 */
@Service
@RequiredArgsConstructor
public class CollectionInventoryStreamMapperService extends ServiceImpl<CollectionInventoryStreamMapper,CollectionInventoryStream> {

    private final CollectionInventoryStreamMapper collectionInventoryStreamMapper;


    /**
     * 根据标识符查询库存流水
     * @param identifier 幂等号
     * @param streamType 流水类型
     * @param collectionId 藏品ID
     * @return 库存流水
     */
    public CollectionInventoryStream selectByIdentifier(String identifier, String streamType, Long collectionId) {
        LambdaQueryWrapper<CollectionInventoryStream> queryWrapper = new LambdaQueryWrapper<>();
        if (StrUtil.isNotEmpty(identifier)) {
            queryWrapper.eq(CollectionInventoryStream::getIdentifier, identifier);
        }
        if (StrUtil.isNotEmpty(streamType)) {
            queryWrapper.eq(CollectionInventoryStream::getStreamType, streamType);
        }
        if (ObjectUtil.isNotEmpty(collectionId)) {
            queryWrapper.eq(CollectionInventoryStream::getCollectionId, collectionId);
        }
        return this.getOne(queryWrapper);
    }
}
