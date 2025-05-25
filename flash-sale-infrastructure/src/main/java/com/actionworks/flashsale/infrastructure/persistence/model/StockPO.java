package com.actionworks.flashsale.infrastructure.persistence.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@TableName("stock")
@EqualsAndHashCode(callSuper = true)
public class StockPO extends BasePO {

    /**
     * 商品唯一编码
     */
    private String code;

    private Integer initialStock;

    private Integer availableStock;

}
