package com.actionworks.flashsale.domain.repository;

import com.actionworks.flashsale.domain.model.entity.FlashActivity;

/**
 * 秒杀活动Repository
 *
 * @author fangyuan
 */
public interface FlashActivityRepository {

    /**
     * 新增秒杀活动
     */
    int save(FlashActivity flashActivity);
}
