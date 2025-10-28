package com.fw.know.go.file.configuration;

import com.fw.know.go.file.FileService;
import com.fw.know.go.file.FileServiceImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Classname FileConfiguration
 * @Description TODO
 * @Date 28/10/2025 下午4:59
 * @Author Leo
 */
@Configuration
public class FileConfiguration {

    @Bean
    @ConditionalOnBean(FileService.class)
    public FileService fileService(){
        return new FileServiceImpl();
    }
}
