package com.fw.know.go.document.controller;

import com.fw.know.go.document.domain.service.DocumentService;
import com.fw.know.go.file.FileService;
import com.fw.know.go.web.vo.Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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

    private final DocumentService documentService;

    @PostMapping("/upload")
    public Result<Boolean> uploadDocument(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()){
            return Result.error(DOCUMENT_EMPTY.getCode(), DOCUMENT_EMPTY.getMessage());
        }
        Boolean result = documentService.uploadDocument(file);
        return Result.success(result);
    }

    @GetMapping("/query")
    public Result<List<Document>> queryDocument(@RequestParam("query") String query,
                                                @RequestParam(value = "topK", required = false, defaultValue = "5") int topK) {
        List<Document> result = documentService.queryDocument(query, topK);
        return Result.success(result);
    }
}
