package com.actionworks.flashsale.domain.repository;

import com.actionworks.flashsale.domain.model.item.aggregate.FlashItem;

import java.util.Optional;

public interface FlashItemRepository {

    /**
     * 根据ID获取秒杀商品
     */
    Optional<FlashItem> findById(Long itemId);

}
