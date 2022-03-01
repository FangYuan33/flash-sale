package com.actionworks.flashsale.domain.model.enums;

/**
 * 秒杀活动状态
 */
public enum FlashActivityStatus {
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

    FlashActivityStatus(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }
}
