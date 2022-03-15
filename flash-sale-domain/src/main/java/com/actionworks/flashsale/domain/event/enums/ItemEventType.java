package com.actionworks.flashsale.domain.event.enums;

/**
 * 秒杀商品事件类型
 */
public enum ItemEventType {
    PUBLISH("发布秒杀商品"),
    ONLINE("上线秒杀商品"),
    OFFLINE("下线秒杀商品");

    private final String desc;

    ItemEventType(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }
}
