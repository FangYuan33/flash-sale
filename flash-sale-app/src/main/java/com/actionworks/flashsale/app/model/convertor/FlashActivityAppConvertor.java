package com.actionworks.flashsale.app.model.convertor;

import com.actionworks.flashsale.app.model.command.FlashActivityPublishCommand;
import com.actionworks.flashsale.domain.model.entity.FlashActivity;
import org.springframework.beans.BeanUtils;

/**
 * app层对象转换为Domain层对象
 */
public class FlashActivityAppConvertor {
    public static FlashActivity toDomain(FlashActivityPublishCommand flashActivityPublishCommand) {
        if (flashActivityPublishCommand == null) {
            return null;
        }
        FlashActivity flashActivity = new FlashActivity();
        BeanUtils.copyProperties(flashActivityPublishCommand, flashActivity);
        return flashActivity;
    }
}