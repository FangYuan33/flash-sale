package com.actionworks.flashsale.application.service.item;

import com.actionworks.flashsale.application.model.command.FlashItemPublishCommand;
import com.actionworks.flashsale.application.model.result.AppResult;

public interface FlashItemAppService {
    /**
     * 发布秒杀商品
     *
     * @param activityId 秒杀活动ID
     */
    <T> AppResult<T> publishFlashItem(Long activityId, FlashItemPublishCommand command);

    /**
     * 上线秒杀商品
     *
     * @param itemId 秒杀商品ID
     */
    <T> AppResult<T> onlineFlashItem(Long itemId);

    /**
     * 下线秒杀商品
     *
     * @param itemId 秒杀商品ID
     */
    <T> AppResult<T> offlineFlashItem(Long itemId);

    /**
     * 获取秒杀商品是否允许下单
     *
     * @param itemId 商品ID
     */
    boolean isAllowPlaceOrderOrNot(Long itemId);
}
