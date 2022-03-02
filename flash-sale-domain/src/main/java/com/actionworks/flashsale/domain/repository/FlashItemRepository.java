package com.actionworks.flashsale.domain.repository;

import com.actionworks.flashsale.domain.model.entity.FlashItem;

import java.util.Optional;

public interface FlashItemRepository {

    /**
     * 秒杀活动保存
     */
    void save(FlashItem flashItem);

    /**
     * 根据ID获取秒杀商品
     */
    Optional<FlashItem> getById(Long itemId);

    /**
     * 根据对象中的ID值进行更新
     *
     * @param flashItem 必须包含商品ID
     */
    void updateById(FlashItem flashItem);
}
