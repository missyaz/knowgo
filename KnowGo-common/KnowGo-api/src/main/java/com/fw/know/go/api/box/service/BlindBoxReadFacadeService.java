package com.fw.know.go.api.box.service;

import com.fw.know.go.api.box.model.BlindBoxVO;
import com.fw.know.go.base.response.SingleResponse;

/**
 * @Description 盲盒门面服务
 * @Date 30/3/2026 上午9:53
 * @Author Leo
 */
public interface BlindBoxReadFacadeService {

    /**
     * 根据盲盒ID查询盲盒信息的接口方法
     *
     * @param blindBoxId 盲盒的唯一标识ID
     * @return 返回一个包含盲盒详细信息的SingleResponse对象，泛型为BlindBoxVO
     */
    SingleResponse<BlindBoxVO> queryById(Long blindBoxId);
}
