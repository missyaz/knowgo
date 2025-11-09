package com.fw.know.go.datasource.configuration;

import com.fw.know.go.datasource.VectorDatasourceService;
import com.fw.know.go.datasource.VectorDatasourceServiceImpl;
import io.micrometer.observation.ObservationRegistry;
import org.springframework.ai.chroma.vectorstore.ChromaApi;
import org.springframework.ai.chroma.vectorstore.ChromaVectorStore;
import org.springframework.ai.embedding.BatchingStrategy;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.chroma.autoconfigure.ChromaVectorStoreProperties;
import org.springframework.ai.vectorstore.observation.VectorStoreObservationConvention;
import org.springframework.beans.factory.ObjectProvider;
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

//    @Bean
//    @ConditionalOnMissingBean
//    public ChromaApi chromaApi() {
//        return ChromaApi.builder()
//                .baseUrl(vectorDatasourceProperties.getHost() + ":" + vectorDatasourceProperties.getPort())
//                .build();
//    }

    @Bean
    public ChromaVectorStore vectorStore(EmbeddingModel embeddingModel, ChromaApi chromaApi,
                                         ChromaVectorStoreProperties storeProperties, ObjectProvider<ObservationRegistry> observationRegistry,
                                         ObjectProvider<VectorStoreObservationConvention> customObservationConvention,
                                         BatchingStrategy chromaBatchingStrategy) {
        return ChromaVectorStore.builder(chromaApi, embeddingModel)
                .tenantName(storeProperties.getTenantName())
                .databaseName(storeProperties.getDatabaseName())
                .collectionName(storeProperties.getCollectionName())
                .initializeSchema(storeProperties.isInitializeSchema())
                .observationRegistry(observationRegistry.getIfUnique(() -> ObservationRegistry.NOOP))
                .customObservationConvention(customObservationConvention.getIfAvailable(() -> null))
                .batchingStrategy(chromaBatchingStrategy)
                .build();
    }

    @Bean
    @ConditionalOnMissingBean
    public VectorDatasourceService vectorDatasourceService(VectorStore vectorStore) {
        return new VectorDatasourceServiceImpl(vectorStore);
    }
}
