package com.actionworks.flashsale.application.assembler;

import com.actionworks.flashsale.application.query.model.dto.FlashItemDTO;
import com.actionworks.flashsale.domain.model.item.aggregate.FlashItem;
import com.actionworks.flashsale.domain.model.item.entity.StockEntity;
import com.actionworks.flashsale.domain.model.item.valobj.ItemPrice;

public class FlashItemAppAssembler {

    public static FlashItemDTO toFlashItemDTO(FlashItem flashItem) {
        if (flashItem == null) {
            return null;
        }

        FlashItemDTO flashItemDTO = new FlashItemDTO();
        flashItemDTO.setId(flashItem.getId());
        flashItemDTO.setItemTitle(flashItem.getItemTitle());
        flashItemDTO.setItemDesc(flashItem.getItemDesc());
        if (flashItem.getStock() != null) {
            StockEntity stock = flashItem.getStock();
            flashItemDTO.setInitialStock(stock.getInitialStock());
            flashItemDTO.setAvailableStock(stock.getAvailableStock());
        }
        ItemPrice itemPrice = flashItem.getItemPrice();
        flashItemDTO.setOriginalPrice(itemPrice.getOriginalPrice());
        flashItemDTO.setFlashPrice(itemPrice.getFlashPrice());
        flashItemDTO.setStatus(flashItem.getStatus().getCode());
        
        return flashItemDTO;
    }

}
