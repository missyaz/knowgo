package com.fw.know.go.document.domain.service;

import java.util.Map;

import cn.hutool.core.util.IdUtil;
import com.fw.know.go.datasource.VectorDatasourceService;
import com.fw.know.go.document.infrastructure.exception.DocumentErrorCode;
import com.fw.know.go.document.infrastructure.exception.DocumentException;
import com.fw.know.go.file.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Description 文档服务
 * @Date 29/10/2025 下午1:16
 * @Author Leo
 */
@Slf4j
@Service
public class DocumentService{

    private final VectorDatasourceService vectorDatasourceService;

    private final FileService fileService;

    public DocumentService(VectorDatasourceService vectorDatasourceService, FileService fileService) {
        this.vectorDatasourceService = vectorDatasourceService;
        this.fileService = fileService;
    }

    /**
     * 上传文档
     * @param file 文档文件
     * @return 是否上传成功
     */
    public Boolean uploadDocument(MultipartFile file) {
        try {
            // 从文件中提取内容文本
            String text = fileService.extractText(file.getInputStream());
            // 从文本中提取元数据
            Map<String, Object> metadata = fileService.extractMetadata(file.getInputStream());
            // 向向量数据库添加文档
            vectorDatasourceService.addDocument(IdUtil.simpleUUID(), text, metadata);
            return true;
        } catch (Exception e) {
            log.error("upload document error", e);
            throw new DocumentException(DocumentErrorCode.PARSE_ERROR);
        }
    }
}
