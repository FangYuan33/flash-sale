package com.actionworks.flashsale.infrastructure.cache.enums;

import java.util.Objects;

/**
 * 执行lua脚本结果枚举
 *
 * @author fangyuan
 */
public enum LuaResult {

    SUCCESS(1L, "操作成功"),
    FAIL(null, "操作失败"),
    INIT_EXIST(-1L, "初始化缓存库存已经存在"),
    FLASH_ITEM_STOCK_NOT_EXIST(-2L, "商品库存缓存不存在"),
    DECREASE_NOT_ENOUGH(-3L, "商品库存缓存数量不足扣减"),
    DECREASE_ERROR(-4L, "库存扣减异常"),
    INIT_PERMISSION_EXIST(-5L, "初始化下单许可已经存在"),
    DECREASE_PERMISSION_NOT_ENOUGH(-6L, "下单许可数量不足扣减"),
    ITEM_PERMISSION_NOT_EXIST(-7L, "下单许可不存在"),
    RECOVER_PERMISSION_ERROR(-9L, "下单许可恢复失败");

    LuaResult(Long code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    private final Long code;

    private final String msg;

    private String key;

    public void setKey(String key) {
        this.key = key;
    }

    public static LuaResult parse(Long code, String key) {
        if (code == null) {
            FAIL.setKey(key);
            return FAIL;
        }

        for (LuaResult value : values()) {
            if (value == null) {
                continue;
            }

            if (Objects.equals(value.code, code)) {
                value.setKey(key);
                return value;
            }
        }

        throw new IllegalArgumentException("Lua脚本操作缓存返回结果解析错误");
    }

    @Override
    public String toString() {
        return "Lua脚本执行结果: {" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", key='" + key + '\'' +
                '}';
    }
}
