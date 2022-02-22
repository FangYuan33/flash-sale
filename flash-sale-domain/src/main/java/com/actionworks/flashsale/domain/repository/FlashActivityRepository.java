package com.actionworks.flashsale.domain.repository;

import com.actionworks.flashsale.domain.model.entity.FlashActivity;

import java.util.Optional;

/**
 * 秒杀活动Repository
 *
 * @author fangyuan
 */
public interface FlashActivityRepository {

    /**
     * 新增秒杀活动
     */
    void save(FlashActivity flashActivity);

    /**
     * 通过ID获取活动
     * 关于Optional的博客：https://www.cnblogs.com/zhangboyu/p/7580262.html
     *
     * @param activityId 活动ID
     */
    Optional<FlashActivity> findById(Long activityId);

    /**
     * 根据ID更新活动
     *
     * @param flashActivity 指定了ID的活动实体类
     */
    void updateById(FlashActivity flashActivity);
}
