package com.actionworks.flashsale.app.service.cache;

import com.actionworks.flashsale.domain.model.query.BaseQueryCondition;

public interface CacheService<T> {

    /**
     * 根据查询条件读取本地缓存
     */
    T getCaches(BaseQueryCondition queryCondition);
}
