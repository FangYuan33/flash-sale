package com.actionworks.flashsale.domain.model.entity;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 秒杀活动实体类
 *
 * @author fangyuan
 */
@Data
@Accessors(chain = true)
public class FlashActivity implements Serializable {

    /**
     * 活动ID
     */
    private Long id;

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
     * 活动状态
     */
    private Integer status;

    /**
     * 校验创建秒杀活动的参数
     * 活动名、开始时间、结束时间非空
     * 结束时间在开始之间之后、结束时间大于当前时间
     */
    public boolean validateParamsForCreate() {
        return !StringUtils.isEmpty(activityName) && startTime != null && endTime != null
                && !endTime.isBefore(startTime) && !endTime.isBefore(LocalDateTime.now());
    }
}
