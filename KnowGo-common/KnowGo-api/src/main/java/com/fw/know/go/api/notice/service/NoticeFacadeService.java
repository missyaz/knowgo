package com.fw.know.go.api.notice.service;

import com.fw.know.go.api.notice.response.NoticeResponse;

/**
 * @Description 消息 RPC 服务
 * @Date 22/1/2026 上午10:59
 * @Author Leo
 */
public interface NoticeFacadeService {

    /**
     * 生成并发送短信验证码
     * @param telephone 手机号
     * @return response
     */
    NoticeResponse generateAndSendSmsCaptcha(String telephone);
}
