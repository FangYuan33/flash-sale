package com.actionworks.flashsale.common.exception;

/**
 * Domain层专用异常枚举
 *
 * @author fangyuan
 */
public enum DomainErrorCode {

    /**
     * 通用错误码
     */
    PARAMS_INVALID( "参数错误"),
    /**
     * 活动相关错误码
     */
    FLASH_ACTIVITY_NOT_EXIST("活动不存在"),
    PUBLISH_FLASH_ACTIVITY_PARAMS_INVALID("待上线的活动参数无效"),
    OFFLINE_FLASH_ACTIVITY_FORBIDDEN("未上线的活动不能下线"),
    /**
     * 秒杀活动相关错误码
     */
    PUBLISH_FLASH_ITEM_PARAMS_INVALID("待发布的秒杀活动参数无效"),
    FLASH_ITEM_NOT_EXIST("秒杀商品不存在"),
    /**
     * 秒杀订单相关错误码
     */
    FLASH_ORDER_NOT_EXIST("秒杀订单不存在");

    private final String errDesc;

    DomainErrorCode(String errDesc) {
        this.errDesc = errDesc;
    }

    public String getErrDesc() {
        return errDesc;
    }
}