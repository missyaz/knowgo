package com.fw.know.go.document.param;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Description RagChatParam 问答参数
 * @Date 24/11/2025 上午9:50
 * @Author Leo
 */
@Data
public class RagChatParam {

    /**
     * 问题
     */
    private String question;

     /**
      * 模型
      */
    private String model;
}
