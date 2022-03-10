package com.actionworks.flashsale.domain.model.query;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * 秒杀活动查询条件
 */
@Data
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class FlashActivityQueryCondition extends BaseQueryCondition {

    public FlashActivityQueryCondition(Long activityId) {
        this.activityId = activityId;
    }

    /**
     * 秒杀活动ID
     */
    private Long activityId;

    /**
     * 活动名称
     */
    private String activityName;

    /**
     * 活动描述
     */
    private String activityDesc;

    /**
     * 开始时间
     */
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    private LocalDateTime endTime;

    /**
     * 状态条件
     */
    private Integer status;

    @Override
    public void buildParams() {
        super.buildParams();
    }
}
