package com.fw.know.go.api.collections.request;

import com.fw.know.go.base.request.BaseRequest;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @Author Leo
 * @Date 2026/3/29 19:53
 * @Description
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class BaseCollectionRequest extends BaseRequest {

    /**
     * 幂等号
     */
    @NotNull(message = "identifier is not null")
    private String identifier;

    /**
     * 藏品ID
     */
    private Long collectionId;
}
