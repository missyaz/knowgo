package com.fw.know.go.notice.domain.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fw.know.go.notice.domain.constant.NoticeState;
import com.fw.know.go.notice.domain.constant.NoticeType;
import com.fw.know.go.notice.domain.entity.Notice;
import com.fw.know.go.notice.infrastructure.exception.NoticeException;
import com.fw.know.go.notice.infrastructure.mapper.NoticeMapper;
import org.springframework.stereotype.Service;

import static com.fw.know.go.notice.infrastructure.exception.NoticeErrorCode.NOTICE_SAVE_FAILED;

/**
 * @Description 通知服务
 * @Date 23/1/2026 下午1:18
 * @Author Leo
 */
@Service
public class NoticeService extends ServiceImpl<NoticeMapper, Notice> {

    private static final String SMS_NOTICE = "验证码";

    public Notice saveCaptcha(String telephone, String captcha) {
        Notice notice = Notice.builder()
                .noticeTitle(SMS_NOTICE)
                .noticeContent(captcha)
                .noticeType(NoticeType.SMS)
                .targetAddress(telephone)
                .state(NoticeState.INIT)
                .build();
        boolean saveResult = this.save(notice);
        if (!saveResult) {
            throw new NoticeException(NOTICE_SAVE_FAILED);
        }
        return notice;
    }
}
