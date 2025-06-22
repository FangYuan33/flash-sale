package com.actionworks.flashsale.domain.repository;

import com.actionworks.flashsale.domain.model.aggregate.FlashItem;

import java.util.Optional;

public interface FlashItemRepository {

    /**
     * 根据ID获取秒杀商品
     */
    Optional<FlashItem> findById(Long itemId);

    void save(FlashItem flashItem);

    FlashItem findByCode(String code);

    void modifyStatus(FlashItem flashItem);

    boolean deduct(String itemCode, Integer quantity);
}
