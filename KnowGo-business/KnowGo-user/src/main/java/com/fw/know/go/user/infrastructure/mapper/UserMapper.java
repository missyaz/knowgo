package com.fw.know.go.user.infrastructure.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fw.know.go.user.domain.entity.User;
import jakarta.validation.constraints.NotNull;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Description
 * @Date 25/2/2026 下午4:21
 * @Author Leo
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}
