package com.actionworks.flashsale.domain.repository;

import com.actionworks.flashsale.domain.model.aggregate.FlashOrder;
import java.util.Optional;

public interface FlashOrderRepository {

    // 保存订单
    void save(FlashOrder flashOrder);

    // 根据订单唯一编码查询订单
    Optional<FlashOrder> findByCode(String code);
}

