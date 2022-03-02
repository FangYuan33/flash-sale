package com.actionworks.flashsale.app.model.convertor;

import com.actionworks.flashsale.app.model.command.FlashItemPublishCommand;
import com.actionworks.flashsale.app.model.dto.FlashItemDTO;
import com.actionworks.flashsale.app.model.query.FlashItemQuery;
import com.actionworks.flashsale.domain.model.entity.FlashItem;
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
