package com.fw.know.go.sms.response;

import com.fw.know.go.base.response.BaseResponse;
import lombok.Builder;
import lombok.experimental.SuperBuilder;

/**
 * @Description
 * @Date 23/1/2026 下午1:53
 * @Author Leo
 */
public class SmsSendResponse extends BaseResponse {

    public static class Builder {

        private final SmsSendResponse smsSendResponse;

        public Builder(){
            smsSendResponse = new SmsSendResponse();
        }

        public Builder setCode(String code){
            smsSendResponse.setResponseCode(code);
            return this;
        }

        public Builder setMessage(String message){
            smsSendResponse.setResponseMessage(message);
            return this;
        }

        public Builder setSuccess(boolean success){
            smsSendResponse.setSuccess(success);
            return this;
        }

        public SmsSendResponse build(){
            return smsSendResponse;
        }
    }
}
