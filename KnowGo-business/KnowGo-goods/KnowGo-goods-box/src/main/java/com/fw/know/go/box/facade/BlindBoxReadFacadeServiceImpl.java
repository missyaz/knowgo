package com.fw.know.go.box.facade;

import com.fw.know.go.api.box.model.BlindBoxVO;
import com.fw.know.go.api.box.service.BlindBoxReadFacadeService;
import com.fw.know.go.base.response.SingleResponse;
import com.fw.know.go.box.domain.entity.BlindBox;
import com.fw.know.go.box.domain.entity.convertor.BlindBoxConvertor;
import com.fw.know.go.box.domain.service.BlindBoxService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import static com.fw.know.go.box.infrastructure.exception.BlindBoxErrorCode.BLIND_BOX_NOT_EXIST;

/**
 * @Description 盲盒服务
 * @Date 30/3/2026 上午10:14
 * @Author Leo
 */
@Slf4j
@DubboService(version = "1.0.0")
public class BlindBoxReadFacadeServiceImpl implements BlindBoxReadFacadeService {

    @Autowired
    private BlindBoxService blindBoxService;

    @Override
    public SingleResponse<BlindBoxVO> queryById(Long blindBoxId) {
        BlindBox blindBox = blindBoxService.queryId(blindBoxId);
        if (blindBox == null) {
            return SingleResponse.fail(BLIND_BOX_NOT_EXIST.getCode(), BLIND_BOX_NOT_EXIST.getMessage());
        }

        // TODO: 库存查询

        BlindBoxVO blindBoxVO = BlindBoxConvertor.INSTANCE.mapToVo(blindBox);
        blindBoxVO.setInventory(0L);
        blindBoxVO.setState(blindBox.getState(), blindBox.getSaleTime(), 0L);
        return SingleResponse.of(blindBoxVO);
    }
}
