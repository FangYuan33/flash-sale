package com.actionworks.flashsale.domain.model.stock;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class StockDeduction {

    private Long itemId;

    private Integer quantity;
}
