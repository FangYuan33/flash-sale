package com.actionworks.flashsale.infrastructure.persistence.convertor;

import com.actionworks.flashsale.domain.model.aggregate.FlashActivity;
import com.actionworks.flashsale.domain.model.aggregate.FlashItem;
import com.actionworks.flashsale.domain.model.enums.FlashActivityStatus;
import com.actionworks.flashsale.infrastructure.persistence.model.FlashActivityPO;

public class FlashActivityConvertor {

    public static FlashActivityPO toPersistentObject(FlashActivity flashActivity) {
        if (flashActivity == null) {
            return null;
        }

        FlashActivityPO flashActivityPO = new FlashActivityPO();
        flashActivityPO.setCode(flashActivity.getCode());
        flashActivityPO.setActivityName(flashActivity.getActivityName());
        flashActivityPO.setActivityDesc(flashActivity.getActivityDesc());
        flashActivityPO.setItemCode(flashActivity.getFlashItem().getCode());
        flashActivityPO.setStartTime(flashActivity.getStartTime());
        flashActivityPO.setEndTime(flashActivity.getEndTime());
        flashActivityPO.setStatus(flashActivity.getStatus().getCode());
        return flashActivityPO;
    }

    public static FlashActivity toDomainObject(FlashActivityPO flashActivityPO) {
        if (flashActivityPO == null) {
            return null;
        }

        FlashActivity.FlashActivityBuilder builder = getFlashActivityBuilder(flashActivityPO);

        return builder.build();
    }

    public static FlashActivity toDomainObject(FlashActivityPO flashActivityPO, FlashItem flashItem) {
        FlashActivity.FlashActivityBuilder flashActivityBuilder = getFlashActivityBuilder(flashActivityPO);
        flashActivityBuilder.flashItem(flashItem);

        return flashActivityBuilder.build();
    }

    private static FlashActivity.FlashActivityBuilder getFlashActivityBuilder(FlashActivityPO flashActivityPO) {
        FlashActivity.FlashActivityBuilder builder = FlashActivity.builder();
        builder.id(flashActivityPO.getId());
        builder.code(flashActivityPO.getCode());
        builder.activityName(flashActivityPO.getActivityName());
        builder.activityDesc(flashActivityPO.getActivityDesc());
        builder.startTime(flashActivityPO.getStartTime());
        builder.endTime(flashActivityPO.getEndTime());
        builder.status(FlashActivityStatus.parse(flashActivityPO.getStatus()));
        return builder;
    }
}
