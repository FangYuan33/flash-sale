package com.actionworks.flashsale.domain.model.enums;

/**
 * 通用的一些枚举
 *
 * @author fangyuan
 */
public enum BaseEnums {

    YES(1),
    NO(0);

    BaseEnums(Integer value) {
        this.value = value;
    }

    private final Integer value;

    public Integer getValue() {
        return value;
    }
}
