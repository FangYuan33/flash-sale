package com.actionworks.flashsale.trigger.model.convertor;

import com.actionworks.flashsale.application.model.result.AppResult;
import com.alibaba.cola.dto.SingleResponse;

/**
 * app层结果对象转换为controller结果对象
 *
 * @author fangyuan
 */
public class ResponseConvertor {

    public static <T> SingleResponse<T> with(AppResult<T> appResult) {
        if (appResult == null) {
            return new SingleResponse<>();
        }

        SingleResponse<T> singleResponse = new SingleResponse<>();
        singleResponse.setSuccess(appResult.isSuccess());
        singleResponse.setErrCode(appResult.getCode());
        singleResponse.setErrMessage(appResult.getMsg());
        singleResponse.setData(appResult.getData());

        return singleResponse;
    }
}
