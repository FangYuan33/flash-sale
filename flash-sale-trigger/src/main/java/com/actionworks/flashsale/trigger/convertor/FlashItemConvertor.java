package com.actionworks.flashsale.trigger.convertor;

import com.actionworks.flashsale.application.command.model.FlashItemOperateCommand;
import com.actionworks.flashsale.application.command.model.FlashItemPublishCommand;
import com.actionworks.flashsale.application.query.model.req.FlashItemQuery;
import com.actionworks.flashsale.trigger.model.request.FlashItemOperateRequest;
import com.actionworks.flashsale.trigger.model.request.FlashItemPublishRequest;
import com.actionworks.flashsale.trigger.model.request.FlashItemQueryRequest;

public class FlashItemConvertor {

    public static FlashItemQuery toQuery(FlashItemQueryRequest request) {
        if (request == null) {
            return null;
        }

        FlashItemQuery query = new FlashItemQuery();
        query.setItemTitle(request.getItemTitle());
        query.setStatus(request.getStatus());
        query.setPageNum(request.getPageNum());
        query.setPageSize(request.getPageSize());

        return query;
    }

    public static FlashItemPublishCommand toPublishCommand(FlashItemPublishRequest request) {
        FlashItemPublishCommand command = new FlashItemPublishCommand();
        command.setItemTitle(request.getItemTitle());
        command.setItemDesc(request.getItemDesc());
        command.setInitialStock(request.getInitialStock());
        command.setAvailableStock(request.getAvailableStock());
        command.setOriginalPrice(request.getOriginalPrice());
        command.setFlashPrice(request.getFlashPrice());

        return command;
    }

    public static FlashItemOperateCommand toOperateCommand(FlashItemOperateRequest request) {
        FlashItemOperateCommand command = new FlashItemOperateCommand();
        command.setCode(request.getCode());
        command.setStatus(request.getStatus());

        return command;
    }
}
