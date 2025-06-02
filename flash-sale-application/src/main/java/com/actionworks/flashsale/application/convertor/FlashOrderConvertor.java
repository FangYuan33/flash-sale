package com.actionworks.flashsale.application.convertor;

import com.actionworks.flashsale.application.command.model.FlashOrderCreateCommand;
import com.actionworks.flashsale.domain.model.aggregate.FlashOrder;

public class FlashOrderConvertor {

    public static FlashOrder createCommandToDomain(FlashOrderCreateCommand command) {
        if (command == null) {
            return null;
        }

        FlashOrder.FlashOrderBuilder builder = FlashOrder.builder();
        builder.itemCode(command.getItemCode());
        builder.userId(command.getUserId());
        builder.quantity(command.getQuantity());

        return builder.build();
    }
}
