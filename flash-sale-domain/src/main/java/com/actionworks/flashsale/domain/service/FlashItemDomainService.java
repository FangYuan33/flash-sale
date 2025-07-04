package com.actionworks.flashsale.domain.service;

import com.actionworks.flashsale.domain.model.aggregate.FlashItem;

public interface FlashItemDomainService {

    void publish(FlashItem flashItem);

    void changeItemStatus(FlashItem flashItem, Integer status);

    void deductStock(FlashItem flashItem, Integer quantity);
}
