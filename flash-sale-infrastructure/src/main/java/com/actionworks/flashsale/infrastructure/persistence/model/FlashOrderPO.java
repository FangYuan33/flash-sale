package com.actionworks.flashsale.infrastructure.persistence.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@TableName("flash_order")
@EqualsAndHashCode(callSuper = true)
public class FlashOrderPO extends BasePO {

    /**
     * 订单唯一编码
     */
    private String code;

    /**
     * 商品唯一编码
     */
    private String itemCode;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 秒杀品名称
     */
    private String itemTitle;

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