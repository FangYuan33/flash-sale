package com.actionworks.flashsale.application.query.convertor;

import com.actionworks.flashsale.application.model.command.FlashItemPublishCommand;
import com.actionworks.flashsale.application.model.dto.FlashItemDTO;
import com.actionworks.flashsale.application.query.model.FlashItemQuery;
import com.actionworks.flashsale.domain.model.item.aggregate.FlashItem;
import com.actionworks.flashsale.domain.model.query.FlashItemQueryCondition;
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

    public static FlashItemDTO toFlashItemDTO(FlashItem flashItem) {
        if (flashItem == null) {
            return null;
        }

        FlashItemDTO flashItemDTO = new FlashItemDTO();
        BeanUtils.copyProperties(flashItem, flashItemDTO);
        return flashItemDTO;
    }

    public static FlashItemQueryCondition toFlashItemQueryCondition(FlashItemQuery query) {
        if (query == null) {
            return null;
        }

        FlashItemQueryCondition queryCondition = new FlashItemQueryCondition();
        BeanUtils.copyProperties(query, queryCondition);
        return queryCondition;
    }
}
