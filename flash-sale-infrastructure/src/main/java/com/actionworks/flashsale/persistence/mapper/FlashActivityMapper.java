package com.actionworks.flashsale.persistence.mapper;

import com.actionworks.flashsale.persistence.model.FlashActivityDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface FlashActivityMapper {

    /**
     * 新增秒杀活动
     */
    int insert(FlashActivityDO flashActivityDO);

    /**
     * 通过活动ID获取活动
     *
     * @param activityId 活动ID
     */
    FlashActivityDO getById(@Param("activityId") Long activityId);

    /**
     * 通过活动ID更新
     *
     * @param flashActivityDO 包含活动ID的实体
     */
    void updateById(FlashActivityDO flashActivityDO);
}
