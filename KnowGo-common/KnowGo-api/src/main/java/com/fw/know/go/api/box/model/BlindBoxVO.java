package com.fw.know.go.api.box.model;

import com.fw.know.go.api.box.constant.BlindBoxStateEnum;
import com.fw.know.go.api.goods.constant.GoodsState;
import com.fw.know.go.api.goods.model.BaseGoodsVO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

/**
 * @Description
 * @Date 30/3/2026 上午9:55
 * @Author Leo
 */
@Getter
@Setter
@ToString
public class BlindBoxVO extends BaseGoodsVO {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 盲盒名称
     */
    private String name;

    /**
     * 盲盒封面
     */
    private String cover;

    /**
     * 盲盒详情
     */
    private String detail;

    /**
     * 价格
     */
    private BigDecimal price;

    /**
     * 库存
     */
    private Long inventory;

    /**
     * 藏品数量
     */
    private Long quantity;

    /**
     * 预约开始时间
     */
    private Date bookStartTime;

    /**
     * 预约结束时间
     */
    private Date bookEndTime;

    /**
     * 是否预约
     */
    private Integer canBook;

    /**
     * 是否已预约过
     */
    private Boolean hasBooked;

    public static final int DEFAULT_MIN_SALE_TIME = 60 * 24;

    public static GoodsState getState(BlindBoxStateEnum state, Date saleTime, Long saleableInventory){
        if (state.equals(BlindBoxStateEnum.INIT) || state.equals(BlindBoxStateEnum.REMOVED)){
            return GoodsState.NOT_FOR_SALE;
        }

        Instant now = Instant.now();
        if (now.compareTo(saleTime.toInstant()) >= 0){
            if (saleableInventory > 0){
                return GoodsState.SELLING;
            } else {
                return GoodsState.SOLD_OUT;
            }
        }
        else {
           if (ChronoUnit.MINUTES.between(now, saleTime.toInstant()) > DEFAULT_MIN_SALE_TIME){
               return GoodsState.WAIT_FOR_SALE;
           }
           else {
               return GoodsState.COMING_SOON;
           }
        }
    }

    public void setState(BlindBoxStateEnum state, Date saleTime, Long saleableInventory){
        super.setState(getState(state, saleTime, saleableInventory));
    }

    @Override
    public String getGoodsName() {
        return name;
    }

    @Override
    public String getGoodsPicUrl() {
        return cover;
    }

    @Override
    public String getSellerId() {
        // 藏品持有人默认是平台,平台ID用O表示
        return "0";
    }

    @Override
    public Integer getVersion() {
        return 0;
    }

    @Override
    public BigDecimal getPrice() {
        return price;
    }

    @Override
    public Boolean canBook() {
        if (canBook == null){
            return false;
        }

        return canBook == 1;
    }

    @Override
    public Boolean canBookNow() {
        // 当前时间是否在 bookStartTime 和 bookEndTime 之间
        if (canBook()){
            Instant now = Instant.now();
            return now.compareTo(bookStartTime.toInstant()) >= 0 && now.compareTo(bookEndTime.toInstant()) <= 0;
        }
        return false;
    }

    @Override
    public Boolean hasBooked() {
        return hasBooked;
    }
}
