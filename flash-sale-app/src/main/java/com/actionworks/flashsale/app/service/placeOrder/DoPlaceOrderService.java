package com.actionworks.flashsale.app.service.placeOrder;

import com.actionworks.flashsale.app.model.command.FlashPlaceOrderCommand;

public interface DoPlaceOrderService {
    /**
     * 下单功能的执行部分
     */
    boolean doPlaceOrder(Long userId, FlashPlaceOrderCommand command);
}
