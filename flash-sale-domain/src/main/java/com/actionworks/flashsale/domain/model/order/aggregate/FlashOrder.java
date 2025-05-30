package com.actionworks.flashsale.domain.model.order.aggregate;

import com.actionworks.flashsale.domain.model.order.enums.FlashOrderStatus;
import com.actionworks.flashsale.domain.model.order.valobject.Address;
import com.actionworks.flashsale.domain.model.order.valobject.ItemInfo;
import com.actionworks.flashsale.common.model.AggregateRoot;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.Setter;

import java.io.Serializable;

@Data
@Builder
@Setter(AccessLevel.PRIVATE)
public class FlashOrder implements AggregateRoot, Serializable {

    private static final long serialVersionUID = 5230401L;

    /**
     * 秒杀订单ID
     */
    private Long id;

    /**
     * 订单编码
     */
    private String code;

    /**
     * 唯一键 code_itemCode
     */
    private String uuid;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 品信息
     */
    private ItemInfo itemInfo;

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
    private FlashOrderStatus status;

    /**
     * 地址
     */
    private Address address;

}
