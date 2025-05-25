package com.actionworks.flashsale.trigger.convertor;

import com.actionworks.flashsale.application.query.model.req.FlashItemQuery;
import com.actionworks.flashsale.trigger.model.request.FlashItemQueryRequest;
import org.springframework.beans.BeanUtils;

public class FlashItemConvertor {

    public static FlashItemQuery toQuery(FlashItemQueryRequest request) {
        if (request == null) {
            return null;
        }

        FlashItemQuery query = new FlashItemQuery();
        BeanUtils.copyProperties(request, query);
        return query;
    }
}
