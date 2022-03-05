package com.actionworks.flashsale.domain.service;

import com.actionworks.flashsale.domain.model.stock.StockDeduction;

public interface StockDeductionDomainService {

    /**
     * 扣减商品库存
     */
    boolean decreaseItemStock(StockDeduction stockDeduction);
}
