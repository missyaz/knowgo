package com.fw.know.go.notice.facade;

import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson2.JSON;
import com.fw.know.go.api.notice.response.NoticeResponse;
import com.fw.know.go.api.notice.service.NoticeFacadeService;
import com.fw.know.go.notice.domain.constant.NoticeState;
import com.fw.know.go.notice.domain.entity.Notice;
import com.fw.know.go.notice.domain.service.NoticeService;
import com.fw.know.go.sms.SmsService;
import com.fw.know.go.sms.response.SmsSendResponse;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import static com.fw.know.go.api.notice.constant.NoticeConstant.CAPTCHA_KEY_PREFIX;

/**
 * @Description
 * @Date 22/1/2026 下午5:44
 * @Author Leo
 */
@DubboService(version = "1.0.0")
public class NoticeFacadeServiceImpl implements NoticeFacadeService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private NoticeService noticeService;

    @Autowired
    private SmsService smsService;

    @Override
    public NoticeResponse generateAndSendSmsCaptcha(String telephone) {

        // 生成验证码
        String captcha = RandomUtil.randomNumbers(4);

        // 验证码存入Redis
        redisTemplate.opsForValue().set(CAPTCHA_KEY_PREFIX + telephone, captcha, 5, TimeUnit.MINUTES);

        // 存入数据库
        Notice notice = noticeService.saveCaptcha(telephone, captcha);

        // 使用外部发送验证码短信
        Thread.ofVirtual().start(() -> {
            // 调用外部短信服务发送验证码短信
            SmsSendResponse result = smsService.sendMsg(notice.getTargetAddress(), notice.getNoticeContent());
            if (result.getSuccess()){
                notice.setState(NoticeState.SUCCESS);
                notice.setSendSuccessTime(new Date());
            }
            else {
                notice.setState(NoticeState.FAILED);
                notice.addExtendInfo("executeResult", JSON.toJSONString(result));
            }
            noticeService.updateById(notice);
        });
        return new NoticeResponse.Builder().setSuccess(true).build();
    }
}
