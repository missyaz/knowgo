package com.fw.know.go.sms;

import com.fw.know.go.sms.response.SmsSendResponse;

/**
 * @Description 短信服务
 * @Date 23/1/2026 下午1:51
 * @Author Leo
 */
public interface SmsService {

    /**
     * 发送短信
     * @param mobile 手机号
     * @param content 内容
     * @return 响应
     */
    SmsSendResponse sendMsg(String mobile,String content);
}
