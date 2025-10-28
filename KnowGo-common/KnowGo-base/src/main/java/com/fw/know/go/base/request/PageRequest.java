package com.fw.know.go.base.request;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

/**
 * @Classname PageRequest
 * @Description 分页通用入参
 * @Date 28/10/2025 下午1:47
 * @Author Leo
 */
@Getter
@Setter
public class PageRequest extends BaseRequest{

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 当前页码
     */
    private int currentPage;

    /**
     * 每页数量
     */
    private int pageSize;
}
