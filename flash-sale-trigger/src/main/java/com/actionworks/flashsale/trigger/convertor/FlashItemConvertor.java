package com.actionworks.flashsale.trigger.convertor;

import com.actionworks.flashsale.application.query.model.req.FlashItemQuery;
import com.actionworks.flashsale.trigger.model.request.FlashItemQueryRequest;

public class FlashItemConvertor {

    public static FlashItemQuery toQuery(FlashItemQueryRequest request) {
        if (request == null) {
            return null;
        }

        FlashItemQuery query = new FlashItemQuery();
        query.setItemTitle(request.getItemTitle());
        query.setStatus(request.getStatus());
        query.setPageNum(request.getPageNum());
        query.setPageSize(request.getPageSize());

        return query;
    }

}
