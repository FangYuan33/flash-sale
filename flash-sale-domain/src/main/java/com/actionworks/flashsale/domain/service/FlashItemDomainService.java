package com.actionworks.flashsale.domain.service;

import com.actionworks.flashsale.domain.model.aggregate.FlashItem;

public interface FlashItemDomainService {

    void publish(FlashItem flashItem);

    void changeItemStatus(String code, Integer status);

    FlashItem deductStock(String itemCode, Integer quantity);
}
