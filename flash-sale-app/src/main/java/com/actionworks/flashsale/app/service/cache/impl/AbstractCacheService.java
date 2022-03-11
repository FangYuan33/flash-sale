package com.actionworks.flashsale.app.service.cache.impl;

import com.actionworks.flashsale.app.service.cache.CacheService;
import com.actionworks.flashsale.app.service.cache.model.EntityCache;
import com.actionworks.flashsale.domain.model.query.BaseQueryCondition;
import com.actionworks.flashsale.exception.RepositoryException;
import com.alibaba.fastjson.JSONObject;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.actionworks.flashsale.exception.RepositoryErrorCode.DATA_NOT_FOUND;

/**
 * 缓存服务的抽象类，这里使用了模板方法模式
 * 因为我在写代码的时候，发现读取本地缓存的步骤是固定的: 1. 先取 2. 取不到再存
 * 发现第一步获取缓存的操作，在不同的服务上（秒杀活动、商品、订单）都是会重复的
 * 所以我就把这一方法定义了默认实现
 * 而存缓存的时候，只有查询数据库的操作是不同的，针对不同的服务做实现
 * 所以定义了一个抽象方法针对各个服务查数据库的操作，供不同的服务自己实现
 *
 * @param <T> 泛型，在具体的实现类上再去指定
 */
@Slf4j
public abstract class AbstractCacheService<T> implements CacheService<T> {

    private final Cache<String, EntityCache<T>> flashLocalCache;

    {
        // 初始化最小大小为10, 允许并发修改的线程数是5, 过期时间指定10s
        flashLocalCache = CacheBuilder.newBuilder()
                .initialCapacity(10).concurrencyLevel(5).expireAfterWrite(10, TimeUnit.SECONDS).build();
    }

    @Override
    public T getCache(BaseQueryCondition queryCondition) {
        String key = queryCondition.toString();

        EntityCache<T> flashActivityCaches = flashLocalCache.getIfPresent(key);

        if (flashActivityCaches != null) {
            return hitLocalCache(flashActivityCaches).get(0);
        } else {
            return saveLocalCacheAndGetData(queryCondition, key);
        }
    }

    @Override
    public List<T> getCaches(BaseQueryCondition queryCondition) {
        String key = queryCondition.toString();

        EntityCache<T> flashActivityCaches = flashLocalCache.getIfPresent(key);

        if (flashActivityCaches != null) {
            // 命中缓存
            return hitLocalCache(flashActivityCaches);
        } else {
            // 未命中缓存
            return saveLocalCacheAndGetDataList(queryCondition, key);
        }
    }

    /**
     * 命中本地缓存直接返回缓存对象
     */
    private List<T> hitLocalCache(EntityCache<T> flashActivityCaches) {
        log.info("命中本地缓存, {}", JSONObject.toJSONString(flashActivityCaches));

        if (flashActivityCaches.isExist()) {
            return flashActivityCaches.getDataList();
        } else {
            throw new RepositoryException(DATA_NOT_FOUND);
        }
    }

    /**
     * 通过ID查询单个缓存时，调用该方法
     * 未命中本地缓存，查库，保存在本地缓存中
     * 这里使用了try-catch 避免了频繁的对数据库的访问
     * 因为我们在请求一个不存在的ID对应的数据时，会抛出数据不存在的业务异常
     * 若不使用try-catch的话，则不会将“空对象”保存在本地缓存中，那么会使数据库压力过大
     * 每次请求都会打到数据库上，造成缓存穿透
     *
     * @param queryCondition 仅仅包含ID信息
     * @param key 缓存对应的key值
     * @return 单个对象
     */
    private T saveLocalCacheAndGetData(BaseQueryCondition queryCondition, String key) {
        T data = null;
        try {
            data = getSingleDataFromDataBase(queryCondition);

            saveLocalCache(Collections.singletonList(data), key);
        } catch (Exception e) {
            saveLocalCache(Collections.emptyList(), key);
        }

        return data;
    }

    /**
     * 多条件查询返回多个缓存对象时，调用该方法
     * 未命中本地缓存，查库，并保存在本地缓存中
     *
     * @param queryCondition 多个查询条件
     * @param key 缓存对应的key值
     * @return 返回查询条件对应的对象
     */
    private List<T> saveLocalCacheAndGetDataList(BaseQueryCondition queryCondition, String key) {
        List<T> data = getDataListFromDataBase(queryCondition);

        saveLocalCache(data, key);

        return data;
    }

    /**
     * 保存在本地缓存中
     */
    private void saveLocalCache(List<T> dataList, String key) {
        // 创建缓存对象，并设置所查数据是否存在
        EntityCache<T> entityCache = new EntityCache<>();
        entityCache.setDataList(dataList).setExist(!CollectionUtils.isEmpty(dataList));

        // 存在本地缓存中， key: 查询条件的JSON字符串
        flashLocalCache.put(key, entityCache);
        log.info("存入本地缓存, {}", JSONObject.toJSONString(entityCache));
    }

    /**
     * 根据不同的服务做具体的查询，单个对象
     *
     * @param queryCondition 仅仅包含ID信息
     */
    protected abstract T getSingleDataFromDataBase(BaseQueryCondition queryCondition);

    /**
     * 根据不同的服务做具体的实现，多个对象
     */
    protected abstract List<T> getDataListFromDataBase(BaseQueryCondition queryCondition);
}
