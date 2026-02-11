package com.fw.know.go.vector;

import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.filter.Filter;

import java.util.List;
import java.util.Map;

/**
 * @Description 
 * @Date 5/11/2025 下午4:16
 * @Author  Leo
 */
public class VectorDatasourceServiceImpl implements VectorDatasourceService {

    private final VectorStore vectorStore;

    public VectorDatasourceServiceImpl(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    @Override
    public void addDocument(String id, String content, Map<String, Object> metadata) {
        vectorStore.add(List.of(new Document(id, content, metadata)));
    }

    @Override
    public void addDocuments(List<Document> documents) {
        vectorStore.add(documents);
    }

    @Override
    public void deleteDocument(String id) {
        vectorStore.delete(List.of(id));
    }

    @Override
    public void deleteDocuments(List<String> ids) {
        vectorStore.delete(ids);
    }

    @Override
    public List<Document> similaritySearch(String query, int topK, double similarityThreshold) {
        return vectorStore.similaritySearch(SearchRequest.builder().query(query).topK(topK).similarityThreshold(similarityThreshold).build());
    }

    @Override
    public List<Document> similaritySearch(String query, int topK) {
        return vectorStore.similaritySearch(SearchRequest.builder().query(query).topK(topK).build());
    }

    @Override
    public List<Document> similaritySearch(String query, int topK, Map<String, Object> filter) {
        SearchRequest searchRequest = SearchRequest.builder()
                .query(query)
                .topK(topK)
                .filterExpression(new Filter.Expression(Filter.ExpressionType.EQ, new Filter.Key("country"), new Filter.Value("UK")))
                .build();
        return vectorStore.similaritySearch(searchRequest);
    }

    @Override
    public void clear() {
        vectorStore.delete(List.of("*"));
    }
}