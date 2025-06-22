package com.actionworks.flashsale.domain.service;

import com.actionworks.flashsale.domain.model.aggregate.FlashItem;
import com.actionworks.flashsale.domain.model.aggregate.FlashOrder;
import com.actionworks.flashsale.domain.model.event.OrderCreateEvent;

public interface FlashOrderDomainService {

    OrderCreateEvent createOrder(FlashOrder flashOrder, FlashItem flashItem);
}
