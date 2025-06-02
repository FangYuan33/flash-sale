package com.actionworks.flashsale.domain.model.entity;

import com.actionworks.flashsale.common.exception.DomainException;
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

    // 关联品编码
    public void relateCode(String code) {
        this.code = code;
    }

    public void deduct(Integer quantity) {
        if (availableStock - quantity < 0) {
            throw new DomainException("[扣减库存] 库存不足, 商品编码: " + code + ", 可用库存: " + availableStock + ", 扣减数量: " + quantity);
        }
        this.availableStock -= quantity;
    }
}
