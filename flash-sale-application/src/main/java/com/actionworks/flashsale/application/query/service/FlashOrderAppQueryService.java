package com.actionworks.flashsale.application.query.service;

import com.actionworks.flashsale.domain.model.aggregate.FlashOrder;

public interface FlashOrderAppQueryService {
    FlashOrder findByCode(String code);
}
