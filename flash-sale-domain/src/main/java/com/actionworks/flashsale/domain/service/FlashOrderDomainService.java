package com.actionworks.flashsale.domain.service;

import com.actionworks.flashsale.domain.model.entity.FlashOrder;
import com.actionworks.flashsale.domain.model.query.FlashOrderQueryCondition;
import com.actionworks.flashsale.domain.model.query.PageResult;

public interface FlashOrderDomainService {
    /**
     * 下单！
     */
    void doPlaceOrder(FlashOrder flashOrder);

    /**
     * 取消！
     *
     * @param orderId 订单ID
     */
    void cancelOrder(Long orderId);

    /**
     * 条件查询秒杀订单
     */
    PageResult<FlashOrder> listByQueryCondition(FlashOrderQueryCondition queryCondition);
}
