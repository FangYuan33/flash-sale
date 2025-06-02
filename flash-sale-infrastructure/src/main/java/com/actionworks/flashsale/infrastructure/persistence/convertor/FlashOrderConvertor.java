package com.actionworks.flashsale.infrastructure.persistence.convertor;

import com.actionworks.flashsale.domain.model.aggregate.FlashOrder;
import com.actionworks.flashsale.domain.model.enums.FlashOrderStatus;
import com.actionworks.flashsale.infrastructure.persistence.model.FlashOrderPO;

public class FlashOrderConvertor {

    public static FlashOrderPO toFlashOrderPO(FlashOrder flashOrder) {
        if (flashOrder == null) {
            return null;
        }

        FlashOrderPO flashOrderPO = new FlashOrderPO();
        flashOrderPO.setCode(flashOrder.getCode());
        flashOrderPO.setItemCode(flashOrder.getItemCode());
        flashOrderPO.setUserId(flashOrder.getUserId());
        flashOrderPO.setItemTitle(flashOrder.getItemTitle());
        flashOrderPO.setQuantity(flashOrder.getQuantity());
        flashOrderPO.setTotalAmount(flashOrder.getTotalAmount());
        flashOrderPO.setStatus(flashOrder.getStatus().getCode());
        return flashOrderPO;
    }

    public static FlashOrder toFlashOrder(FlashOrderPO flashOrderPO) {
        if (flashOrderPO == null) {
            return null;
        }
        return FlashOrder.builder()
                .code(flashOrderPO.getCode())
                .itemCode(flashOrderPO.getItemCode())
                .userId(flashOrderPO.getUserId())
                .itemTitle(flashOrderPO.getItemTitle())
                .quantity(flashOrderPO.getQuantity())
                .totalAmount(flashOrderPO.getTotalAmount())
                .status(FlashOrderStatus.parse(flashOrderPO.getStatus()))
                .build();
    }
}
