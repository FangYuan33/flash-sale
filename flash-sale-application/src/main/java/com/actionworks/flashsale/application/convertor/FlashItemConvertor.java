package com.actionworks.flashsale.application.convertor;

import com.actionworks.flashsale.application.command.model.FlashItemPublishCommand;
import com.actionworks.flashsale.application.query.model.req.FlashItemQuery;
import com.actionworks.flashsale.domain.model.item.aggregate.FlashItem;
import com.actionworks.flashsale.domain.model.item.entity.StockEntity;
import com.actionworks.flashsale.domain.model.item.valobj.ItemPrice;
import com.actionworks.flashsale.domain.model.query.FlashItemQueryCondition;

public class FlashItemConvertor {

    public static FlashItemQueryCondition query2QueryCondition(FlashItemQuery query) {
        FlashItemQueryCondition condition = new FlashItemQueryCondition();
        condition.setItemTitle(query.getItemTitle()).setStatus(query.getStatus());
        condition.setPageNum(query.getPageNum());
        condition.setPageSize(query.getPageSize());

        return condition;
    }

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
