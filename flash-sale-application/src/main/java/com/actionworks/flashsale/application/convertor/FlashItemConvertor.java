package com.actionworks.flashsale.application.convertor;

import com.actionworks.flashsale.application.command.model.FlashItemPublishCommand;
import com.actionworks.flashsale.domain.model.aggregate.FlashItem;
import com.actionworks.flashsale.domain.model.entity.StockEntity;
import com.actionworks.flashsale.domain.model.valobj.ItemPrice;

public class FlashItemConvertor {

    public static FlashItem publishCommandToDO(FlashItemPublishCommand command) {
        FlashItem.FlashItemBuilder builder = FlashItem.builder();

        builder.itemTitle(command.getItemTitle());
        builder.itemDesc(command.getItemDesc());

        StockEntity.StockEntityBuilder stock = StockEntity.builder();
        stock.initialStock(command.getInitialStock());
        stock.availableStock(command.getAvailableStock());
        builder.stock(stock.build());

        ItemPrice itemPrice = new ItemPrice(command.getOriginalPrice(), command.getFlashPrice());
        builder.itemPrice(itemPrice);

        return builder.build();
    }
}
