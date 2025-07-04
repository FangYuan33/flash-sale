package com.actionworks.flashsale.domain.model.enums;

import com.actionworks.flashsale.common.exception.DomainException;
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

        throw new DomainException("解析商品状态 " + code + " 失败");
    }
}
