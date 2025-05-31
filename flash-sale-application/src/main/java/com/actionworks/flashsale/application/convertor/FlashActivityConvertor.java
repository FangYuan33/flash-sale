package com.actionworks.flashsale.application.convertor;

import com.actionworks.flashsale.application.command.model.FlashActivityPublishCommand;
import com.actionworks.flashsale.domain.model.aggregate.FlashActivity;
import com.actionworks.flashsale.domain.model.aggregate.FlashItem;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FlashActivityConvertor {

    public static FlashActivity publishCommandToDO(FlashActivityPublishCommand command) {
        FlashActivity.FlashActivityBuilder builder = FlashActivity.builder();

        // 将 yyyy-MM-dd HH:mm:ss 格式的时间字符串转换为 LocalDateTime
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime startTime = LocalDateTime.parse(command.getStartTime(), dateTimeFormatter);
        LocalDateTime endTime = LocalDateTime.parse(command.getEndTime(), dateTimeFormatter);

        // 关联 FlashItem
        FlashItem flashItem = FlashItem.builder()
                .code(command.getFlashItemCode())
                .build();

        builder.activityName(command.getActivityName())
                .activityDesc(command.getActivityDesc())
                .flashItem(flashItem)
                .startTime(startTime)
                .endTime(endTime);

        return builder.build();
    }
}

