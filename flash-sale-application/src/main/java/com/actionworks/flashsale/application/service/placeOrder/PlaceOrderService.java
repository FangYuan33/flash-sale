package com.actionworks.flashsale.application.service.placeOrder;

import com.actionworks.flashsale.application.model.command.FlashPlaceOrderCommand;
import com.actionworks.flashsale.application.model.result.AppResult;

public interface PlaceOrderService {
    /**
     * 实际下单执行的动作
     *
     * @param userId 用户ID
     */
    <T> AppResult<T> doPlaceOrder(Long userId, FlashPlaceOrderCommand command);
}
