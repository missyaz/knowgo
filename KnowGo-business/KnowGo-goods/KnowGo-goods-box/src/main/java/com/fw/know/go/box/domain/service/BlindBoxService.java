package com.fw.know.go.box.domain.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fw.know.go.api.goods.request.GoodsFreezeInventoryRequest;
import com.fw.know.go.box.domain.entity.BlindBox;

/**
 * @Description
 * @Date 30/3/2026 上午10:28
 * @Author Leo
 */
public interface BlindBoxService extends IService<BlindBox> {

    /**
     * 查询
     * @param blindBoxId 盲盒ID
     * @return 盲盒信息
     */
    BlindBox queryId(Long blindBoxId);

    /**
     * 冻结库存
     * @param request 冻结库存请求
     * @return true/false
     */
    public Boolean freezeInventory(GoodsFreezeInventoryRequest request);
}
