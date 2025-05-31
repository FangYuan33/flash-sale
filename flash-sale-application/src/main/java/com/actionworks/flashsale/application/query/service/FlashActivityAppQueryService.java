package com.actionworks.flashsale.application.query.service;

import com.actionworks.flashsale.application.query.model.dto.FlashActivityDTO;

public interface FlashActivityAppQueryService {

    FlashActivityDTO findByCode(String code);
}
