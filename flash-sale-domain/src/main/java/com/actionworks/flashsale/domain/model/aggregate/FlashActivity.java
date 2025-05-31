package com.actionworks.flashsale.domain.model.aggregate;

import com.actionworks.flashsale.common.exception.DomainException;
import com.actionworks.flashsale.common.model.AggregateRoot;
import com.actionworks.flashsale.domain.adapter.CodeGenerateService;
import com.actionworks.flashsale.domain.model.enums.FlashActivityStatus;
import com.actionworks.flashsale.domain.model.enums.FlashItemStatus;
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

    private String code;

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

    public void publish(CodeGenerateService codeGenerateService) {
        this.status = FlashActivityStatus.PUBLISHED;
        this.code = codeGenerateService.generateCode();
    }

    public void changeStatus(FlashActivityStatus newStatus) {
        // 发布只能上线
        if (this.status.equals(FlashActivityStatus.PUBLISHED) && FlashActivityStatus.ONLINE.equals(newStatus)) {
            this.status = FlashActivityStatus.ONLINE;
        }
        // 下线也能上线
        else if (this.status.equals(FlashActivityStatus.OFFLINE) && FlashActivityStatus.ONLINE.equals(newStatus)) {
            this.status = FlashActivityStatus.ONLINE;
        }
        // 上线能上线，联动商品下线
        else if (this.status.equals(FlashActivityStatus.ONLINE) && FlashActivityStatus.OFFLINE.equals(newStatus)) {
            this.status = FlashActivityStatus.OFFLINE;
            this.flashItem.changeStatus(FlashItemStatus.OFFLINE);
        } else {
            throw new DomainException("[变更活动状态] 状态异常 source: " + this.status + " target: " + status);
        }
    }
}
