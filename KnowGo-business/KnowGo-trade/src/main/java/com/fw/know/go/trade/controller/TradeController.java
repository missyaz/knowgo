package com.fw.know.go.trade.controller;

import com.fw.know.go.order.OrderException;
import com.fw.know.go.trade.exception.TradeErrorCode;
import com.fw.know.go.trade.exception.TradeException;
import com.fw.know.go.trade.param.BuyParam;
import com.fw.know.go.trade.service.TradeService;
import com.fw.know.go.web.vo.Result;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description
 * @Date 2/4/2026 上午10:25
 * @Author Leo
 */
@Slf4j
@RestController
@RequestMapping("trade")
@RequiredArgsConstructor
public class TradeController {

    private final TradeService tradeService;


    @PostMapping("/normalBuy")
    public Result<String> normalBuy(@Valid @RequestBody BuyParam buyParam){
        try {
            return Result.success(tradeService.normalBuy(buyParam));
        }
        catch (OrderException | TradeException e){
            return Result.error(e.getErrorCode().getCode(), e.getErrorCode().getMessage());
        }
        catch (Exception e){
            log.error(e.getMessage());
        }
        throw new TradeException(TradeErrorCode.ORDER_CREATE_FAILED);
    }
}
