package com.fw.know.go.user.domain.service;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fw.know.go.api.user.constant.UserOperateTypeEnum;
import com.fw.know.go.user.domain.entity.User;
import com.fw.know.go.user.domain.entity.UserOperateStream;
import com.fw.know.go.user.infrastructure.mapper.UserOperateStreamMapper;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @Description 用户操作流水服务实现类
 * @Date 27/2/2026 上午9:46
 * @Author Leo
 */
@Service
public class UserOperatorStreamService extends ServiceImpl<UserOperateStreamMapper, UserOperateStream>  {

    public Long insertStream(User user, UserOperateTypeEnum operateTypeEnum){
        UserOperateStream stream = new UserOperateStream();
        stream.setUserId(String.valueOf(user.getId()));
        stream.setOperateTime(new Date());
        stream.setType(operateTypeEnum.name());
        stream.setParam(JSON.toJSONString(user));
        boolean result = save(stream);
        if (result){
            return stream.getId();
        }
        return null;
    }
}
