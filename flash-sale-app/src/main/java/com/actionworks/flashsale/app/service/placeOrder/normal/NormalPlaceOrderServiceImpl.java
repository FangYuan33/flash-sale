package com.actionworks.flashsale.app.service.placeOrder.normal;

import com.actionworks.flashsale.app.exception.BizException;
import com.actionworks.flashsale.app.model.command.FlashPlaceOrderCommand;
import com.actionworks.flashsale.app.model.convertor.FlashOrderAppConvertor;
import com.actionworks.flashsale.app.model.dto.FlashItemDTO;
import com.actionworks.flashsale.app.model.result.AppResult;
import com.actionworks.flashsale.app.service.activity.FlashActivityAppService;
import com.actionworks.flashsale.app.service.item.FlashItemAppService;
import com.actionworks.flashsale.app.service.placeOrder.PlaceOrderService;
import com.actionworks.flashsale.domain.model.entity.FlashOrder;
import com.actionworks.flashsale.domain.model.stock.StockDeduction;
import com.actionworks.flashsale.domain.service.FlashOrderDomainService;
import com.actionworks.flashsale.domain.service.StockDeductionDomainService;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import static com.actionworks.flashsale.app.exception.AppErrorCode.DO_PLACE_ORDER;
import static com.actionworks.flashsale.domain.model.enums.FlashOrderStatus.CREATE;

@Slf4j
@Service
public class NormalPlaceOrderServiceImpl implements PlaceOrderService {

    @Resource
    private FlashActivityAppService flashActivityAppService;
    @Resource
    private FlashItemAppService flashItemAppService;
    @Resource
    private StockDeductionDomainService stockDeductionDomainService;
    @Resource
    private FlashOrderDomainService flashOrderDomainService;

    @Override
    @Transactional
    public <T> AppResult<T> doPlaceOrder(Long userId, FlashPlaceOrderCommand command) {
        log.info("doPlaceOrder|开始下单|{}, {}", userId, JSON.toJSONString(command));

        // 校验秒杀活动、秒杀时间条件
        checkPlaceOrderCondition(command.getActivityId(), command.getItemId());

        // 扣减库存
        decreaseItemStock(command.getItemId(), command.getQuantity());

        // 初始化秒杀订单信息
        FlashOrder flashOrder = initialFlashOrderInfo(userId, command);

        // 秒杀订单入库
        flashOrderDomainService.doPlaceOrder(flashOrder);
        log.info("doPlaceOrder|下单成功");

        return AppResult.success();
    }

    /**
     * 校验秒杀活动、秒杀时间条件
     *
     * @param activityId 秒杀活动id
     * @param itemId 秒杀商品id
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
     * @param itemId 秒杀商品ID
     * @param quantity 扣减数量
     */
    private void decreaseItemStock(Long itemId, Integer quantity) {
        StockDeduction stockDeduction = new StockDeduction().setItemId(itemId).setQuantity(quantity);
        boolean success = stockDeductionDomainService.decreaseItemStock(stockDeduction);

        if (!success) {
            log.error("doPlaceOrder|扣减库存失败|{}", JSON.toJSONString(stockDeduction));
            throw new BizException(DO_PLACE_ORDER);
        }
    }

    /**
     * 初始化秒杀订单信息
     *
     * @param userId 用户ID
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
