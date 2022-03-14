package com.actionworks.flashsale.domain.event.enums;

/**
 * 秒杀活动事件类型
 */
public enum ActivityEventType {

    PUBLISH("发布秒杀活动"),
    ONLINE("上线秒杀活动"),
    OFFLINE("下线秒杀活动");

    private final String desc;

    ActivityEventType(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }
}
