package com.fw.know.go.document.controller;

import cn.hutool.core.util.StrUtil;
import com.fw.know.go.document.domain.service.RagChatService;
import com.fw.know.go.document.infrastructure.exception.RagErrorCode;
import com.fw.know.go.document.infrastructure.exception.RagException;
import com.fw.know.go.document.param.RagChatParam;
import com.fw.know.go.web.vo.Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @Description RagChatController 问答接口
 * @Date 24/11/2025 上午9:46
 * @Author Leo
 */
@Slf4j
@RestController
@RequestMapping("/rag")
@RequiredArgsConstructor
public class RagChatController {

    private final RagChatService ragChatService;

    /**
     * @Description 问答接口
     * @Date 24/11/2025 上午9:46
     * @Author Leo
     */
    @PostMapping("/chat")
    public Result<String> chat(@Valid @RequestBody RagChatParam param) {
        String question = param.getQuestion();
        if (StrUtil.isBlank(question)) {
            throw new RagException(RagErrorCode.QUESTION_EMPTY);
        }
        String result = ragChatService.chatWithKnowledgeBase(param.getQuestion(), param.getModel());
        return Result.success(result);
    }
}
