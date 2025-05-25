package com.actionworks.flashsale.domain.model.item.aggregate;

import com.actionworks.flashsale.common.exception.DomainException;
import com.actionworks.flashsale.common.model.AggregateRoot;
import com.actionworks.flashsale.domain.adapter.ItemCodeGenerateService;
import com.actionworks.flashsale.domain.model.item.entity.StockEntity;
import com.actionworks.flashsale.domain.model.item.enums.FlashItemStatus;
import com.actionworks.flashsale.domain.model.item.valobj.ItemPrice;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.Setter;

import java.io.Serializable;

@Data
@Builder
@Setter(AccessLevel.PRIVATE)
public class FlashItem implements AggregateRoot, Serializable {

    private static final long serialVersionUID = 5230411L;

    /**
     * 秒杀品ID
     */
    private Long id;

    /**
     * 商品唯一编码
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
     * 库存
     */
    private StockEntity stock;

    /**
     * 品的价格
     */
    private ItemPrice itemPrice;

    /**
     * 秒杀商品状态
     */
    private FlashItemStatus status;

    /**
     * 发布生成编码并初始化状态
     */
    public void publish(ItemCodeGenerateService itemCodeGenerateService) {
        this.code = itemCodeGenerateService.generateCode();
        this.status = FlashItemStatus.PUBLISHED;
        this.stock.relateCode(this.code);
    }

    public void changeStatus(FlashItemStatus status) {
        // 发布只能上线
        if (this.status.equals(FlashItemStatus.PUBLISHED) && FlashItemStatus.ONLINE.equals(status)) {
            this.status = status;
        }
        // 下线也能上线
        else if (this.status.equals(FlashItemStatus.OFFLINE) && FlashItemStatus.ONLINE.equals(status)) {
            this.status = status;
        }
        // 上线能下线
        else if (this.status.equals(FlashItemStatus.ONLINE) && FlashItemStatus.OFFLINE.equals(status)) {
            this.status = status;
        } else {
            throw new DomainException("[变更商品状态] 状态异常 source: " + this.status + " target: " + status);
        }
    }

}
