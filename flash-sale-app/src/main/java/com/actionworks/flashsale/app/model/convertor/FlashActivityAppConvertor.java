package com.actionworks.flashsale.app.model.convertor;

import com.actionworks.flashsale.app.model.command.FlashActivityPublishCommand;
import com.actionworks.flashsale.app.model.dto.FlashActivityDTO;
import com.actionworks.flashsale.domain.model.entity.FlashActivity;
import org.springframework.beans.BeanUtils;

/**
 * app层对象转换器
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

    public static FlashActivityDTO toFlashActivityDTO(FlashActivity flashActivity) {
        if (flashActivity == null) {
            return null;
        }

        FlashActivityDTO flashActivityDTO = new FlashActivityDTO();
        BeanUtils.copyProperties(flashActivity, flashActivityDTO);
        return flashActivityDTO;
    }
}