package com.actionworks.flashsale.persistence.mapper;

import com.actionworks.flashsale.persistence.model.FlashActivityDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FlashActivityMapper {

    /**
     * 新增秒杀活动
     */
    int insert(FlashActivityDO flashActivityDO);
}
