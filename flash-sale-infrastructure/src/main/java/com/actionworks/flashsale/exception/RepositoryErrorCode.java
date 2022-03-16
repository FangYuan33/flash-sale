package com.actionworks.flashsale.exception;

/**
 * RepositoryException专用异常枚举
 */
public enum RepositoryErrorCode {

    /**
     * 通用异常枚举
     */
    ID_NOT_EXIST("ID为空"),

    /**
     * 缓存异常枚举
     */
    DATA_NOT_FOUND("查询数据不存在"),
    FLASH_ITEM_STOCK_CACHE_FAILED("秒杀商品库存缓存失败"),
    /**
     *请稍后尝试
     */
    TRY_LATTER("请稍后尝试");

    private final String errorDesc;

    RepositoryErrorCode(String errorDesc) {
        this.errorDesc = errorDesc;
    }

    public String getErrorDesc() {
        return errorDesc;
    }
}
