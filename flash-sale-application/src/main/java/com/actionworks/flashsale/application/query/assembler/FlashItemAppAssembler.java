package com.actionworks.flashsale.application.query.assembler;

import com.actionworks.flashsale.application.query.model.item.dto.FlashItemDTO;
import com.actionworks.flashsale.domain.model.item.aggregate.FlashItem;
import org.springframework.beans.BeanUtils;

public class FlashItemAppAssembler {

    public static FlashItemDTO toFlashItemDTO(FlashItem flashItem) {
        if (flashItem == null) {
            return null;
        }

        FlashItemDTO flashItemDTO = new FlashItemDTO();
        BeanUtils.copyProperties(flashItem, flashItemDTO);
        return flashItemDTO;
    }

}
