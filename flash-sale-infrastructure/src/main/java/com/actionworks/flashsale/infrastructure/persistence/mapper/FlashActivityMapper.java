package com.actionworks.flashsale.infrastructure.persistence.mapper;

import com.actionworks.flashsale.domain.model.query.FlashActivityQueryCondition;
import com.actionworks.flashsale.infrastructure.persistence.model.FlashActivityDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FlashActivityMapper extends BaseMapper<FlashActivityDO> {

    /**
     * 条件查询秒杀活动
     */
    List<FlashActivityDO> listByQueryCondition(FlashActivityQueryCondition condition);

    /**
     * 条件查询计数
     */
    Integer countByQueryCondition(FlashActivityQueryCondition queryCondition);
}
