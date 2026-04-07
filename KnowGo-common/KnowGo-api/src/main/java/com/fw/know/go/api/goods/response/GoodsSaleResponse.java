package com.fw.know.go.api.goods.response;

import com.fw.know.go.base.response.BaseResponse;
import lombok.Getter;
import lombok.Setter;

import static com.fw.know.go.base.exception.BizErrorCode.DUPLICATED;

/**
 * @Description 商品销售响应
 * @Date 7/4/2026 下午2:51
 * @Author Leo
 */
@Getter
@Setter
public class GoodsSaleResponse extends BaseResponse {

    /**
     * 持有藏品Id
     */
    private Long heldCollectionId;

    public static class GoodsResponseBuilder {
        private Long heldCollectionId;

        public GoodsSaleResponse.GoodsResponseBuilder heldCollectionId(Long heldCollectionId) {
            this.heldCollectionId = heldCollectionId;
            return this;
        }

        public GoodsSaleResponse buildSuccess(){
            GoodsSaleResponse goodsSaleResponse = new GoodsSaleResponse();
            goodsSaleResponse.setHeldCollectionId(heldCollectionId);
            goodsSaleResponse.setSuccess(true);
            return goodsSaleResponse;
        }

        public GoodsSaleResponse buildDuplicated(){
            GoodsSaleResponse goodsSaleResponse = new GoodsSaleResponse();
            goodsSaleResponse.setHeldCollectionId(heldCollectionId);
            goodsSaleResponse.setSuccess(true);
            goodsSaleResponse.setResponseCode(DUPLICATED.getCode());
            goodsSaleResponse.setResponseMessage(DUPLICATED.getMessage());
            return goodsSaleResponse;
        }

        public GoodsSaleResponse buildFail(String code, String msg){
            GoodsSaleResponse goodsSaleResponse = new GoodsSaleResponse();
            goodsSaleResponse.setHeldCollectionId(heldCollectionId);
            goodsSaleResponse.setSuccess(false);
            goodsSaleResponse.setResponseCode(code);
            goodsSaleResponse.setResponseMessage(msg);
            return goodsSaleResponse;
        }
    }
}
