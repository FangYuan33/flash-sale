package com.actionworks.flashsale.controller.model.convertor;

import com.actionworks.flashsale.app.model.command.FlashItemPublishCommand;
import com.actionworks.flashsale.app.model.query.FlashItemQuery;
import com.actionworks.flashsale.controller.model.request.FlashItemPublishRequest;
import com.actionworks.flashsale.controller.model.request.FlashItemQueryRequest;
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

    public static FlashItemQuery toQuery(FlashItemQueryRequest request) {
        if (request == null) {
            return null;
        }

        FlashItemQuery query = new FlashItemQuery();
        BeanUtils.copyProperties(request, query);
        return query;
    }
}
