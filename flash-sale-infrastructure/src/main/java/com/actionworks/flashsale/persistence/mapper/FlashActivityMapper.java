package com.actionworks.flashsale.persistence.mapper;

import com.actionworks.flashsale.domain.model.query.FlashActivityQueryCondition;
import com.actionworks.flashsale.persistence.model.FlashActivityDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

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

    /**
     * 条件查询秒杀活动
     */
    List<FlashActivityDO> getByQueryCondition(FlashActivityQueryCondition condition);

    /**
     * 条件查询计数
     */
    int countByQueryCondition(FlashActivityQueryCondition queryCondition);
}
