package com.actionworks.flashsale.domain.model.order.valobject;

import lombok.Value;

/**
 * 值对象 - 品信息
 */
@Value
public class ItemInfo {

    /**
     * 品编码
     */
    String itemCode;

    /**
     * 品标题
     */
    String itemTitle;

}
