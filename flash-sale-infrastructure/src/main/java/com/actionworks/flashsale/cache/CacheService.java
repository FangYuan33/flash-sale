package com.actionworks.flashsale.cache;

import com.actionworks.flashsale.domain.model.query.BaseQueryCondition;

import java.util.List;

public interface CacheService<T> {

    /**
     * 查询单条本地缓存数据
     */
    T getCache(BaseQueryCondition queryCondition);

    /**
     * 根据查询条件读取本地缓存
     */
    List<T> getCaches(BaseQueryCondition queryCondition);
}
