package com.actionworks.flashsale.infrastructure.persistence.convertor;

import com.actionworks.flashsale.domain.model.item.aggregate.FlashItem;
import com.actionworks.flashsale.domain.model.item.entity.StockEntity;
import com.actionworks.flashsale.domain.model.item.enums.FlashItemStatus;
import com.actionworks.flashsale.domain.model.item.valobj.ItemPrice;
import com.actionworks.flashsale.infrastructure.persistence.model.FlashItemPO;
import com.actionworks.flashsale.infrastructure.persistence.model.StockPO;
import org.springframework.beans.BeanUtils;

public class FlashItemConvertor {

    public static FlashItemPO toPersistentObject(FlashItem flashItem) {
        if (flashItem == null) {
            return null;
        }

        FlashItemPO flashItemDO = new FlashItemPO();
        BeanUtils.copyProperties(flashItem, flashItemDO);
        return flashItemDO;
    }

    public static FlashItem toDomainObject(FlashItemPO flashItemDO) {
        if (flashItemDO == null) {
            return null;
        }

        FlashItem.FlashItemBuilder builder = FlashItem.builder();
        initialFlashItemInfo(builder, flashItemDO);

        return builder.build();
    }

    public static FlashItem toDomainObject(FlashItemPO flashItemDO, StockPO stockPO) {
        if (flashItemDO == null) {
            return null;
        }

        FlashItem.FlashItemBuilder builder = FlashItem.builder();
        initialFlashItemInfo(builder, flashItemDO);
        StockEntity.StockEntityBuilder stockBuilder = StockEntity.builder();
        initialStockInfo(stockBuilder, stockPO);
        builder.stock(stockBuilder.build());

        return builder.build();
    }

    private static void initialFlashItemInfo(FlashItem.FlashItemBuilder builder, FlashItemPO flashItemDO) {
        builder.id(flashItemDO.getId());
        builder.code(flashItemDO.getCode());
        builder.itemTitle(flashItemDO.getItemTitle());
        builder.itemDesc(flashItemDO.getItemDesc());
        builder.status(FlashItemStatus.parseByCode(flashItemDO.getStatus()));
        ItemPrice itemPrice = new ItemPrice(flashItemDO.getOriginalPrice(), flashItemDO.getFlashPrice());
        builder.itemPrice(itemPrice);
    }

    private static void initialStockInfo(StockEntity.StockEntityBuilder stockBuilder, StockPO stockPO) {
        stockBuilder.id(stockPO.getId()).code(stockPO.getCode())
                .initialStock(stockPO.getInitialStock()).availableStock(stockPO.getAvailableStock());
    }
}
