package com.actionworks.flashsale.persistence.mapper;

import com.actionworks.flashsale.domain.model.query.FlashOrderQueryCondition;
import com.actionworks.flashsale.persistence.model.FlashOrderDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FlashOrderMapper extends BaseMapper<FlashOrderDO> {

    /**
     * 条件查询秒杀订单
     */
    List<FlashOrderDO> listByQueryCondition(FlashOrderQueryCondition queryCondition);

    /**
     * 条件计数
     */
    Integer countByQueryCondition(FlashOrderQueryCondition queryCondition);
}
