package com.actionworks.flashsale.app.service.placeOrder.normal;

import com.actionworks.flashsale.app.exception.BizException;
import com.actionworks.flashsale.app.model.command.FlashPlaceOrderCommand;
import com.actionworks.flashsale.app.model.convertor.FlashOrderAppConvertor;
import com.actionworks.flashsale.app.model.dto.FlashItemDTO;
import com.actionworks.flashsale.app.model.result.AppResult;
import com.actionworks.flashsale.app.service.activity.FlashActivityAppService;
import com.actionworks.flashsale.app.service.item.FlashItemAppService;
import com.actionworks.flashsale.app.service.placeOrder.PlaceOrderService;
import com.actionworks.flashsale.cache.ItemStockCacheService;
import com.actionworks.flashsale.domain.model.entity.FlashOrder;
import com.actionworks.flashsale.domain.model.stock.StockDeduction;
import com.actionworks.flashsale.domain.service.FlashOrderDomainService;
import com.actionworks.flashsale.domain.service.StockDeductionDomainService;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import static com.actionworks.flashsale.app.exception.AppErrorCode.DO_PLACE_ORDER;
import static com.actionworks.flashsale.domain.model.enums.FlashOrderStatus.CREATE;

@Slf4j
@Service
@ConditionalOnProperty(name = "place_order_type", havingValue = "normal")
public class NormalPlaceOrderServiceImpl implements PlaceOrderService {

    @Resource
    private FlashActivityAppService flashActivityAppService;
    @Resource
    private FlashItemAppService flashItemAppService;
    @Resource
    private StockDeductionDomainService stockDeductionDomainService;
    @Resource
    private FlashOrderDomainService flashOrderDomainService;
    @Resource
    private ItemStockCacheService itemStockCacheService;

    @Override
    public <T> AppResult<T> doPlaceOrder(Long userId, FlashPlaceOrderCommand command) {
        log.info("doPlaceOrder|开始下单|{}, {}", userId, JSON.toJSONString(command));

        // 校验秒杀活动、秒杀时间条件
        checkPlaceOrderCondition(command.getActivityId(), command.getItemId());

        // 扣减库存
        boolean decreaseSuccess = decreaseItemStock(command.getItemId(), command.getQuantity());

        // 库存扣减成功，下订单
        if (decreaseSuccess) {
            createFlashOrder(userId, command);

            return AppResult.success();
        } else {
            throw new BizException(DO_PLACE_ORDER);
        }
    }

    /**
     * 校验秒杀活动、秒杀时间条件
     *
     * @param activityId 秒杀活动id
     * @param itemId     秒杀商品id
     */
    private void checkPlaceOrderCondition(Long activityId, Long itemId) {
        boolean activityAllow = flashActivityAppService.isAllowPlaceOrderOrNot(activityId);
        boolean itemAllow = flashItemAppService.isAllowPlaceOrderOrNot(itemId);

        if (!activityAllow || !itemAllow) {
            throw new BizException(DO_PLACE_ORDER);
        }
    }

    /**
     * 扣减库存
     *
     * @param itemId   秒杀商品ID
     * @param quantity 扣减数量
     */
    private boolean decreaseItemStock(Long itemId, Integer quantity) {
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

    /**
     * 下单
     */
    private void createFlashOrder(Long userId, FlashPlaceOrderCommand command) {
        try {
            // 初始化秒杀订单信息
            FlashOrder flashOrder = initialFlashOrderInfo(userId, command);

            // 秒杀订单入库
            flashOrderDomainService.doPlaceOrder(flashOrder);
            log.info("doPlaceOrder|下单成功");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            // 下单失败恢复缓存和数据库库存
            itemStockCacheService.increaseItemStock(command.getItemId(), command.getQuantity());

            StockDeduction stockDeduction =
                    new StockDeduction().setItemId(command.getItemId()).setQuantity(command.getQuantity());
            stockDeductionDomainService.increaseItemStock(stockDeduction);
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
