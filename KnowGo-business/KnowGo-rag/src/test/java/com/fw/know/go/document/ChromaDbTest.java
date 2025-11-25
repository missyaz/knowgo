package com.fw.know.go.document;

import com.fw.know.go.datasource.VectorDatasourceService;
import com.fw.know.go.document.domain.service.DocumentService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.ai.document.Document;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * ChromaDb测试类，测试文档的添加和查询功能
 */
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ChromaDbTest {

    @Autowired
    private VectorDatasourceService vectorDatasourceService;

    @Autowired
    private DocumentService documentService;

    private static final String TEST_DOCUMENT_ID = "test-document-1";
    private static final String TEST_CONTENT = "这是一个测试文档内容，包含了关于Spring Boot和向量数据库的信息。";
    private static final String TEST_QUERY = "Spring Boot";

    @BeforeEach
    void setUp() {
        // 测试前清空数据库，避免测试数据影响
        //vectorDatasourceService.clear();
    }

    @Test
    @Order(1)
    void testAddDocument() {
        // 准备测试数据
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("source", "test");
        metadata.put("category", "technical");
        
        // 执行添加文档操作
        List <Document> documents = List.of(
                new Document("Spring AI rocks!! Spring AI rocks!! Spring AI rocks!! Spring AI rocks!! Spring AI rocks!!", Map.of("meta1", "meta1")),
                new Document("The World is Big and Salvation Lurks Around the Corner"),
                new Document("You walk forward facing the past and you turn back toward the future.", Map.of("meta2", "meta2")));
        vectorDatasourceService.addDocuments(documents);
        
        // 验证文档是否添加成功
        List<Document> documentResults = vectorDatasourceService.similaritySearch("武汉", 5, 0.75);
        assertNotNull(documentResults, "查询结果不应为空");
        assertFalse(documentResults.isEmpty(), "至少应返回一个文档");
        
        // 检查文档内容和元数据
        Document foundDoc = documents.getFirst();
        assertEquals(TEST_DOCUMENT_ID, foundDoc.getId(), "文档ID应匹配");
        assertTrue(foundDoc.getFormattedContent().contains("Spring Boot"), "文档内容应包含关键词");
        assertEquals("test", foundDoc.getMetadata().get("source"), "元数据source应匹配");
        assertEquals("technical", foundDoc.getMetadata().get("category"), "元数据category应匹配");
        
        System.out.println("测试添加文档成功！");
    }

    @Test
    @Order(2)
    void testSimilaritySearch() {
        // 先添加多个文档用于测试
        Map<String, Object> metadata1 = new HashMap<>();
        metadata1.put("source", "spring_docs");
        vectorDatasourceService.addDocument("doc1", "Spring Boot是一个流行的Java框架，用于快速开发基于Spring的应用程序。", metadata1);
        
        Map<String, Object> metadata2 = new HashMap<>();
        metadata2.put("source", "vector_db_docs");
        vectorDatasourceService.addDocument("doc2", "向量数据库是一种专门用于存储和查询向量数据的数据库系统。", metadata2);
        
        Map<String, Object> metadata3 = new HashMap<>();
        metadata3.put("source", "general");
        vectorDatasourceService.addDocument("doc3", "这是一个不相关的文档，讨论的是天气和旅游。", metadata3);
        
        // 执行相似性查询
        List<Document> results = vectorDatasourceService.similaritySearch("Spring Boot框架", 5, 0.75);
        
        // 验证查询结果
        assertNotNull(results, "查询结果不应为空");
        assertFalse(results.isEmpty(), "应返回至少一个结果");
        
        // 检查相关性排序 - 第一个结果应该是最相关的（关于Spring Boot的文档）
        String firstDocContent = results.getFirst().getFormattedContent();
        assertTrue(firstDocContent.contains("Spring Boot"), "最相关的文档应包含查询关键词");
        
        // 测试带过滤条件的查询
        Map<String, Object> filter = new HashMap<>();
        filter.put("source", "spring_docs");
        List<Document> filteredResults = vectorDatasourceService.similaritySearch("框架", 5, filter);
        
        // 验证过滤结果
        assertNotNull(filteredResults, "过滤查询结果不应为空");
        for (Document doc : filteredResults) {
            assertEquals("spring_docs", doc.getMetadata().get("source"), "所有结果应匹配过滤条件");
        }
        
        System.out.println("测试相似性查询成功！找到 " + results.size() + " 个相关文档");
    }

    @Test
    @Order(3)
    void testDocumentService() {
        // 测试DocumentService的上传和查询功能
        // 由于我们没有实际的MultipartFile对象，这里直接测试底层的vectorDatasourceService
        
        // 1. 先添加测试文档
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("test_service", "true");
        //vectorDatasourceService.addDocument("service-test-doc", "这是通过服务层添加的测试文档，包含重要信息。", metadata);
        
        // 2. 使用服务层的查询方法
        List<Document> results = documentService.queryDocument("武汉", 5, 0.5d);
        
        // 3. 验证查询结果
        assertNotNull(results, "服务层查询结果不应为空");
        assertFalse(results.isEmpty(), "服务层查询应返回结果");
        assertTrue(results.getFirst().getFormattedContent().contains("重要信息"), "查询结果应包含关键词");
        
        System.out.println("测试DocumentService成功！");
    }

    @AfterAll
    static void tearDown(@Autowired VectorDatasourceService vectorDatasourceService) {
        // 测试完成后清理数据
        //vectorDatasourceService.clear();
        System.out.println("测试完成，已清理测试数据");
    }
}