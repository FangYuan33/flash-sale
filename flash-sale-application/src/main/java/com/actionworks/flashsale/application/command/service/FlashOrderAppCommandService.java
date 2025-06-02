package com.actionworks.flashsale.application.command.service;

import com.actionworks.flashsale.application.command.model.FlashOrderCreateCommand;

public interface FlashOrderAppCommandService {

    void createOrder(FlashOrderCreateCommand flashOrder);

}
