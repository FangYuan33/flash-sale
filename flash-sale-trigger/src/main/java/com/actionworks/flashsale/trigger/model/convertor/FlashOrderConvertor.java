package com.actionworks.flashsale.trigger.model.convertor;

import com.actionworks.flashsale.application.model.command.FlashPlaceOrderCommand;
import com.actionworks.flashsale.application.model.query.FlashOrderQuery;
import com.actionworks.flashsale.trigger.model.request.FlashOrderPlaceRequest;
import com.actionworks.flashsale.trigger.model.request.FlashOrderQueryRequest;
import org.springframework.beans.BeanUtils;

public class FlashOrderConvertor {

    public static FlashPlaceOrderCommand toCommand(FlashOrderPlaceRequest request) {
        if (request == null) {
            return null;
        }

        FlashPlaceOrderCommand command = new FlashPlaceOrderCommand();
        BeanUtils.copyProperties(request, command);
        return command;
    }

    public static FlashOrderQuery toQuery(FlashOrderQueryRequest request) {
        if (request == null) {
            return null;
        }

        FlashOrderQuery query = new FlashOrderQuery();
        BeanUtils.copyProperties(request, query);
        return query;
    }
}
