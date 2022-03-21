package com.actionworks.flashsale.app.service.order;

import com.actionworks.flashsale.app.exception.BizException;
import com.actionworks.flashsale.app.model.command.FlashPlaceOrderCommand;
import com.actionworks.flashsale.app.model.convertor.FlashOrderAppConvertor;
import com.actionworks.flashsale.app.model.dto.FlashOrderDTO;
import com.actionworks.flashsale.app.model.query.FlashOrderQuery;
import com.actionworks.flashsale.app.model.result.AppResult;
import com.actionworks.flashsale.app.service.placeOrder.PlaceOrderService;
import com.actionworks.flashsale.cache.ItemStockCacheService;
import com.actionworks.flashsale.domain.model.entity.FlashOrder;
import com.actionworks.flashsale.domain.model.query.FlashOrderQueryCondition;
import com.actionworks.flashsale.domain.model.query.PageResult;
import com.actionworks.flashsale.domain.model.stock.StockDeduction;
import com.actionworks.flashsale.domain.service.FlashOrderDomainService;
import com.actionworks.flashsale.domain.service.StockDeductionDomainService;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import java.util.List;
import java.util.stream.Collectors;

import static com.actionworks.flashsale.app.exception.AppErrorCode.INVALID_PARAMS;
import static com.actionworks.flashsale.app.exception.AppErrorCode.RECOVER_STOCK_FAILED;

@Slf4j
@Service
public class DefaultFlashOrderAppService implements FlashOrderAppService {

    @Resource
    private FlashOrderDomainService flashOrderDomainService;
    @Resource
    private PlaceOrderService placeOrderService;
    @Resource
    private StockDeductionDomainService stockDeductionDomainService;
    @Resource
    private ItemStockCacheService itemStockCacheService;

    @Override
    public <T> AppResult<T> placeOrder(Long userId, FlashPlaceOrderCommand command) {
        log.info("placeOrder|下单|{}, {}", userId, JSON.toJSONString(command));

        if (userId == null || command == null) {
            throw new BizException(INVALID_PARAMS);
        }

        return placeOrderService.doPlaceOrder(userId, command);
    }

    @Override
    @Transactional
    public <T> AppResult<T> cancelOrder(Long orderId) {
        log.info("cancelOrder|取消秒杀订单|{}", orderId);

        if (orderId == null) {
            throw new BizException(INVALID_PARAMS);
        }

        boolean success = flashOrderDomainService.cancelOrder(orderId);

        if (success) {
            log.info("cancelOrder|取消秒杀订单|成功");

            recoverStockNum(orderId);
        } else {
            return AppResult.error("订单取消失败");
        }

        return AppResult.success();
    }

    /**
     * 恢复商品库存 缓存and数据库
     *
     * @param orderId 订单ID
     */
    private void recoverStockNum(Long orderId) {
        try {
            FlashOrder flashOrder = flashOrderDomainService.getById(orderId);

            itemStockCacheService.increaseItemStock(flashOrder.getItemId(), flashOrder.getQuantity());

            StockDeduction stockDeduction =
                    new StockDeduction().setItemId(flashOrder.getItemId()).setQuantity(flashOrder.getQuantity());
            stockDeductionDomainService.increaseItemStock(stockDeduction);
        } catch (Exception e) {
            log.error(e.getMessage(), e);

            throw new BizException(RECOVER_STOCK_FAILED);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public AppResult<FlashOrderDTO> getFlashOrder(Long orderId) {
        FlashOrder flashOrder = flashOrderDomainService.getById(orderId);

        FlashOrderDTO flashOrderDTO = FlashOrderAppConvertor.toFlashOrderDTO(flashOrder);

        return AppResult.success(flashOrderDTO);
    }

    @Override
    @SuppressWarnings("unchecked")
    public AppResult<List<FlashOrderDTO>> getFlashOrders(FlashOrderQuery flashOrderQuery) {
        FlashOrderQueryCondition queryCondition = FlashOrderAppConvertor.toQueryCondition(flashOrderQuery);
        PageResult<FlashOrder> flashOrderPageResult = flashOrderDomainService.listByQueryCondition(queryCondition);

        // stream 转换出参类型
        List<FlashOrderDTO> flashOrderDTOS = flashOrderPageResult.getData().stream()
                .map(FlashOrderAppConvertor::toFlashOrderDTO).collect(Collectors.toList());

        return AppResult.success(flashOrderDTOS);
    }
}
