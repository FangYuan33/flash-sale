package com.actionworks.flashsale.application.query.assembler;

import com.actionworks.flashsale.application.query.model.dto.FlashItemDTO;
import com.actionworks.flashsale.infrastructure.persistence.model.FlashItemPO;
import com.actionworks.flashsale.infrastructure.persistence.model.StockPO;

public class FlashItemAppAssembler {

    public static FlashItemDTO toFlashItemDTO(FlashItemPO flashItemPO, StockPO stockPO) {
        if (flashItemPO == null) {
            return null;
        }
        FlashItemDTO flashItemDTO = new FlashItemDTO();
        flashItemDTO.setId(flashItemPO.getId());
        flashItemDTO.setCode(flashItemPO.getCode());
        flashItemDTO.setItemTitle(flashItemPO.getItemTitle());
        flashItemDTO.setItemDesc(flashItemPO.getItemDesc());
        if (stockPO != null) {
            flashItemDTO.setInitialStock(stockPO.getInitialStock());
            flashItemDTO.setAvailableStock(stockPO.getAvailableStock());
        }
        flashItemDTO.setOriginalPrice(flashItemPO.getOriginalPrice());
        flashItemDTO.setFlashPrice(flashItemPO.getFlashPrice());
        flashItemDTO.setStatus(flashItemPO.getStatus());
        return flashItemDTO;
    }

}
