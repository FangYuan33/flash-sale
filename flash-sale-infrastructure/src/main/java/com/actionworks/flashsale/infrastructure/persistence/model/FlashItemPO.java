package com.actionworks.flashsale.infrastructure.persistence.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@TableName("flash_item")
@EqualsAndHashCode(callSuper = true)
public class FlashItemPO extends BasePO {

    /**
     * 品唯一编码
     */
    private String code;

    /**
     * 秒杀品名称标题
     */
    private String itemTitle;

    /**
     * 秒杀品介绍
     */
    private String itemDesc;

    /**
     * 秒杀品原价
     */
    private Long originalPrice;

    /**
     * 秒杀价
     */
    private Long flashPrice;

    /**
     * 秒杀商品状态 10-已发布 20-已上线 30-已下线
     */
    private Integer status;

}
