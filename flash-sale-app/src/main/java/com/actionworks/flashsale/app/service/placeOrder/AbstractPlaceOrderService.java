package com.actionworks.flashsale.app.service.placeOrder;

import com.actionworks.flashsale.app.exception.BizException;
import com.actionworks.flashsale.app.model.command.FlashPlaceOrderCommand;
import com.actionworks.flashsale.app.model.result.AppResult;
import com.actionworks.flashsale.app.service.activity.FlashActivityAppService;
import com.actionworks.flashsale.app.service.item.FlashItemAppService;
import com.actionworks.flashsale.cache.constants.CacheConstants;
import com.actionworks.flashsale.cache.redis.RedisCacheService;

import javax.annotation.Resource;

import static com.actionworks.flashsale.app.exception.AppErrorCode.FLASH_SALE_NOT_BEGIN;
import static com.actionworks.flashsale.app.exception.AppErrorCode.USER_ALREADY_FLASH;

/**
 * 使用模板方法模式对下单操作进行优化，尽可能对代码进行复用
 */
public abstract class AbstractPlaceOrderService implements PlaceOrderService {

    @Resource
    private FlashActivityAppService flashActivityAppService;
    @Resource
    private FlashItemAppService flashItemAppService;

    @Resource
    private RedisCacheService<Object> redisCacheService;

    @Override
    public <T> AppResult<T> doPlaceOrder(Long userId, FlashPlaceOrderCommand command) {
        String userFlashKey = String.format(CacheConstants.USER_HAS_FLASH_ITEM_PREFIX, userId, command.getItemId());

        // 校验秒杀活动和秒杀商品的秒杀条件
        checkPlaceOrderCondition(command.getActivityId(), command.getItemId());
        // 校验同一个用户重复下单
        checkUserHasFlashed(userFlashKey);

        boolean success = doSyncPlaceOrderOrPostPlaceOrderTask(userId, command);

        if (success) {
            // 同步下单成功 or 提交任务成功，添加上该用户的下单标识
            redisCacheService.setValue(userFlashKey, 1);

            return AppResult.success("成功参加秒杀活动，请在订单中查询秒杀结果");
        } else {
            return AppResult.error("库存不足，参加秒杀活动失败");
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

    /**
     * 进行同步下单 or 发送异步下单消息
     *
     * @return 执行的结果
     */
    protected abstract boolean doSyncPlaceOrderOrPostPlaceOrderTask(Long userId, FlashPlaceOrderCommand command);
}
