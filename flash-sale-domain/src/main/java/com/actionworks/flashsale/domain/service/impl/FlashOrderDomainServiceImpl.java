package com.actionworks.flashsale.domain.service.impl;

import com.actionworks.flashsale.domain.repository.FlashOrderRepository;
import com.actionworks.flashsale.domain.service.FlashOrderDomainService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class FlashOrderDomainServiceImpl implements FlashOrderDomainService {

    @Resource
    private FlashOrderRepository flashOrderRepository;
}
