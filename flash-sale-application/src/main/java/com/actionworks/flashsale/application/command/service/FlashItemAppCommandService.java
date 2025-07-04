package com.actionworks.flashsale.application.command.service;

import com.actionworks.flashsale.application.command.model.FlashItemOperateCommand;
import com.actionworks.flashsale.application.command.model.FlashItemPublishCommand;

public interface FlashItemAppCommandService {

    void publishFlashItem(FlashItemPublishCommand command);

    void operateFlashItem(FlashItemOperateCommand command);
}
