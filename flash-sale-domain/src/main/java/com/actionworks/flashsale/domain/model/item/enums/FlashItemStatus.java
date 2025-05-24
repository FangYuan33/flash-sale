package com.actionworks.flashsale.domain.model.item.enums;

import lombok.Getter;

@Getter
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

    public static FlashItemStatus parseByCode(int code) {
        for (FlashItemStatus value : values()) {
            if (value.code.equals(code)) {
                return value;
            }
        }

        return null;
    }
}
