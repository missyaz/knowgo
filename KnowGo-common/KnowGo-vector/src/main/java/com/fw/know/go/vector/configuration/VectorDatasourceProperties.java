package com.fw.know.go.vector.configuration;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Description 向量数据库的配置属性
 * @Date 28/10/2025 下午2:24
 * @Author  Leo
 */
@Data
@ConfigurationProperties(prefix = VectorDatasourceProperties.PREFIX)
public class VectorDatasourceProperties {
    /**
     * 向量数据库的默认名称
     */
    public static final String PREFIX = "vector-datasource";

    /**
     * 向量数据库的租户名称
     */
    private String tenantName;

    /**
     * 向量数据库的URL
     */
    private String baseUrl;

    /**
     * 向量数据库的访问令牌
     */
    private String keyToken;

    /**
     * 向量数据库的用户名
     */
    private String userName;

    /**
     * 向量数据库的密码
     */
    private String password;

     /**
     * 向量数据库的数据库名称
     */
    private String databaseName;

    /**
     * 向量数据库的集合名称
     */
    private String collectionName;

    /**
     * 是否初始化向量数据库的schema
     */
    private Boolean initializeSchema = true;
}
