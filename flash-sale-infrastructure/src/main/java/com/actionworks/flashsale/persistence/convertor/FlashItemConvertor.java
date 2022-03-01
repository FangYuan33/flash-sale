package com.actionworks.flashsale.persistence.convertor;

import com.actionworks.flashsale.domain.model.entity.FlashItem;
import com.actionworks.flashsale.persistence.model.FlashItemDO;
import org.springframework.beans.BeanUtils;

public class FlashItemConvertor {

    public static FlashItemDO toDataObject(FlashItem flashItem) {
        if (flashItem == null) {
            return null;
        }

        FlashItemDO flashItemDO = new FlashItemDO();
        BeanUtils.copyProperties(flashItem, flashItemDO);
        return flashItemDO;
    }
}
