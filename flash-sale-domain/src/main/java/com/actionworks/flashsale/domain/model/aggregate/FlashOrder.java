package com.actionworks.flashsale.domain.model.aggregate;

import com.actionworks.flashsale.common.model.AggregateRoot;
import com.actionworks.flashsale.domain.model.enums.FlashOrderStatus;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

@Data
@Builder
@Slf4j
@Setter(AccessLevel.PRIVATE)
public class FlashOrder implements AggregateRoot, Serializable {

    private static final long serialVersionUID = 5230422L;

    private Long id;

    private String code;

    private String itemCode;

    private String itemTitle;;

    private Long userId;

    private Integer quantity;

    private Long totalAmount;

    private FlashOrderStatus status;

    public void create(FlashItem flashItem) {
        this.itemCode = flashItem.getCode();
        this.itemTitle = flashItem.getItemTitle();
        this.totalAmount = flashItem.getItemPrice().getFlashPrice() * quantity;
        this.status = FlashOrderStatus.CREATE;
    }
    
    public void assignCode(String code) {
        this.code = code;
    }
}
