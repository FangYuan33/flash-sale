package com.actionworks.flashsale.domain.service;

import com.actionworks.flashsale.domain.model.entity.FlashOrder;

public interface FlashOrderDomainService {
    /**
     * 下单！
     */
    void doPlaceOrder(FlashOrder flashOrder);
}
