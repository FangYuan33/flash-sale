package com.actionworks.flashsale.domain.model.enums;

public enum FlashItemStatus {
    /**
     * 已发布
     */
    PUBLISHED(10),
    /**
     * 已上线
     */
    ONLINE(20),
    /**
     * 已下线
     */
    OFFLINE(30);

    private final Integer code;

    FlashItemStatus(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }
}
