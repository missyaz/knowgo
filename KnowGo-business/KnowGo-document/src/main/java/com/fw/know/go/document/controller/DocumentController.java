package com.fw.know.go.document.controller;

import com.fw.know.go.file.FileService;
import com.fw.know.go.web.vo.Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import static com.fw.know.go.document.infrastructure.exception.DocumentErrorCode.DOCUMENT_EMPTY;

/**
 * @Classname DocumentController
 * @Description 文档控制器
 * @Date 29/10/2025 下午1:16
 * @Author Leo
 */
@Slf4j
@RestController
@RequestMapping("/document")
@RequiredArgsConstructor
public class DocumentController {

    private final FileService fileService;

    @PostMapping("/parse")
    public Result<Boolean> parseDocument(@RequestParam("file")MultipartFile file) {
        if (file.isEmpty()){
            return Result.error(DOCUMENT_EMPTY.getCode(), DOCUMENT_EMPTY.getMessage());
        }
        try {
            String text = fileService.extractText(file.getInputStream());
            // TODO: 将提取到的文本存入向量数据库
            return Result.success(true);
        } catch (Exception e) {
            log.error("parse document error", e);
            return Result.success(false);
        }
    }
}
