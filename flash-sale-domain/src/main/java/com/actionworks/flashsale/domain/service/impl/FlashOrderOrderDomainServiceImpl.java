package com.actionworks.flashsale.domain.service.impl;

import com.actionworks.flashsale.domain.adapter.CodeGenerateService;
import com.actionworks.flashsale.domain.model.aggregate.FlashItem;
import com.actionworks.flashsale.domain.model.aggregate.FlashOrder;
import com.actionworks.flashsale.domain.repository.FlashOrderRepository;
import com.actionworks.flashsale.domain.service.FlashItemDomainService;
import com.actionworks.flashsale.domain.service.FlashOrderDomainService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class FlashOrderOrderDomainServiceImpl implements FlashOrderDomainService {

    @Resource
    private FlashOrderRepository flashOrderRepository;
    @Resource
    private FlashItemDomainService flashItemDomainService;
    @Resource
    private CodeGenerateService codeGenerateService;

    @Override
    public void createOrder(FlashOrder flashOrder) {
        // 扣减库存
        FlashItem flashItem = flashItemDomainService.deductStock(flashOrder.getItemCode(), flashOrder.getQuantity());
        // 创建订单
        flashOrder.create(codeGenerateService, flashItem);
        flashOrderRepository.save(flashOrder);
    }
}
