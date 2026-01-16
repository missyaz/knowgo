package com.fw.know.go.datasource.configuration;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.BlockAttackInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.fw.know.go.datasource.VectorDatasourceService;
import com.fw.know.go.datasource.VectorDatasourceServiceImpl;
import com.fw.know.go.datasource.handler.DataObjectHandler;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.ai.chroma.vectorstore.ChromaApi;
import org.springframework.ai.chroma.vectorstore.ChromaVectorStore;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

/**
 * @Description 数据源配置类
 * @Date 28/10/2025 下午2:24
 * @Author  Leo
 */
@Configuration
@MapperScan(basePackages = "cn.hollis.nft.turbo.*.infrastructure.mapper")
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

    @Bean
    public DataObjectHandler myMetaObjectHandler() {
        return new DataObjectHandler();
    }

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor(){
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        // 乐观锁插件
        interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
        // 防全表更新与删除插件
        interceptor.addInnerInterceptor(new BlockAttackInnerInterceptor());
        // 分页插件
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.POSTGRE_SQL));
        return interceptor;
    }
}
