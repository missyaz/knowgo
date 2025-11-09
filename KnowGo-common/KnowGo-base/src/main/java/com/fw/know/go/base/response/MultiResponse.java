package com.fw.know.go.base.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * @Classname MultiResponse
 * @Description 多个返回结果的响应对象
 * @Date 28/10/2025 下午2:00
 * @Author Leo
 */
@Getter
@Setter
@ToString
public class MultiResponse<T> extends BaseResponse {

    @Serial
    private static final long serialVersionUID = 1L;

    private List<T> dataList;

    public static <T> MultiResponse<T> of(List<T> dataList){
        MultiResponse<T> multiResponse = new MultiResponse<>();
        multiResponse.setDataList(dataList);
        multiResponse.setSuccess(true);
        return multiResponse;
    }

}
