package com.actionworks.flashsale.application.convertor;

import com.actionworks.flashsale.application.query.model.req.FlashItemQuery;
import com.actionworks.flashsale.domain.model.query.FlashItemQueryCondition;

public class FlashItemConvertor {

    public static FlashItemQueryCondition query2QueryCondition(FlashItemQuery query) {
        FlashItemQueryCondition condition = new FlashItemQueryCondition();
        condition.setItemTitle(query.getItemTitle()).setStatus(query.getStatus());
        condition.setPageNum(query.getPageNum());
        condition.setPageSize(query.getPageSize());

        return condition;
    }
}
