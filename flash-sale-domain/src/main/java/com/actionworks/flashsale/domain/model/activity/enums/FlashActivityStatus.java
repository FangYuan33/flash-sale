package com.actionworks.flashsale.domain.model.activity.enums;

import lombok.Getter;

/**
 * 秒杀活动状态
 */
@Getter
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

}
