package com.fw.know.go.box.domain.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fw.know.go.api.goods.request.GoodsFreezeInventoryRequest;
import com.fw.know.go.box.domain.entity.BlindBox;
import com.fw.know.go.box.domain.service.BlindBoxService;
import com.fw.know.go.box.infrastructure.mapper.BlindBoxMapper;
import org.springframework.stereotype.Service;

/**
 * @Description
 * @Date 30/3/2026 下午1:08
 * @Author Leo
 */
@Service
public class BlindBoxServiceImpl extends ServiceImpl<BlindBoxMapper, BlindBox> implements BlindBoxService {

    @Override
    public BlindBox queryId(Long blindBoxId) {
        return this.getById(blindBoxId);
    }

    @Override
    public Boolean freezeInventory(GoodsFreezeInventoryRequest request) {
        return null;
    }
}
