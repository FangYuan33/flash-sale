package com.actionworks.flashsale.infrastructure.persistence.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@TableName("flash_order")
@EqualsAndHashCode(callSuper = true)
public class FlashOrderDO extends BaseDO {

    /**
     * 秒杀品ID
     */
    private Long itemId;

    /**
     * 秒杀活动ID
     */
    private Long activityId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 秒杀品名称
     */
    private String itemTitle;

    /**
     * 秒杀价
     */
    private Long flashPrice;

    /**
     * 数量
     */
    private Integer quantity;

    /**
     * 总价格
     */
    private Long totalAmount;

    /**
     * 订单状态 10-已创建 20-已取消
     */
    private Integer status;
}
