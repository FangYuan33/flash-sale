package com.actionworks.flashsale.infrastructure.persistence.convertor;

import com.actionworks.flashsale.domain.model.item.entity.StockEntity;
import com.actionworks.flashsale.infrastructure.persistence.model.StockPO;

public class StockConvertor {

    public static StockPO toPersistentObject(StockEntity stockEntity) {
        StockPO stockPO = new StockPO();
        stockPO.setCode(stockEntity.getCode());
        stockPO.setInitialStock(stockEntity.getInitialStock());
        stockPO.setAvailableStock(stockEntity.getAvailableStock());

        return stockPO;
    }

}
