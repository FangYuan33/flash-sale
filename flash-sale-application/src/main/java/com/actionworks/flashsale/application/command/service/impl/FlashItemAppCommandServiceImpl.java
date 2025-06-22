package com.actionworks.flashsale.application.command.service.impl;

import com.actionworks.flashsale.application.command.model.FlashItemOperateCommand;
import com.actionworks.flashsale.application.command.model.FlashItemPublishCommand;
import com.actionworks.flashsale.application.command.service.FlashItemAppCommandService;
import com.actionworks.flashsale.application.convertor.FlashItemConvertor;
import com.actionworks.flashsale.common.exception.AppException;
import com.actionworks.flashsale.domain.model.aggregate.FlashItem;
import com.actionworks.flashsale.domain.repository.FlashItemRepository;
import com.actionworks.flashsale.domain.service.FlashItemDomainService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
public class FlashItemAppCommandServiceImpl implements FlashItemAppCommandService {

    @Resource
    private FlashItemDomainService flashItemDomainService;
    @Resource
    private FlashItemRepository flashItemRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void publishFlashItem(FlashItemPublishCommand command) {
        checkPublishCommand(command);
        
        // 创建商品对象
        FlashItem flashItem = FlashItemConvertor.publishCommandToDO(command);
        
        // 发布商品（领域服务只处理业务逻辑）
        flashItemDomainService.publish(flashItem);
        
        // 持久化商品（应用层负责数据访问）
        flashItemRepository.save(flashItem);
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
        checkOperateCommand(command);
        
        // 获取商品信息
        FlashItem flashItem = flashItemRepository.findByCode(command.getCode());
        if (flashItem == null) {
            throw new AppException("[操作秒杀商品] 商品不存在: " + command.getCode());
        }
        
        // 变更商品状态（领域服务只处理业务逻辑）
        flashItemDomainService.changeItemStatus(flashItem, command.getStatus());
        
        // 持久化商品状态（应用层负责数据访问）
        flashItemRepository.modifyStatus(flashItem);
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
