package com.actionworks.flashsale.trigger.convertor;

import com.actionworks.flashsale.application.command.model.FlashOrderCreateCommand;
import com.actionworks.flashsale.trigger.model.request.FlashOrderCreateRequest;

public class FlashOrderConvertor {

    public static FlashOrderCreateCommand toCreateCommand(FlashOrderCreateRequest request) {
        if (request == null) {
            return null;
        }

        FlashOrderCreateCommand command = new FlashOrderCreateCommand();
        command.setItemCode(request.getItemCode());
        command.setUserId(request.getUserId());
        command.setQuantity(request.getQuantity());
        return command;
    }
}
