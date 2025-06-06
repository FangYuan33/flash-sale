package com.actionworks.flashsale.application.command.service.impl;

import com.actionworks.flashsale.application.ability.AbilityService;
import com.actionworks.flashsale.application.command.model.FlashOrderCreateCommand;
import com.actionworks.flashsale.application.command.service.FlashOrderAppCommandService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class FlashOrderAppCommandServiceImpl implements FlashOrderAppCommandService {

    @Resource
    private AbilityService<FlashOrderCreateCommand, Void> createOrderAbility;

    @Override
    public void createOrder(FlashOrderCreateCommand createCommand) {
        createOrderAbility.execute(createCommand);
    }

}