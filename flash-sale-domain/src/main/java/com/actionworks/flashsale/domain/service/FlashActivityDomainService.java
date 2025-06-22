package com.actionworks.flashsale.domain.service;

import com.actionworks.flashsale.domain.model.aggregate.FlashActivity;
import com.actionworks.flashsale.domain.model.aggregate.FlashItem;

public interface FlashActivityDomainService {

    void publish(FlashActivity flashActivity, FlashItem flashItem);

    void changeActivityStatus(FlashActivity flashActivity, Integer status);
}
