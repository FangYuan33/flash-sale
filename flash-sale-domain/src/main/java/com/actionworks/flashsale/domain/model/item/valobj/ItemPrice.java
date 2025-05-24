package com.actionworks.flashsale.domain.model.item.valobj;

import com.actionworks.flashsale.common.model.ValueObject;
import lombok.Value;

@Value
public class ItemPrice implements ValueObject {

    /**
     * 秒杀品原价
     */
    Long originalPrice;

    /**
     * 秒杀价
     */
    Long flashPrice;

}
