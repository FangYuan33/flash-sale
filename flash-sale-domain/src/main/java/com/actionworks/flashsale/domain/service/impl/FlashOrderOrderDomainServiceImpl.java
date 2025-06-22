package com.actionworks.flashsale.domain.service.impl;

import com.actionworks.flashsale.domain.adapter.CodeGenerateService;
import com.actionworks.flashsale.domain.model.aggregate.FlashItem;
import com.actionworks.flashsale.domain.model.aggregate.FlashOrder;
import com.actionworks.flashsale.domain.repository.FlashItemRepository;
import com.actionworks.flashsale.domain.repository.FlashOrderRepository;
import com.actionworks.flashsale.domain.service.FlashOrderDomainService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class FlashOrderOrderDomainServiceImpl implements FlashOrderDomainService {

    @Resource
    private FlashOrderRepository flashOrderRepository;
    @Resource
    private FlashItemRepository flashItemRepository;
    @Resource
    private CodeGenerateService codeGenerateService;

    @Override
    public void createOrder(FlashOrder flashOrder) {
        // 获取商品信息（用于订单创建）
        FlashItem flashItem = flashItemRepository.findByCode(flashOrder.getItemCode());
        
        // 创建订单（领域模型只处理核心业务逻辑）
        flashOrder.create(flashItem);
        
        // 在领域服务层处理外部依赖（编码生成）
        String orderCode = codeGenerateService.generateCode();
        flashOrder.assignCode(orderCode);
        
        // 保存订单
        flashOrderRepository.save(flashOrder);
    }
}
