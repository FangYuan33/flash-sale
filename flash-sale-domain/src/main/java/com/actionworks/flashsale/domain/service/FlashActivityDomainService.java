package com.actionworks.flashsale.domain.service;

import com.actionworks.flashsale.domain.model.aggregate.FlashActivity;

public interface FlashActivityDomainService {

    void publish(FlashActivity flashActivity);

    void changeActivityStatus(String code, Integer status);

    FlashActivity getById(Long activityId);

}
