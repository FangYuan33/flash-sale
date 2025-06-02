package com.actionworks.flashsale.domain.model.enums;

import com.actionworks.flashsale.common.exception.DomainException;
import lombok.Getter;

@Getter
public enum FlashOrderStatus {

    /**
     * 已创建
     */
    CREATE(10),
    /**
     * 已取消
     */
    CANCEL(20);

    private final Integer code;

    FlashOrderStatus(Integer code) {
        this.code = code;
    }

    public static FlashOrderStatus parse(Integer status) {
        if (status == null) {
            return null;
        }

        for (FlashOrderStatus orderStatus : FlashOrderStatus.values()) {
            if (orderStatus.getCode().equals(status)) {
                return orderStatus;
            }
        }
        throw new DomainException("解析订单状态 " + status + " 失败");
    }
}
