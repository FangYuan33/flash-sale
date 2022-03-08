package com.actionworks.flashsale.app.service.placeOrder;

import com.actionworks.flashsale.app.model.command.FlashPlaceOrderCommand;
import com.actionworks.flashsale.app.model.result.AppResult;

public interface PlaceOrderService {
    /**
     * 实际下单执行的动作
     *
     * @param userId 用户ID
     */
    <T> AppResult<T> doPlaceOrder(Long userId, FlashPlaceOrderCommand command);
}
