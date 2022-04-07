package com.actionworks.flashsale.app.service.placeOrder.queued;

import com.actionworks.flashsale.app.exception.BizException;
import com.actionworks.flashsale.app.model.command.FlashPlaceOrderCommand;
import com.actionworks.flashsale.app.model.result.AppResult;
import com.actionworks.flashsale.app.service.activity.FlashActivityAppService;
import com.actionworks.flashsale.app.service.item.FlashItemAppService;
import com.actionworks.flashsale.app.service.placeOrder.PlaceOrderService;
import com.actionworks.flashsale.cache.ItemStockCacheService;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import static com.actionworks.flashsale.app.exception.AppErrorCode.DO_PLACE_ORDER;

/**
 * 异步下单服务层实现
 *
 * @author fangyuan
 */
@Slf4j
@Service
@ConditionalOnProperty(name = "placeOrder.type", havingValue = "queued")
public class QueuedPlaceOrderServiceImpl implements PlaceOrderService {

    @Resource
    private FlashActivityAppService flashActivityAppService;
    @Resource
    private FlashItemAppService flashItemAppService;
    @Resource
    private ItemStockCacheService permissionCacheService;

    @Override
    public <T> AppResult<T> doPlaceOrder(Long userId, FlashPlaceOrderCommand command) {
        log.info("开始异步下单, userId {}, {}", userId, JSON.toJSONString(command));

        // 校验秒杀活动和秒杀商品的秒杀条件
        checkPlaceOrderCondition(command.getActivityId(), command.getItemId());

        // 校验同一个用户重复下单

        // 扣减下单许可
        boolean decreaseSuccess = permissionCacheService.decreaseItemAvailablePermission(command.getItemId());

        if (decreaseSuccess) {
            // 扣减下单许可成功，提交任务

            // 提交任务成功，添加上该用户的下单标识

            return null;
        } else {
            return AppResult.error("库存不足");
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
}
