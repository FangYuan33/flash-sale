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
}
