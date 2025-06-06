package com.actionworks.flashsale.domain.service;

import com.actionworks.flashsale.domain.model.item.valobj.StockDeduction;

public interface StockDeductionDomainService {

    /**
     * 扣减商品库存
     */
    boolean decreaseItemStock(StockDeduction stockDeduction);

    /**
     * 增加商品库存
     */
    boolean increaseItemStock(StockDeduction stockDeduction);
}
