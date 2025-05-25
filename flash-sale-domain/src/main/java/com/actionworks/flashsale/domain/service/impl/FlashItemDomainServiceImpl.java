package com.actionworks.flashsale.domain.service.impl;

import com.actionworks.flashsale.domain.adapter.ItemCodeGenerateService;
import com.actionworks.flashsale.domain.model.item.aggregate.FlashItem;
import com.actionworks.flashsale.domain.repository.FlashItemRepository;
import com.actionworks.flashsale.domain.service.FlashItemDomainService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Slf4j
@Service
public class FlashItemDomainServiceImpl implements FlashItemDomainService {

    @Resource
    private ItemCodeGenerateService itemCodeGenerateService;
    @Resource
    private FlashItemRepository flashItemRepository;

    @Override
    public void publish(FlashItem flashItem) {
        // 执行发布逻辑
        flashItem.publish(itemCodeGenerateService);
        flashItemRepository.save(flashItem);
    }

}
