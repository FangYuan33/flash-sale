package com.actionworks.flashsale.controller.model.convertor;

import com.actionworks.flashsale.app.model.command.FlashPlaceOrderCommand;
import com.actionworks.flashsale.app.model.query.FlashOrderQuery;
import com.actionworks.flashsale.controller.model.request.FlashOrderPlaceRequest;
import com.actionworks.flashsale.controller.model.request.FlashOrderQueryRequest;
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
