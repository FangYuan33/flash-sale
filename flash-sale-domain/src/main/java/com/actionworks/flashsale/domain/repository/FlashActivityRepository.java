package com.actionworks.flashsale.domain.repository;

import com.actionworks.flashsale.domain.model.aggregate.FlashActivity;

import java.util.Optional;

public interface FlashActivityRepository {

    /**
     * 根据ID获取秒杀活动
     */
    Optional<FlashActivity> findById(Long activityId);

    /**
     * 保存秒杀活动
     */
    void save(FlashActivity flashActivity);

    /**
     * 修改秒杀活动状态
     */
    void modifyStatus(FlashActivity flashActivity);

    Optional<FlashActivity> findByCode(String code);
}
