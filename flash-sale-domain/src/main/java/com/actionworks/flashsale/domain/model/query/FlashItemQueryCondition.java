package com.actionworks.flashsale.domain.model.query;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class FlashItemQueryCondition extends BaseQueryCondition {

    public FlashItemQueryCondition(Long itemId) {
        this.itemId = itemId;
    }

    /**
     * 秒杀商品ID
     */
    private Long itemId;

    /**
     * 秒杀活动ID
     */
    private Long activityId;

    /**
     * 秒杀品名称标题
     */
    private String itemTitle;

    /**
     * 秒杀品副标题
     */
    private String itemSubTitle;

    /**
     * 秒杀开始时间
     */
    private LocalDateTime startTime;

    /**
     * 秒杀结束时间
     */
    private LocalDateTime endTime;

    /**
     * 秒杀商品状态 10-已发布 20-已上线 30-已下线
     */
    private Integer status;

    /**
     * 库存预热情况 0-未预热 1-已预热
     */
    private Integer warmUp;

    @Override
    public void buildParams() {
        super.buildParams();
    }
}
