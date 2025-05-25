package com.actionworks.flashsale.trigger.model.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
public class FlashItemQueryRequest extends PageQueryRequest {

    /**
     * 秒杀品名称标题
     */
    private String itemTitle;

    /**
     * 秒杀商品状态 10-已发布 20-已上线 30-已下线
     */
    private Integer status;
}
