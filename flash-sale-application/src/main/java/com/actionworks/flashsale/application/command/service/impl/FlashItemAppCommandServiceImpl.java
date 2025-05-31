package com.actionworks.flashsale.application.command.service.impl;

import com.actionworks.flashsale.application.command.model.FlashItemOperateCommand;
import com.actionworks.flashsale.application.command.model.FlashItemPublishCommand;
import com.actionworks.flashsale.application.command.service.FlashItemAppCommandService;
import com.actionworks.flashsale.application.convertor.FlashItemConvertor;
import com.actionworks.flashsale.common.exception.AppException;
import com.actionworks.flashsale.domain.model.aggregate.FlashItem;
import com.actionworks.flashsale.domain.service.FlashItemDomainService;
import org.apache.commons.lang3.StringUtils;
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
        checkPublishCommand(command);
        FlashItem flashItem = FlashItemConvertor.publishCommandToDO(command);
        flashItemDomainService.publish(flashItem);
    }

    private void checkPublishCommand(FlashItemPublishCommand command) {
        if (StringUtils.isBlank(command.getItemTitle())) {
            throw new AppException("[发布秒杀商品] 标题为空");
        }
        if (StringUtils.isBlank(command.getItemDesc())) {
            throw new AppException("[发布秒杀商品] 描述为空");
        }
        if (command.getInitialStock() == null) {
            throw new AppException("[发布秒杀商品] 初始库存为空");
        }
        if (command.getAvailableStock() == null) {
            throw new AppException("[发布秒杀商品] 可用库存为空");
        }
        if (command.getOriginalPrice() == null) {
            throw new AppException("[发布秒杀商品] 秒杀品原价为空");
        }
        if (command.getFlashPrice() == null) {
            throw new AppException("[发布秒杀商品] 秒杀价为空");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void operateFlashItem(FlashItemOperateCommand command) {
        flashItemDomainService.changeItemStatus(command.getCode(), command.getStatus());
    }

    private void checkOperateCommand(FlashItemOperateCommand command) {
        if (StringUtils.isBlank(command.getCode())) {
            throw new AppException("[操作秒杀商品] 商品编码为空");
        }
        if (command.getStatus() == null) {
            throw new AppException("[操作秒杀商品] 操作状态为空");
        }
    }
}
