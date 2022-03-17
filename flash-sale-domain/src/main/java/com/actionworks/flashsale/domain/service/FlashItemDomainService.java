package com.actionworks.flashsale.domain.service;

import com.actionworks.flashsale.domain.model.entity.FlashItem;
import com.actionworks.flashsale.domain.model.query.FlashItemQueryCondition;
import com.actionworks.flashsale.domain.model.query.PageResult;

import java.util.List;

public interface FlashItemDomainService {
    /**
     * 发布秒杀商品
     */
    void publishFlashItem(FlashItem flashItem);

    /**
     * 上线秒杀商品
     *
     * @param itemId 秒杀商品ID
     */
    void onlineFlashItem(Long itemId);

    /**
     * 下线秒杀商品
     *
     * @param itemId 秒杀商品ID
     */
    void offlineFlashItem(Long itemId);

    /**
     * 通过ID获取秒杀商品
     *
     * @param itemId 秒杀商品ID
     */
    FlashItem getById(Long itemId);

    /**
     * 条件查询秒杀商品
     */
    PageResult<FlashItem> listByQueryCondition(FlashItemQueryCondition queryCondition);

    /**
     * 根据ID对秒杀商品进行更新
     *
     * @param flashItem 必须包含ID信息
     */
    void updateById(FlashItem flashItem);
}
