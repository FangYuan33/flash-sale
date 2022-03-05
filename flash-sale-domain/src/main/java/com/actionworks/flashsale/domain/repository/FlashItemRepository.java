package com.actionworks.flashsale.domain.repository;

import com.actionworks.flashsale.domain.model.entity.FlashItem;
import com.actionworks.flashsale.domain.model.query.FlashItemQueryCondition;

import java.util.List;
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

    /**
     * 条件查询秒杀商品
     */
    Optional<List<FlashItem>> listByQueryCondition(FlashItemQueryCondition queryCondition);

    /**
     * 条件计数
     */
    int countByQueryCondition(FlashItemQueryCondition queryCondition);

    /**
     * 扣减商品库存
     *
     * @param itemId 商品ID
     * @param quantity 商品数量
     */
    boolean decreaseItemStock(Long itemId, Integer quantity);
}
