package com.fw.know.go.datasource.configuration;

import com.fw.know.go.datasource.VectorDatasourceService;
import com.fw.know.go.datasource.VectorDatasourceServiceImpl;
import org.springframework.ai.chroma.vectorstore.ChromaApi;
import org.springframework.ai.chroma.vectorstore.ChromaVectorStore;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description 数据源配置类
 * @Date 28/10/2025 下午2:24
 * @Author  Leo
 */
@Configuration
@EnableConfigurationProperties(VectorDatasourceProperties.class)
public class DatasourceConfiguration {

    /**
     * 向量数据库的配置属性
     */
    private final VectorDatasourceProperties vectorDatasourceProperties;


    public DatasourceConfiguration(VectorDatasourceProperties vectorDatasourceProperties) {
        this.vectorDatasourceProperties = vectorDatasourceProperties;
    }

    @Bean
    @ConditionalOnMissingBean
    public ChromaApi chromaApi() {
        return ChromaApi.builder()
                .baseUrl(vectorDatasourceProperties.getHost() + ":" + vectorDatasourceProperties.getPort())
                .build();
    }

    @Bean
    @ConditionalOnMissingBean
    public VectorStore vectorStore(EmbeddingModel embeddingModel, ChromaApi chromaApi) {
        return ChromaVectorStore.builder(chromaApi, embeddingModel)
                .collectionName(vectorDatasourceProperties.getCollectionName())
                .initializeImmediately(vectorDatasourceProperties.getInitializeSchema())
                .build();
    }

    @Bean
    @ConditionalOnMissingBean
    public VectorDatasourceService vectorDatasourceService(VectorStore vectorStore) {
        return new VectorDatasourceServiceImpl(vectorStore);
    }
}
