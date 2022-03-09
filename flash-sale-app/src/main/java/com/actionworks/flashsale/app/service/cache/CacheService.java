package com.actionworks.flashsale.app.service.cache;

import com.actionworks.flashsale.domain.model.query.BaseQueryCondition;

public interface CacheService<T> {

    T getCaches(BaseQueryCondition queryCondition);
}
