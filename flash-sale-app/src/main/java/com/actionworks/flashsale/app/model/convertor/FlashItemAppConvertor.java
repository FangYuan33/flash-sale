package com.actionworks.flashsale.app.model.convertor;

import com.actionworks.flashsale.app.model.command.FlashItemPublishCommand;
import com.actionworks.flashsale.domain.model.entity.FlashItem;
import org.springframework.beans.BeanUtils;

public class FlashItemAppConvertor {

    public static FlashItem toDomain(FlashItemPublishCommand command) {
        if (command == null) {
            return null;
        }

        FlashItem flashItem = new FlashItem();
        BeanUtils.copyProperties(command, flashItem);
        return flashItem;
    }
}
