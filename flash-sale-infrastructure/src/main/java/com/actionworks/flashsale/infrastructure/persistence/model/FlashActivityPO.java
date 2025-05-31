package com.actionworks.flashsale.infrastructure.persistence.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@TableName("flash_activity")
@EqualsAndHashCode(callSuper = true)
public class FlashActivityPO extends BasePO {

    /**
     * 活动唯一编码
     */
    private String code;

    /**
     * 秒杀活动名称
     */
    private String activityName;

    /**
     * 秒杀活动描述
     */
    private String activityDesc;

    /**
     * 秒杀品ID
     */
    private String itemCode;

    /**
     * 秒杀活动开始时间
     */
    private LocalDateTime startTime;

    /**
     * 秒杀活动结束时间
     */
    private LocalDateTime endTime;

    /**
     * 秒杀活动状态 10-已发布 20-已上线 30-已下线
     */
    private Integer status;
}
