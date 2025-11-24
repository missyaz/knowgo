package com.fw.know.go.datasource;

import org.springframework.ai.document.Document;

import java.util.List;
import java.util.Map;

/**
 * @Description 向量数据源服务接口
 * @Date 5/11/2025 下午2:26
 * @Author Leo
 */
public interface VectorDatasourceService {
    
    /**
     * 添加单个文档到向量数据库
     * @param id 文档ID
     * @param content 文档内容
     * @param metadata 文档元数据
     */
    void addDocument(String id, String content, Map<String, Object> metadata);
    
    /**
     * 批量添加文档到向量数据库
     * @param documents 文档列表
     */
    void addDocuments(List<Document> documents);
    
    /**
     * 按ID删除文档
     * @param id 文档ID
     */
    void deleteDocument(String id);
    
    /**
     * 按ID批量删除文档
     * @param ids 文档ID列表
     */
    void deleteDocuments(List<String> ids);
    
    /**
     * 相似性搜索
     * @param query 查询文本
     * @param topK 返回的最大结果数
     * @param similarityThreshold 相似度阈值
     * @return 相似文档列表
     */
    List<Document> similaritySearch(String query, int topK, double similarityThreshold);
    
    /**
     * 带过滤条件的相似性搜索
     * @param query 查询文本
     * @param topK 返回的最大结果数
     * @param filter 过滤条件
     * @return 相似文档列表
     */
    List<Document> similaritySearch(String query, int topK, Map<String, Object> filter);
    
    /**
     * 清除集合中的所有文档
     */
    void clear();
}