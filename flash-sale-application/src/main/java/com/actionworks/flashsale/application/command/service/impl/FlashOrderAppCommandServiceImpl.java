package com.actionworks.flashsale.application.command.service.impl;

import com.actionworks.flashsale.application.command.model.FlashOrderCreateCommand;
import com.actionworks.flashsale.application.command.service.FlashOrderAppCommandService;
import com.actionworks.flashsale.application.convertor.FlashOrderConvertor;
import com.actionworks.flashsale.common.exception.AppException;
import com.actionworks.flashsale.domain.model.aggregate.FlashOrder;
import com.actionworks.flashsale.domain.service.FlashOrderDomainService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
public class FlashOrderAppCommandServiceImpl implements FlashOrderAppCommandService {

    @Resource
    private FlashOrderDomainService flashOrderDomainService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createOrder(FlashOrderCreateCommand createCommand) {
        checkCreateCommand(createCommand);

        FlashOrder flashOrder = FlashOrderConvertor.createCommandToDomain(createCommand);
        flashOrderDomainService.createOrder(flashOrder);
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