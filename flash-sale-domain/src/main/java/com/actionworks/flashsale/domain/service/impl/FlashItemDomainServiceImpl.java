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
        // 执行发布逻辑
        flashItem.publish(codeGenerateService);
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

}
