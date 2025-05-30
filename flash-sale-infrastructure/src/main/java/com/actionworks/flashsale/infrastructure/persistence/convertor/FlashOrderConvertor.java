package com.actionworks.flashsale.infrastructure.persistence.convertor;

import com.actionworks.flashsale.domain.model.order.aggregate.FlashOrder;
import com.actionworks.flashsale.infrastructure.persistence.model.FlashOrderDO;
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

    public static FlashOrder toDomainObject(FlashOrderDO flashOrderDO) {
        if (flashOrderDO == null) {
            return null;
        }

        FlashOrder flashOrder = new FlashOrder();
        BeanUtils.copyProperties(flashOrderDO, flashOrder);
        return flashOrder;
    }
}
