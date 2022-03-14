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

    /**
     * 更新缓存
     *
     * @param id 对应秒杀活动或秒杀商品的ID
     */
    void refreshCache(Long id);
}
