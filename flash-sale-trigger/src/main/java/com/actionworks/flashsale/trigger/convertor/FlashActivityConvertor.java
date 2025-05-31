package com.actionworks.flashsale.trigger.convertor;

import com.actionworks.flashsale.application.command.model.FlashActivityOperateCommand;
import com.actionworks.flashsale.application.command.model.FlashActivityPublishCommand;
import com.actionworks.flashsale.trigger.model.request.FlashActivityOperateRequest;
import com.actionworks.flashsale.trigger.model.request.FlashActivityPublishRequest;

public class FlashActivityConvertor {

    public static FlashActivityPublishCommand toPublishCommand(FlashActivityPublishRequest request) {
        FlashActivityPublishCommand command = new FlashActivityPublishCommand();
        command.setActivityName(request.getActivityName());
        command.setActivityDesc(request.getActivityDesc());
        command.setFlashItemCode(request.getFlashItemCode());
        command.setStartTime(request.getStartTime());
        command.setEndTime(request.getEndTime());

        return command;
    }

    public static FlashActivityOperateCommand toOperateCommand(FlashActivityOperateRequest request) {
        FlashActivityOperateCommand command = new FlashActivityOperateCommand();
        command.setCode(request.getCode());
        command.setStatus(request.getStatus());
        return command;
    }
}
