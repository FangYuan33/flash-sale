package com.actionworks.flashsale.domain.repository;

import com.actionworks.flashsale.domain.model.item.aggregate.FlashItem;
import com.actionworks.flashsale.domain.model.query.FlashItemQueryCondition;

import java.util.List;
import java.util.Optional;

public interface FlashItemRepository {

    /**
     * 根据ID获取秒杀商品
     */
    Optional<FlashItem> findById(Long itemId);

    List<FlashItem> findByCondition(FlashItemQueryCondition condition);
}
