package com.actionworks.flashsale.domain.repository;

import com.actionworks.flashsale.domain.model.entity.FlashOrder;

public interface FlashOrderRepository {

    /**
     * 秒杀订单入库
     */
    void save(FlashOrder flashOrder);
}
