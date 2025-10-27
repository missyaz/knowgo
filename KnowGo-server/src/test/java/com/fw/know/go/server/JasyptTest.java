package com.fw.know.go.server;

import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.EnvironmentPBEConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.StandardBeanInfoFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @Classname JasyptTest
 * @Description TODO
 * @Date 27/10/2025 上午11:24
 * @Author Leo
 */
@SpringBootTest
public class JasyptTest {

    @Autowired
    private StringEncryptor encryptor;

    @Test
    public void testEncryptor() {
        String encryptedPassword = encryptor.encrypt("123456");
        System.out.println(encryptedPassword);
    }

    @Test
    public void encrypt(){
        String encryptedPassword = "sk-f65546009b6e44fda3fa164c659d53d7";
        String algorithm = "PBEWithMD5AndDES";
        // 盐值
        String password = "Leo";
        StandardPBEStringEncryptor standardPBEStringEncryptor = new StandardPBEStringEncryptor();
        EnvironmentPBEConfig environmentPBEConfig = new EnvironmentPBEConfig();
        environmentPBEConfig.setPassword(password);
        environmentPBEConfig.setAlgorithm(algorithm);
        standardPBEStringEncryptor.setConfig(environmentPBEConfig);
        System.out.println(standardPBEStringEncryptor.encrypt(encryptedPassword));
    }

    @Test
    public void decrypt(){
        String encryptedPassword = "ttTyxRzAgUYLKAtrUeZYRr24ddHiT8zRFnq+l4cvPdy1gmLWXyJhJukRa4Oe1gqY";
        String algorithm = "PBEWithMD5AndDES";
        // 盐值
        String password = "Leo";
        StandardPBEStringEncryptor standardPBEStringEncryptor = new StandardPBEStringEncryptor();
        EnvironmentPBEConfig environmentPBEConfig = new EnvironmentPBEConfig();
        environmentPBEConfig.setPassword(password);
        environmentPBEConfig.setAlgorithm(algorithm);
        standardPBEStringEncryptor.setConfig(environmentPBEConfig);
        System.out.println(standardPBEStringEncryptor.decrypt(encryptedPassword));
    }

}
