package com.fw.know.go.collection.infrastructure.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fw.know.go.collection.domain.entity.Collection;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Description
 * @Date 30/3/2026 下午1:11
 * @Author Leo
 */
@Mapper
public interface CollectionMapper extends BaseMapper<Collection> {
    /**
     * 冻结库存
     * @param collectionId 藏品ID
     * @param quantity 冻结数量
     * @return 冻结成功数量
     */
    int freezeInventory(Long collectionId, Integer quantity);
}
