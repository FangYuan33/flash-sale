package com.actionworks.flashsale.application.query.assembler;

import com.actionworks.flashsale.application.query.model.dto.FlashOrderDTO;
import com.actionworks.flashsale.infrastructure.persistence.model.FlashOrderPO;

public class FlashOrderAppAssembler {
    public static FlashOrderDTO toDTO(FlashOrderPO flashOrderPO) {
        if (flashOrderPO == null) {
            return null;
        }

        return new FlashOrderDTO()
                .setId(flashOrderPO.getId())
                .setCode(flashOrderPO.getCode())
                .setUserId(flashOrderPO.getUserId())
                .setItemCode(flashOrderPO.getItemCode())
                .setQuantity(flashOrderPO.getQuantity())
                .setTotalAmount(flashOrderPO.getTotalAmount())
                .setStatus(flashOrderPO.getStatus());
    }
} 