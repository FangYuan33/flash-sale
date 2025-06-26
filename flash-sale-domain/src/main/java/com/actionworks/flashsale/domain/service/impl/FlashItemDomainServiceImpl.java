package com.actionworks.flashsale.domain.service.impl;

import com.actionworks.flashsale.common.exception.DomainException;
import com.actionworks.flashsale.domain.adapter.CodeGenerateService;
import com.actionworks.flashsale.domain.model.aggregate.FlashItem;
import com.actionworks.flashsale.domain.model.enums.FlashItemStatus;
import com.actionworks.flashsale.domain.repository.FlashItemRepository;
import com.actionworks.flashsale.domain.service.FlashItemDomainService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Slf4j
@Service
public class FlashItemDomainServiceImpl implements FlashItemDomainService {

    @Resource
    private CodeGenerateService codeGenerateService;
    @Resource
    private FlashItemRepository flashItemRepository;

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

        // 持久化库存扣减，在领域服务层引入 FlashItemRepository 来协助完成扣减库存的操作，我们需要保证扣减库存实时有效
        // 所以认为该操作属于业务逻辑，而不是简单的数据访问操作
        boolean success = flashItemRepository.deduct(flashItem.getCode(), quantity);
        if (!success) {
            log.error("[扣减库存] 扣减商品库存失败, 商品编码: {}, 数量: {}", flashItem.getCode(), quantity);
            throw new DomainException("[扣减库存] 扣减商品库存失败, 商品编码: " + flashItem.getCode() + ", 数量: " + quantity);
        }
    }
}
