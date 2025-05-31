package com.actionworks.flashsale.application.command.service;

import com.actionworks.flashsale.application.command.model.FlashActivityOperateCommand;
import com.actionworks.flashsale.application.command.model.FlashActivityPublishCommand;

public interface FlashActivityAppCommandService {

    void publishFlashActivity(FlashActivityPublishCommand command);

    void operateFlashActivity(FlashActivityOperateCommand command);

}
