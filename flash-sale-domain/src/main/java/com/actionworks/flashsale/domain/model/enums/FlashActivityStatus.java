package com.actionworks.flashsale.domain.model.enums;

import com.actionworks.flashsale.common.exception.DomainException;
import lombok.Getter;

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

    public static FlashActivityStatus parse(Integer status) {
        for (FlashActivityStatus value : values()) {
            if (value.code.equals(status)) {
                return value;
            }
        }

        throw new DomainException("解析活动状态 " + status + " 失败");
    }
}
