package com.actionworks.flashsale.persistence.convertor;

import com.actionworks.flashsale.domain.model.entity.FlashOrder;
import com.actionworks.flashsale.persistence.model.FlashOrderDO;
import org.springframework.beans.BeanUtils;

public class FlashOrderConvertor {

    public static FlashOrderDO toDataObject(FlashOrder flashOrder) {
        if (flashOrder == null) {
            return null;
        }

        FlashOrderDO flashOrderDO = new FlashOrderDO();
        BeanUtils.copyProperties(flashOrder, flashOrderDO);
        return flashOrderDO;
    }
}
