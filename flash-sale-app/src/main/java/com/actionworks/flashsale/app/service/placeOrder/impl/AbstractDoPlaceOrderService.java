package com.actionworks.flashsale.app.service.placeOrder.impl;

import com.actionworks.flashsale.app.model.command.FlashPlaceOrderCommand;
import com.actionworks.flashsale.app.model.convertor.FlashOrderAppConvertor;
import com.actionworks.flashsale.app.model.dto.FlashItemDTO;
import com.actionworks.flashsale.app.service.item.FlashItemAppService;
import com.actionworks.flashsale.app.service.placeOrder.DoPlaceOrderService;
import com.actionworks.flashsale.cache.ItemStockCacheService;
import com.actionworks.flashsale.domain.model.entity.FlashOrder;
import com.actionworks.flashsale.domain.model.stock.StockDeduction;
import com.actionworks.flashsale.domain.service.FlashOrderDomainService;
import com.actionworks.flashsale.domain.service.StockDeductionDomainService;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;

import static com.actionworks.flashsale.domain.model.enums.FlashOrderStatus.CREATE;

@Slf4j
public abstract class AbstractDoPlaceOrderService implements DoPlaceOrderService {

    @Resource
    private FlashItemAppService flashItemAppService;
    @Resource
    private StockDeductionDomainService stockDeductionDomainService;
    @Resource
    private FlashOrderDomainService flashOrderDomainService;
    @Resource
    private ItemStockCacheService itemStockCacheService;

    /**
     * 扣减库存 生成订单的执行逻辑
     */
    @Override
    public boolean doPlaceOrder(Long userId, FlashPlaceOrderCommand command) {
        // 扣减库存
        boolean decreaseSuccess = decreaseItemStock(command.getItemId(), command.getQuantity());

        if (decreaseSuccess) {
            return createFlashOrder(userId, command);
        } else {
            return false;
        }
    }

    /**
     * 扣减库存 同步和异步有不同的实现，所以把这里也做了抽象
     * 同步则是需要对缓存库存和数据库库存一起扣减
     * 异步扣减的仅仅是数据库库存
     *
     * @param itemId   秒杀商品ID
     * @param quantity 扣减数量
     */
    protected abstract boolean decreaseItemStock(Long itemId, Integer quantity);

    /**
     * 下单
     */
    private boolean createFlashOrder(Long userId, FlashPlaceOrderCommand command) {
        try {
            // 初始化秒杀订单信息
            FlashOrder flashOrder = initialFlashOrderInfo(userId, command);

            // 秒杀订单入库
            flashOrderDomainService.doPlaceOrder(flashOrder);
            log.info("下单成功");

            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            // 下单失败恢复缓存和数据库库存
            itemStockCacheService.increaseItemStock(command.getItemId(), command.getQuantity());

            StockDeduction stockDeduction =
                    new StockDeduction().setItemId(command.getItemId()).setQuantity(command.getQuantity());
            stockDeductionDomainService.increaseItemStock(stockDeduction);

            return false;
        }
    }

    /**
     * 初始化秒杀订单信息
     *
     * @param userId  用户ID
     * @param command 包含订单中必要信息
     */
    private FlashOrder initialFlashOrderInfo(Long userId, FlashPlaceOrderCommand command) {
        FlashItemDTO flashItem = (FlashItemDTO) flashItemAppService.getById(command.getItemId()).getData();
        FlashOrder flashOrder = FlashOrderAppConvertor.toDomain(command);
        Long totalAmount = flashItem.getFlashPrice() * flashOrder.getQuantity();

        flashOrder.setUserId(userId).setItemTitle(flashItem.getItemTitle()).setFlashPrice(flashItem.getFlashPrice())
                .setTotalAmount(totalAmount).setStatus(CREATE.getCode());

        return flashOrder;
    }
}
