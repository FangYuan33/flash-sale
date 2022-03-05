package com.actionworks.flashsale.app.model.convertor;

import com.actionworks.flashsale.app.model.command.FlashPlaceOrderCommand;
import com.actionworks.flashsale.domain.model.entity.FlashOrder;
import org.springframework.beans.BeanUtils;

public class FlashOrderAppConvertor {

    public static FlashOrder toDomain(FlashPlaceOrderCommand command) {
        if (command == null) {
            return null;
        }

        FlashOrder flashOrder = new FlashOrder();
        BeanUtils.copyProperties(command, flashOrder);
        return flashOrder;
    }
}
