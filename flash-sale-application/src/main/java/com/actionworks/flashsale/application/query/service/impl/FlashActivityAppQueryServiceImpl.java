package com.actionworks.flashsale.application.query.service.impl;

import com.actionworks.flashsale.application.assembler.FlashActivityAppAssembler;
import com.actionworks.flashsale.application.query.model.dto.FlashActivityDTO;
import com.actionworks.flashsale.application.query.service.FlashActivityAppQueryService;
import com.actionworks.flashsale.domain.model.aggregate.FlashActivity;
import com.actionworks.flashsale.domain.repository.FlashActivityRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Optional;

@Service
public class FlashActivityAppQueryServiceImpl implements FlashActivityAppQueryService {

    @Resource
    private FlashActivityRepository flashActivityRepository;

    @Override
    public FlashActivityDTO findByCode(String code) {
        Optional<FlashActivity> flashActivityOptional = flashActivityRepository.findByCode(code);

        return flashActivityOptional.map(FlashActivityAppAssembler::toDTO).orElse(null);
    }
}
