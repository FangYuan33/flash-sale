package com.actionworks.flashsale.app.service.placeOrder.impl.queued;

import com.actionworks.flashsale.app.service.placeOrder.impl.AbstractDoPlaceOrderService;
import com.actionworks.flashsale.domain.model.stock.StockDeduction;
import com.actionworks.flashsale.domain.service.StockDeductionDomainService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Slf4j
@Service
@ConditionalOnProperty(name = "placeOrder.type", havingValue = "queued")
public class QueuedDoPlaceOrderServiceImpl extends AbstractDoPlaceOrderService {

    @Resource
    private StockDeductionDomainService stockDeductionDomainService;

    @Override
    protected boolean decreaseItemStock(Long itemId, Integer quantity) {
        StockDeduction stockDeduction = new StockDeduction().setItemId(itemId).setQuantity(quantity);

        // 返回扣减数据库库存结果
        return stockDeductionDomainService.decreaseItemStock(stockDeduction);
    }
}
