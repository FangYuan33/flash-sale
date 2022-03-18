package com.actionworks.flashsale.persistence.mapper;

import com.actionworks.flashsale.domain.model.query.FlashItemQueryCondition;
import com.actionworks.flashsale.persistence.model.FlashItemDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FlashItemMapper extends BaseMapper<FlashItemDO> {

    /**
     * 条件查询秒杀活动
     */
    List<FlashItemDO> listByQueryCondition(FlashItemQueryCondition queryCondition);

    /**
     * 条件计数
     */
    Integer countByQueryCondition(FlashItemQueryCondition queryCondition);

    /**
     * 条件查询秒杀秒杀活动不分页
     */
    List<FlashItemDO> listByQueryConditionWithoutPageSize(FlashItemQueryCondition queryCondition);

    /**
     * 扣减库存
     *
     * @param itemId 商品ID
     * @param quantity 商品数量
     */
    int decreaseItemStock(@Param("itemId") Long itemId, @Param("quantity") Integer quantity);
}
