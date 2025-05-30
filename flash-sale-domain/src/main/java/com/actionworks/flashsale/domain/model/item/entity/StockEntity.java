package com.actionworks.flashsale.domain.model.item.entity;

import com.actionworks.flashsale.common.model.Entity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.Setter;

@Data
@Builder
@Setter(AccessLevel.PRIVATE)
public class StockEntity implements Entity {

    private Long id;

    /**
     * 商品唯一编码
     */
    private String code;

    /**
     * 秒杀品初始库存
     */
    private Integer initialStock;

    /**
     * 秒杀品可用库存
     */
    private Integer availableStock;

}
