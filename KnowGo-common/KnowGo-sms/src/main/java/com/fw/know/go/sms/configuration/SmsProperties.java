package com.fw.know.go.sms.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Description 短信服务配置类
 * @Date 23/1/2026 下午2:02
 * @Author Leo
 */
@Data
@ConfigurationProperties(prefix = SmsProperties.PREFIX)
public class SmsProperties {

    public static final String PREFIX = "know.go.sms";

    private String host;

    private String path;

    private String appCode;

    private String smsSignId;

    private String smsTemplateId;
}
