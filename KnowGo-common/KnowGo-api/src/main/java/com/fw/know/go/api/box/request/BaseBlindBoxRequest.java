package com.fw.know.go.api.box.request;

import com.fw.know.go.base.request.BaseRequest;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @Description
 * @Date 30/3/2026 上午10:40
 * @Author Leo
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BaseBlindBoxRequest extends BaseRequest {

    /**
     * 幂等号
     */
    @NotNull(message = "idenifier is not null")
    private String idenifier;

    /**
     * 盲盒ID
     */
    private Long blindBoxId;
}
