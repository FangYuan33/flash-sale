package com.actionworks.flashsale.app.service.placeOrder.impl.normal;

import com.actionworks.flashsale.app.service.placeOrder.impl.AbstractDoPlaceOrderService;
import com.actionworks.flashsale.cache.ItemStockCacheService;
import com.actionworks.flashsale.domain.model.stock.StockDeduction;
import com.actionworks.flashsale.domain.service.StockDeductionDomainService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Slf4j
@Service
@ConditionalOnProperty(name = "placeOrder.type", havingValue = "normal")
public class NormalDoPlaceOrderServiceImpl extends AbstractDoPlaceOrderService {

    @Resource
    private ItemStockCacheService itemStockCacheService;
    @Resource
    private StockDeductionDomainService stockDeductionDomainService;

    @Override
    protected boolean decreaseItemStock(Long itemId, Integer quantity) {
        boolean cacheSuccess = false;
        boolean dbSuccess = false;
        StockDeduction stockDeduction = new StockDeduction().setItemId(itemId).setQuantity(quantity);

        try {
            // 预扣减缓存库存
            cacheSuccess = itemStockCacheService.decreaseItemStock(itemId, quantity);
            if (!cacheSuccess) {
                return false;
            }

            // 扣减数据库
            dbSuccess = stockDeductionDomainService.decreaseItemStock(stockDeduction);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            // 缓存扣减成功但是出现了异常，需要将缓存库存恢复
            if (cacheSuccess) {
                itemStockCacheService.increaseItemStock(itemId, quantity);
            }
        }

        return cacheSuccess && dbSuccess;
    }
}
