package com.fw.know.go.user.domain.entity.convertor;

import com.fw.know.go.api.user.response.data.UserInfo;
import com.fw.know.go.user.domain.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.factory.Mappers;

/**
 * @Description
 * @Date 16/3/2026 下午4:23
 * @Author Leo
 */
@Mapper(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface UserConvertior {

    UserConvertior INSTANCE = Mappers.getMapper(UserConvertior.class);

    /**
     * 将User对象转换为UserInfo对象的方法
     * @param request 包含用户信息的User对象
     * @return 转换后的UserInfo对象
     */
    @Mapping(target = "userId", source = "request.id")
    @Mapping(target = "createTime", source = "request.gmtCreate")
    public UserInfo mapToVo(User request);
}
