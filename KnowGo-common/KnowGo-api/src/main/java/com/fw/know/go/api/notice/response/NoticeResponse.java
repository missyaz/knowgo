package com.fw.know.go.api.notice.response;

import com.fw.know.go.base.response.BaseResponse;
import lombok.experimental.SuperBuilder;

/**
 * @Description
 * @Date 22/1/2026 上午11:05
 * @Author Leo
 */
public class NoticeResponse extends BaseResponse {

    public static class Builder {

        private final NoticeResponse noticeResponse;

        public Builder(){
            noticeResponse = new NoticeResponse();
        }

        public Builder setCode(String code){
            noticeResponse.setResponseCode(code);
            return this;
        }

        public Builder setMessage(String message){
            noticeResponse.setResponseMessage(message);
            return this;
        }

        public Builder setSuccess(boolean success){
            noticeResponse.setSuccess(success);
            return this;
        }

        public NoticeResponse build(){
            return noticeResponse;
        }
    }
}
