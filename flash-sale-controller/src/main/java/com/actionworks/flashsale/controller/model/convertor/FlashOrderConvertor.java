package com.actionworks.flashsale.controller.model.convertor;

import com.actionworks.flashsale.app.model.command.FlashPlaceOrderCommand;
import com.actionworks.flashsale.controller.model.request.FlashPlaceOrderRequest;
import org.springframework.beans.BeanUtils;

public class FlashOrderConvertor {

    public static FlashPlaceOrderCommand toCommand(FlashPlaceOrderRequest request) {
        if (request == null) {
            return null;
        }

        FlashPlaceOrderCommand command = new FlashPlaceOrderCommand();
        BeanUtils.copyProperties(request, command);
        return command;
    }
}
