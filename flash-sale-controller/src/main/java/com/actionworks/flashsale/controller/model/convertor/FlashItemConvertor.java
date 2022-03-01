package com.actionworks.flashsale.controller.model.convertor;

import com.actionworks.flashsale.app.model.command.FlashItemPublishCommand;
import com.actionworks.flashsale.controller.model.request.FlashItemPublishRequest;
import org.springframework.beans.BeanUtils;

public class FlashItemConvertor {

    public static FlashItemPublishCommand toCommand(FlashItemPublishRequest request) {
        if (request == null) {
            return null;
        }

        FlashItemPublishCommand command = new FlashItemPublishCommand();
        BeanUtils.copyProperties(request, command);

        return command;
    }
}
