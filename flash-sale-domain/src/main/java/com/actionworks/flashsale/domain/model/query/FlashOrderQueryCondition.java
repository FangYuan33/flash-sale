package com.actionworks.flashsale.domain.model.query;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class FlashOrderQueryCondition extends BaseQueryCondition {
    /**
     * 商品ID
     */
    private Long itemId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 活动ID
     */
    private Long activityId;

    @Override
    public void buildParams() {
        super.buildParams();
    }
}
