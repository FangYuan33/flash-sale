package com.actionworks.flashsale.application.query.convertor;

import com.actionworks.flashsale.application.model.command.FlashActivityPublishCommand;
import com.actionworks.flashsale.application.model.dto.FlashActivityDTO;
import com.actionworks.flashsale.application.model.query.FlashActivitiesQuery;
import com.actionworks.flashsale.domain.model.activity.aggregate.FlashActivity;
import com.actionworks.flashsale.domain.model.query.FlashActivityQueryCondition;
import org.springframework.beans.BeanUtils;

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

    public static FlashActivityQueryCondition toFlashActivityQueryCondition(FlashActivitiesQuery flashActivitiesQuery) {
        if (flashActivitiesQuery == null) {
            return null;
        }

        FlashActivityQueryCondition queryCondition = new FlashActivityQueryCondition();
        BeanUtils.copyProperties(flashActivitiesQuery, queryCondition);
        return queryCondition;
    }
}