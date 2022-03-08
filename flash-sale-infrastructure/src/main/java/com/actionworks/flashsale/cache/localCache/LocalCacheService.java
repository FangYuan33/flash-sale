package com.actionworks.flashsale.cache.localCache;

import com.actionworks.flashsale.domain.model.query.BaseQueryCondition;

public interface LocalCacheService<T> {

    T getCaches(BaseQueryCondition queryCondition);
}
