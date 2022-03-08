package com.actionworks.flashsale.app.service.activity;

import com.actionworks.flashsale.app.model.command.FlashActivityPublishCommand;
import com.actionworks.flashsale.app.model.query.FlashActivitiesQuery;
import com.actionworks.flashsale.app.model.result.AppResult;

import java.util.List;

/**
 * 秒杀活动Service
 *
 * @author fangyuan
 */
public interface FlashActivityAppService {

    /**
     * 发布秒杀活动
     */
    <T> AppResult<T> publishFlashActivity(FlashActivityPublishCommand activityPublishCommand);

    /**
     * 上线秒杀活动
     *
     * @param activityId 秒杀活动ID
     */
    <T> AppResult<T> onlineFlashActivity(Long activityId);

    /**
     * 下线秒杀活动
     *
     * @param activityId 秒杀活动ID
     */
    <T> AppResult<T> offlineFlashActivity(Long activityId);

    /**
     * 根据ID获取秒杀活动
     */
    <T> AppResult<T> getFlashActivity(Long activityId);

    /**
     * 根据条件获取秒杀活动
     */
    <T> AppResult<List<T>> getFlashActivities(FlashActivitiesQuery flashActivitiesQuery);

    /**
     * 获取是否允许下单的条件
     *
     * @param activityId 活动ID
     */
    boolean isAllowPlaceOrderOrNot(Long activityId);
}