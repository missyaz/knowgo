package com.fw.know.go.user.infrastructure.mapper;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fw.know.go.user.domain.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description 用户查询服务，主要封装用户查询相关的数据库操作
 * @Date 25/2/2026 下午5:30
 * @Author Leo
 */
@Service
public class UserMapperService {

    @Autowired
    private UserMapper userMapper;

    public User findByInviteCode(String inviteCode){
        if (StrUtil.isBlankIfStr(inviteCode)){
            return null;
        }
        return userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getInviteCode, inviteCode));
    }

    public User findByTelephone(String telephone){
        if (StrUtil.isBlankIfStr(telephone)){
            return null;
        }
        return userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getTelephone, telephone));
    }

    public User findByNickName(String nickName) {
        if (StrUtil.isBlankIfStr(nickName)){
            return null;
        }
        return userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getNickName, nickName));
    }
}
