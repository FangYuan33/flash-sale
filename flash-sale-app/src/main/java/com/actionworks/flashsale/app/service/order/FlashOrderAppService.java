package com.actionworks.flashsale.app.service.order;

import com.actionworks.flashsale.app.model.command.FlashPlaceOrderCommand;
import com.actionworks.flashsale.app.model.query.FlashOrderQuery;
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

    /**
     * 通过秒杀订单ID查询订单
     */
    <T> AppResult<T> getFlashOrder(Long orderId);

    /**
     * 条件查询秒杀订单
     */
    <T> AppResult<T> getFlashOrders(FlashOrderQuery flashOrderQuery);
}
