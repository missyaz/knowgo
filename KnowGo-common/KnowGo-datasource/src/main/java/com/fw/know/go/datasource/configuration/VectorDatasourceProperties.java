package com.fw.know.go.datasource.configuration;
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
     * 向量数据库的名称
     */
    private String name;

    /**
     * 向量数据库的主机地址
     */
    private String host;

    /**
     * 向量数据库的端口
     */
    private String port;

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
     * 向量数据库的集合名称
     */
    private String collectionName;

    /**
     * 是否初始化向量数据库的schema
     */
    private Boolean initializeSchema = true;
}
