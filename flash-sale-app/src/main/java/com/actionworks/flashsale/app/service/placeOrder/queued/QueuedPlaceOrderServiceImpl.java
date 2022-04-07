package com.actionworks.flashsale.app.service.placeOrder.queued;

import com.actionworks.flashsale.app.exception.BizException;
import com.actionworks.flashsale.app.model.command.FlashPlaceOrderCommand;
import com.actionworks.flashsale.app.model.result.AppResult;
import com.actionworks.flashsale.app.service.activity.FlashActivityAppService;
import com.actionworks.flashsale.app.service.item.FlashItemAppService;
import com.actionworks.flashsale.app.service.placeOrder.PlaceOrderService;
import com.actionworks.flashsale.cache.ItemPermissionCacheService;
import com.actionworks.flashsale.cache.constants.CacheConstants;
import com.actionworks.flashsale.cache.redis.RedisCacheService;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import static com.actionworks.flashsale.app.exception.AppErrorCode.FLASH_SALE_NOT_BEGIN;
import static com.actionworks.flashsale.app.exception.AppErrorCode.USER_ALREADY_FLASH;

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
    private ItemPermissionCacheService itemPermissionCacheService;
    @Resource
    private RedisCacheService<Object> redisCacheService;

    @Override
    public <T> AppResult<T> doPlaceOrder(Long userId, FlashPlaceOrderCommand command) {
        log.info("开始异步下单, userId {}, {}", userId, JSON.toJSONString(command));
        String userFlashKey = String.format(CacheConstants.USER_HAS_FLASH_ITEM_PREFIX, userId, command.getItemId());

        // 校验秒杀活动和秒杀商品的秒杀条件
        checkPlaceOrderCondition(command.getActivityId(), command.getItemId());

        // 校验同一个用户重复下单
        checkUserHasFlashed(userFlashKey);

        // 扣减下单许可
        boolean decreaseSuccess = itemPermissionCacheService.decreaseItemPermission(command.getItemId());

        if (decreaseSuccess) {
            // 扣减下单许可成功，提交任务

            // 提交任务成功，添加上该用户的下单标识
            redisCacheService.setValue(userFlashKey, 1);

            return AppResult.success("成功参加秒杀活动，请稍后查询秒杀结果");
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
            throw new BizException(FLASH_SALE_NOT_BEGIN);
        }
    }

    /**
     * 校验用户重复秒杀
     */
    private void checkUserHasFlashed(String userFlashKey) {
        boolean userAlreadyFlash = redisCacheService.hasKey(userFlashKey);
        if (userAlreadyFlash) {
            throw new BizException(USER_ALREADY_FLASH);
        }
    }
}
