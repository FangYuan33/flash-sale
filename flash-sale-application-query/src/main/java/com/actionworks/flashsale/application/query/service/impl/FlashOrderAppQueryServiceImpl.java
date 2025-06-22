package com.actionworks.flashsale.application.query.service.impl;

import com.actionworks.flashsale.application.query.assembler.FlashOrderAppAssembler;
import com.actionworks.flashsale.application.query.model.dto.FlashOrderDTO;
import com.actionworks.flashsale.application.query.service.FlashOrderAppQueryService;
import com.actionworks.flashsale.infrastructure.persistence.mapper.FlashOrderMapper;
import com.actionworks.flashsale.infrastructure.persistence.model.FlashOrderPO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class FlashOrderAppQueryServiceImpl implements FlashOrderAppQueryService {

    @Resource
    private FlashOrderMapper flashOrderRepository;

    @Override
    public FlashOrderDTO findByCode(String code) {
        LambdaQueryWrapper<FlashOrderPO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(FlashOrderPO::getCode, code);
        FlashOrderPO flashOrderPO = flashOrderRepository.selectOne(queryWrapper);
        return FlashOrderAppAssembler.toDTO(flashOrderPO);
    }
}
