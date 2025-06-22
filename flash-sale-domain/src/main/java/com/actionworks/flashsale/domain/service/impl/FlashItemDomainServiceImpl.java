package com.actionworks.flashsale.domain.service.impl;

import com.actionworks.flashsale.common.exception.DomainException;
import com.actionworks.flashsale.domain.adapter.CodeGenerateService;
import com.actionworks.flashsale.domain.model.aggregate.FlashItem;
import com.actionworks.flashsale.domain.model.enums.FlashItemStatus;
import com.actionworks.flashsale.domain.repository.FlashItemRepository;
import com.actionworks.flashsale.domain.repository.StockRepository;
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
    @Resource
    private StockRepository stockRepository;

    @Override
    public void publish(FlashItem flashItem) {
        // 发布商品（领域模型只处理核心业务逻辑）
        flashItem.publish();
        
        // 在领域服务层处理外部依赖（编码生成）
        String itemCode = codeGenerateService.generateCode();
        flashItem.assignCode(itemCode);
        
        flashItemRepository.save(flashItem);
    }

    @Override
    public void changeItemStatus(String code, Integer status) {
        FlashItem flashItem = flashItemRepository.findByCode(code);
        if (flashItem == null) {
            throw new DomainException("[变更商品状态] 品：" + code + " 不存在");
        }
        // 变更状态
        flashItem.changeStatus(FlashItemStatus.parseByCode(status));
        flashItemRepository.modifyStatus(flashItem);
    }

    @Override
    public FlashItem deductStock(String itemCode, Integer quantity) {
        FlashItem flashItem = flashItemRepository.findByCode(itemCode);
        if (flashItem == null) {
            throw new DomainException("[扣减库存] 商品不存在: " + itemCode);
        }
        flashItem.deductStock(quantity);
        int success = stockRepository.deduct(itemCode, quantity);
        if (success <= 0) {
            log.error("[扣减库存] 扣减商品库存失败, 商品编码: {}, 数量: {}", itemCode, quantity);
            throw new DomainException("[扣减库存] 扣减商品库存失败, 商品编码: " + itemCode + ", 数量: " + quantity);
        }

        return flashItem;
    }

}
