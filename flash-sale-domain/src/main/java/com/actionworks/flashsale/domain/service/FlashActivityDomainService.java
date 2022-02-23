package com.actionworks.flashsale.domain.service;

import com.actionworks.flashsale.domain.model.entity.FlashActivity;

/**
 * 秒杀活动实体类服务层
 */
public interface FlashActivityDomainService {

    /**
     * 发布秒杀活动
     */
    void publishActivity(FlashActivity flashActivity);

    /**
     * 上线活动
     *
     * @param activityId 活动ID
     */
    void onlineActivity(Long activityId);

    /**
     * 下线活动
     *
     * @param activityId 活动ID
     */
    void offlineActivity(Long activityId);

    /**
     * 根据ID获取单个活动
     */
    FlashActivity getFlashActivity(Long activityId);
}
