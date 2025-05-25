package com.actionworks.flashsale.domain.service;

import com.actionworks.flashsale.domain.model.item.aggregate.FlashItem;

public interface FlashItemDomainService {

    void publish(FlashItem flashItem);

}
