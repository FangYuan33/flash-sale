package com.actionworks.flashsale.application.command.service.impl;

import com.actionworks.flashsale.application.command.model.FlashItemPublishCommand;
import com.actionworks.flashsale.application.command.service.FlashItemAppCommandService;
import com.actionworks.flashsale.application.convertor.FlashItemConvertor;
import com.actionworks.flashsale.domain.model.item.aggregate.FlashItem;
import com.actionworks.flashsale.domain.service.FlashItemDomainService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
public class FlashItemAppCommandServiceImpl implements FlashItemAppCommandService {

    @Resource
    private FlashItemDomainService flashItemDomainService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void publishFlashItem(FlashItemPublishCommand command) {
        FlashItem flashItem = FlashItemConvertor.publishCommandToDO(command);
        flashItemDomainService.publish(flashItem);
    }

}
