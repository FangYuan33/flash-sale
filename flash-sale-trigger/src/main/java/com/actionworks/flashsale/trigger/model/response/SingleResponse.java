package com.actionworks.flashsale.trigger.model.response;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class SingleResponse<T> extends Response {

    private static final long serialVersionUID = 11L;

    private T data;

    public static <T> SingleResponse<T> of(T data) {
        SingleResponse<T> response = new SingleResponse<>();
        response.setSuccess(true);
        response.setData(data);
        return response;
    }
    
}
