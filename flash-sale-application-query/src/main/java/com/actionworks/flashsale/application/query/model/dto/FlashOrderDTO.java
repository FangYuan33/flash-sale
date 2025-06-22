package com.actionworks.flashsale.application.query.model.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class FlashOrderDTO {
    private Long id;
    private String code;
    private Long userId;
    private String itemCode;
    private Integer quantity;
    private Long totalAmount;
    private Integer status;
} 