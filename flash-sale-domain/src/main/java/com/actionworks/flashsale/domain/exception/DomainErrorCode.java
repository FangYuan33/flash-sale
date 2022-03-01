package com.actionworks.flashsale.domain.exception;


import com.alibaba.cola.dto.ErrorCodeI;

/**
 * Domain层专用异常枚举
 *
 * @author fangyuan
 */
public enum DomainErrorCode implements ErrorCodeI {

    /**
     * 通用错误码
     */
    PARAMS_INVALID("500", "参数错误"),
    /**
     * 活动相关错误码
     */
    FLASH_ACTIVITY_NOT_EXIST("301", "活动不存在"),
    PUBLISH_FLASH_ACTIVITY_PARAMS_INVALID("302", "待上线的活动参数无效"),
    OFFLINE_FLASH_ACTIVITY_FORBIDDEN("303", "未上线的活动不能下线"),

    /**
     * 秒杀活动相关错误码
     */
    PUBLISH_FLASH_ITEM_PARAMS_INVALID("401", "待发布的秒杀活动参数无效");



    private final String errCode;
    private final String errDesc;

    DomainErrorCode(String errCode, String errDesc) {
        this.errCode = errCode;
        this.errDesc = errDesc;
    }

    @Override
    public String getErrCode() {
        return errCode;
    }

    @Override
    public String getErrDesc() {
        return errDesc;
    }
}