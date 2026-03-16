package com.fw.know.go.api.user.request.condition;

import lombok.*;

import java.io.Serial;

/**
 * @Description 用户ID查询条件
 * @Date 16/3/2026 下午1:14
 * @Author Leo
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserIdQueryCondition implements UserQueryCondition{

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 用户Id
     */
    private Long userId;
}
