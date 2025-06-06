package com.actionworks.flashsale.application.ability.impl;

import com.actionworks.flashsale.application.ability.AbilityService;
import com.actionworks.flashsale.application.command.model.FlashOrderCreateCommand;
import com.actionworks.flashsale.application.convertor.FlashOrderConvertor;
import com.actionworks.flashsale.common.exception.AppException;
import com.actionworks.flashsale.domain.adapter.CodeGenerateService;
import com.actionworks.flashsale.domain.event.DomainEventPublisher;
import com.actionworks.flashsale.domain.model.aggregate.FlashItem;
import com.actionworks.flashsale.domain.model.aggregate.FlashOrder;
import com.actionworks.flashsale.domain.model.event.CreateOrderEvent;
import com.actionworks.flashsale.domain.repository.FlashOrderRepository;
import com.actionworks.flashsale.domain.service.FlashItemDomainService;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;

@Service
public class CreateOrderAbility implements AbilityService<FlashOrderCreateCommand, Void> {

    @Resource
    private FlashItemDomainService flashItemDomainService;
    @Resource
    private CodeGenerateService codeGenerateService;

    @Resource
    private FlashOrderRepository flashOrderRepository;

    @Resource
    private DomainEventPublisher domainEventPublisher;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Void execute(FlashOrderCreateCommand flashOrderCreateCommand) {
        checkCreateCommand(flashOrderCreateCommand);
        FlashOrder flashOrder = FlashOrderConvertor.createCommandToDomain(flashOrderCreateCommand);
        // 扣减库存
        FlashItem flashItem = flashItemDomainService.deductStock(flashOrderCreateCommand.getItemCode(), flashOrderCreateCommand.getQuantity());
        // 创建订单
        flashOrder.create(codeGenerateService, flashItem);
        flashOrderRepository.save(flashOrder);

        CreateOrderEvent createOrderEvent =
                new CreateOrderEvent(String.valueOf(flashOrder.getId()), JSONObject.toJSONString(flashOrder), LocalDateTime.now());
        domainEventPublisher.publishEvent(createOrderEvent);

        return null;
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
