package com.actionworks.flashsale.domain.service;

import com.actionworks.flashsale.domain.model.entity.FlashItem;

public interface FlashItemDomainService {
    /**
     * 发布秒杀商品
     */
    void publishFlashItem(FlashItem flashItem);
}
