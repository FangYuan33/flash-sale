package com.actionworks.flashsale.domain.repository;

import com.actionworks.flashsale.domain.model.entity.FlashItem;

public interface FlashItemRepository {

    /**
     * 秒杀活动保存
     */
    void save(FlashItem flashItem);
}
