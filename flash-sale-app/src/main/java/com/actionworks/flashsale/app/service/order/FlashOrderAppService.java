package com.actionworks.flashsale.app.service.order;

import com.actionworks.flashsale.app.model.command.FlashPlaceOrderCommand;
import com.actionworks.flashsale.app.model.result.AppResult;

public interface FlashOrderAppService {

    /**
     * 秒杀下单
     *
     * @param userId 用户ID
     */
    <T> AppResult<T> placeOrder(Long userId, FlashPlaceOrderCommand command);

    /**
     * 取消订单
     *
     * @param orderId 订单ID
     */
    <T> AppResult<T> cancelOrder(Long orderId);
}
