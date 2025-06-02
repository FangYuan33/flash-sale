package com.actionworks.flashsale.domain.service;

import com.actionworks.flashsale.domain.model.aggregate.FlashOrder;

public interface FlashOrderDomainService {

    void createOrder(FlashOrder flashOrder);
}
