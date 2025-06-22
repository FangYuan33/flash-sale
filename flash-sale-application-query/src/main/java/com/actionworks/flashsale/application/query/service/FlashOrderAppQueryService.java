package com.actionworks.flashsale.application.query.service;

import com.actionworks.flashsale.application.query.model.dto.FlashOrderDTO;

public interface FlashOrderAppQueryService {
    FlashOrderDTO findByCode(String code);
}
