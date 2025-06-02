package com.actionworks.flashsale.application.query.service.impl;

import com.actionworks.flashsale.application.query.service.FlashOrderAppQueryService;
import com.actionworks.flashsale.domain.model.aggregate.FlashOrder;
import com.actionworks.flashsale.domain.repository.FlashOrderRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Optional;

@Service
public class FlashOrderAppQueryServiceImpl implements FlashOrderAppQueryService {

    @Resource
    private FlashOrderRepository flashOrderRepository;

    @Override
    public FlashOrder findByCode(String code) {
        Optional<FlashOrder> flashOrderOptional = flashOrderRepository.findByCode(code);
        return flashOrderOptional.orElse(null);
    }
}
