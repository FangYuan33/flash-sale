package com.actionworks.flashsale.app.service.order;

import com.actionworks.flashsale.app.exception.BizException;
import com.actionworks.flashsale.app.model.command.FlashPlaceOrderCommand;
import com.actionworks.flashsale.app.model.result.AppResult;
import com.actionworks.flashsale.app.service.placeOrder.PlaceOrderService;
import com.actionworks.flashsale.domain.service.FlashOrderDomainService;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import static com.actionworks.flashsale.app.exception.AppErrorCode.INVALID_PARAMS;

@Slf4j
@Service
public class DefaultFlashOrderAppService implements FlashOrderAppService {

    @Resource
    private FlashOrderDomainService flashOrderDomainService;
    @Resource
    private PlaceOrderService placeOrderService;

    @Override
    public <T> AppResult<T> placeOrder(Long userId, FlashPlaceOrderCommand command) {
        log.info("placeOrder|下单|{}, {}", userId, JSON.toJSONString(command));

        if (userId == null || command == null) {
            throw new BizException(INVALID_PARAMS);
        }

        return placeOrderService.doPlaceOrder(userId, command);
    }
}
