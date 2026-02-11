package com.fw.know.go.vector.configuration;

import com.fw.know.go.vector.VectorDatasourceService;
import com.fw.know.go.vector.VectorDatasourceServiceImpl;
import org.springframework.ai.chroma.vectorstore.ChromaApi;
import org.springframework.ai.chroma.vectorstore.ChromaVectorStore;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

/**
 * @Description
 * @Date 10/2/2026 下午4:48
 * @Author Leo
 */
@Configuration
@EnableConfigurationProperties(VectorDatasourceProperties.class)
public class VectorConfiguration {

    /**
     * 向量数据库的配置属性
     */
    private final VectorDatasourceProperties vectorDatasourceProperties;

    public VectorConfiguration(VectorDatasourceProperties vectorDatasourceProperties) {
        this.vectorDatasourceProperties = vectorDatasourceProperties;
    }

    @Bean
    @ConditionalOnMissingBean
    public ChromaApi chromaApi() {
        return ChromaApi.builder()
                .baseUrl(Objects.requireNonNull(vectorDatasourceProperties.getBaseUrl()))
                .build();
    }

    @Bean
    @ConditionalOnMissingBean
    public ChromaVectorStore vectorStore(EmbeddingModel embeddingModel, ChromaApi chromaApi,
                                         VectorDatasourceProperties storeProperties) {
        return ChromaVectorStore.builder(Objects.requireNonNull(chromaApi), Objects.requireNonNull(embeddingModel))
                .tenantName(Objects.requireNonNull(storeProperties.getTenantName()))
                .databaseName(Objects.requireNonNull(storeProperties.getDatabaseName()))
                .collectionName(Objects.requireNonNull(storeProperties.getCollectionName()))
                .initializeSchema(storeProperties.getInitializeSchema() != null && storeProperties.getInitializeSchema())
                .build();
    }

    @Bean
    @ConditionalOnMissingBean
    public VectorDatasourceService vectorDatasourceService(VectorStore vectorStore) {
        return new VectorDatasourceServiceImpl(vectorStore);
    }
}
