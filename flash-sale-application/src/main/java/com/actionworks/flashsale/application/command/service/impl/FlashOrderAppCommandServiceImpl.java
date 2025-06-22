package com.actionworks.flashsale.application.command.service.impl;

import com.actionworks.flashsale.application.command.model.FlashOrderCreateCommand;
import com.actionworks.flashsale.application.command.service.FlashOrderAppCommandService;
import com.actionworks.flashsale.application.convertor.FlashOrderConvertor;
import com.actionworks.flashsale.common.exception.AppException;
import com.actionworks.flashsale.domain.model.aggregate.FlashItem;
import com.actionworks.flashsale.domain.model.aggregate.FlashOrder;
import com.actionworks.flashsale.domain.repository.FlashItemRepository;
import com.actionworks.flashsale.domain.repository.FlashOrderRepository;
import com.actionworks.flashsale.domain.repository.StockRepository;
import com.actionworks.flashsale.domain.service.FlashItemDomainService;
import com.actionworks.flashsale.domain.service.FlashOrderDomainService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Slf4j
@Service
public class FlashOrderAppCommandServiceImpl implements FlashOrderAppCommandService {

    @Resource
    private FlashOrderDomainService flashOrderDomainService;
    @Resource
    private FlashItemDomainService flashItemDomainService;
    @Resource
    private FlashItemRepository flashItemRepository;
    @Resource
    private FlashOrderRepository flashOrderRepository;
    @Resource
    private StockRepository stockRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createOrder(FlashOrderCreateCommand createCommand) {
        checkCreateCommand(createCommand);

        // 1. 获取商品信息（只查询一次）
        FlashItem flashItem = flashItemRepository.findByCode(createCommand.getItemCode());
        if (flashItem == null) {
            throw new AppException("[创建秒杀订单] 商品不存在: " + createCommand.getItemCode());
        }

        // 2. 扣减库存（领域服务只处理业务逻辑）
        flashItemDomainService.deductStock(flashItem, createCommand.getQuantity());
        
        // 3. 持久化库存扣减（应用层负责数据访问）
        int success = stockRepository.deduct(flashItem.getCode(), createCommand.getQuantity());
        if (success <= 0) {
            log.error("[扣减库存] 扣减商品库存失败, 商品编码: {}, 数量: {}", flashItem.getCode(), createCommand.getQuantity());
            throw new AppException("[扣减库存] 扣减商品库存失败, 商品编码: " + flashItem.getCode() + ", 数量: " + createCommand.getQuantity());
        }

        // 4. 创建订单（领域服务只处理业务逻辑）
        FlashOrder flashOrder = FlashOrderConvertor.createCommandToDomain(createCommand);
        flashOrderDomainService.createOrder(flashOrder, flashItem);
        
        // 5. 持久化订单（应用层负责数据访问）
        flashOrderRepository.save(flashOrder);
    }

    private void checkCreateCommand(FlashOrderCreateCommand createCommand) {
        if (createCommand == null) {
            throw new AppException("[创建秒杀订单] 命令对象不能为空");
        }
        if (StringUtils.isBlank(createCommand.getItemCode())) {
            throw new AppException("[创建秒杀订单] 商品编码不能为空");
        }
        if (createCommand.getUserId() == null) {
            throw new AppException("[创建秒杀订单] 用户ID不能为空");
        }
        if (createCommand.getQuantity() == null || createCommand.getQuantity() <= 0) {
            throw new AppException("[创建秒杀订单] 数量必须大于 0");
        }
    }

}