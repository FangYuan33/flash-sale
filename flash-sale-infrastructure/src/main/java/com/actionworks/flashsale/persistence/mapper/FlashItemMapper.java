package com.actionworks.flashsale.persistence.mapper;

import com.actionworks.flashsale.domain.model.query.FlashItemQueryCondition;
import com.actionworks.flashsale.persistence.model.FlashItemDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

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
    int countByQueryCondition(FlashItemQueryCondition queryCondition);
}
