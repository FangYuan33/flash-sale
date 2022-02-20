package com.actionworks.flashsale.app.model.result;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * service返回结果对象
 */
@Getter
@NoArgsConstructor
public class AppResult<T> {

    /**
     * 状态码
     */
    private String code;

    /**
     * 成功标志
     */
    private boolean isSuccess;

    /**
     * 消息
     */
    private String msg;

    /**
     * 结果对象
     */
    private T data;

    public AppResult(String code, boolean isSuccess, String msg, T data) {
        this.code = code;
        this.isSuccess = isSuccess;
        this.msg = msg;
        this.data = data;
    }

    private enum Type {
        /**
         * 成功
         */
        SUCCESS("0"),
        /**
         * 失败
         */
        ERROR("500");

        private final String value;

        Type(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public static <T> AppResult<T> success() {
        return AppResult.success("操作成功");
    }

    public static <T> AppResult<T> success(String msg) {
        return AppResult.success(msg, null);
    }

    public static <T> AppResult<T> success(String msg, T data) {
        return new AppResult<>(Type.SUCCESS.getValue(), true, msg, data);
    }

    public static <T> AppResult<T> error() {
        return AppResult.error("操作失败");
    }

    public static <T> AppResult<T> error(String msg) {
        return AppResult.error(msg, null);
    }

    public static <T> AppResult<T> error(String msg, T data) {
        return new AppResult<>(Type.ERROR.getValue(), false, msg, data);
    }
}
