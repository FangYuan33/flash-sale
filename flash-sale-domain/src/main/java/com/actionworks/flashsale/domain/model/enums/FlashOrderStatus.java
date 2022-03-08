package com.actionworks.flashsale.domain.model.enums;

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

    public Integer getCode() {
        return code;
    }
}
