package com.fw.know.go.user.infrastructure.mapper;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fw.know.go.user.domain.entity.UserOperateStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description 用户操作流水映射服务
 * @Date 25/2/2026 下午5:57
 * @Author Leo
 */
@Service
public class UserOperatorStreamMapperService {

    @Autowired
    private UserOperateStreamMapper userOperateStreamMapper;
}
