package com.actionworks.flashsale.domain.service.impl;

import com.actionworks.flashsale.domain.adapter.CodeGenerateService;
import com.actionworks.flashsale.domain.model.aggregate.FlashItem;
import com.actionworks.flashsale.domain.model.enums.FlashItemStatus;
import com.actionworks.flashsale.domain.service.FlashItemDomainService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Slf4j
@Service
public class FlashItemDomainServiceImpl implements FlashItemDomainService {

    @Resource
    private CodeGenerateService codeGenerateService;

    @Override
    public void publish(FlashItem flashItem) {
        // 发布商品（领域模型只处理核心业务逻辑）
        flashItem.publish();
        
        // 在领域服务层处理外部依赖（编码生成）
        String itemCode = codeGenerateService.generateCode();
        flashItem.assignCode(itemCode);
        
        // 注意：数据访问操作已移除，由应用层负责
    }

    @Override
    public void changeItemStatus(FlashItem flashItem, Integer status) {
        // 变更状态（领域模型只处理核心业务逻辑）
        flashItem.changeStatus(FlashItemStatus.parseByCode(status));
        
        // 注意：数据访问操作已移除，由应用层负责
    }

    @Override
    public void deductStock(FlashItem flashItem, Integer quantity) {
        // 扣减库存（领域模型只处理核心业务逻辑）
        flashItem.deductStock(quantity);
        
        // 注意：库存持久化操作已移除，由应用层负责
    }
}
