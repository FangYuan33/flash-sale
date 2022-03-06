package com.actionworks.flashsale.domain.repository;

import com.actionworks.flashsale.domain.model.entity.FlashOrder;

import java.util.Optional;

public interface FlashOrderRepository {

    /**
     * 秒杀订单入库
     */
    void save(FlashOrder flashOrder);

    /**
     * 根据订单id获取订单信息
     */
    Optional<FlashOrder> getById(Long orderId);

    /**
     * 根据ID修改订单信息
     *
     * @param flashOrder 必须包含ID信息
     * @throws RuntimeException 否则抛出RepositoryException异常信息
     */
    void updateById(FlashOrder flashOrder);
}
