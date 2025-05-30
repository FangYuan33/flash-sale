package com.actionworks.flashsale.domain.model.activity.aggregate;

import com.actionworks.flashsale.domain.model.activity.enums.FlashActivityStatus;
import com.actionworks.flashsale.domain.model.item.aggregate.FlashItem;
import com.actionworks.flashsale.common.model.AggregateRoot;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 秒杀活动实体类
 *
 * @author fangyuan
 */
@Data
@Builder
@Setter(AccessLevel.PRIVATE)
public class FlashActivity implements AggregateRoot, Serializable {

    private static final long serialVersionUID = 5230421L;

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
     * 秒杀品
     */
    private FlashItem flashItem;

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
    private FlashActivityStatus status;

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
