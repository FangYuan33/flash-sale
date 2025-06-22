package com.actionworks.flashsale.application.query.model.req;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class FlashItemQuery extends PageQuery {

    /**
     * 品唯一编码
     */
    private String code;

    /**
     * 秒杀品名称标题
     */
    private String itemTitle;

    /**
     * 秒杀商品状态 10-已发布 20-已上线 30-已下线
     */
    private Integer status;
}
