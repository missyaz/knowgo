package com.fw.know.go.sms;

import com.fw.know.go.sms.response.SmsSendResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * @Description 短信服务-Mock实现
 * @Date 23/1/2026 下午1:56
 * @Author Leo
 */
@Slf4j
public class MockSmsServiceImpl implements SmsService{

    @Override
    public SmsSendResponse sendMsg(String mobile, String content) {
        return new SmsSendResponse.Builder().setSuccess(true).build();
    }
}
