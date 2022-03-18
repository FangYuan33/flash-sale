package com.actionworks.flashsale.cache.impl;

import com.actionworks.flashsale.cache.CacheService;
import com.actionworks.flashsale.cache.model.EntityCache;
import com.actionworks.flashsale.cache.redis.RedisCacheService;
import com.actionworks.flashsale.domain.exception.DomainException;
import com.actionworks.flashsale.domain.model.query.BaseQueryCondition;
import com.actionworks.flashsale.exception.RepositoryException;
import com.alibaba.fastjson.JSONObject;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.actionworks.flashsale.exception.RepositoryErrorCode.DATA_NOT_FOUND;
import static com.actionworks.flashsale.exception.RepositoryErrorCode.TRY_LATTER;

/**
 * 缓存服务的抽象类，这里使用了模板方法模式
 *
 * @param <T> 泛型，在具体地实现类上再去指定
 */
@Slf4j
public abstract class AbstractCacheService<T> implements CacheService<T> {

    /**
     * 分布式锁的key前缀, key: 前缀 + 查询条件toString
     */
    private static final String UPDATE_LOCK_PREFIX = "UPDATE_LOCK_PREFIX_%s";

    /**
     * 分布式获取锁的等待时间
     */
    private static final long WAIT_TIME = 1L;

    /**
     * 分布式锁最大的持有时间，超过自动释放锁
     */
    private static final long LEASE_TIME = 5L;

    /**
     * 分布式缓存的持续时间
     */
    private static final long DISTRIBUTED_CACHE_LIVE_TIME = 60;

    @Resource
    private RedissonClient redissonClient;
    @Resource
    private RedisCacheService<T> redisCacheService;

    private final Cache<String, EntityCache<T>> flashLocalCache;

    {
        // 初始化最小大小为10, 最大大小暂定33，允许并发修改的线程数是5, 过期时间指定10s
        flashLocalCache = CacheBuilder.newBuilder()
                .initialCapacity(10).maximumSize(33)
                .concurrencyLevel(5).expireAfterWrite(10, TimeUnit.SECONDS).build();
    }

    @Override
    public T getCache(BaseQueryCondition queryCondition) {
        String key = queryCondition.toString();

        EntityCache<T> flashActivityCaches = flashLocalCache.getIfPresent(key);

        if (flashActivityCaches != null) {
            return hitLocalCache(flashActivityCaches).get(0);
        } else {
            return getDataFromDistributedCacheAndSaveLocalCache(queryCondition, key);
        }
    }

    @Override
    public List<T> getCaches(BaseQueryCondition queryCondition) {
        String key = queryCondition.toString();

        EntityCache<T> flashActivityCaches = flashLocalCache.getIfPresent(key);

        if (flashActivityCaches != null) {
            return hitLocalCache(flashActivityCaches);
        } else {
            return getDataListFromDistributedCacheAndSaveLocalCache(queryCondition, key);
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
     * 从分布式缓存中取单个对象，取出后进行本地缓存
     */
    private T getDataFromDistributedCacheAndSaveLocalCache(BaseQueryCondition queryCondition, String key) {
        T data = getDataFromDistributedCache(queryCondition, key);

        if (data == null) {
            saveLocalCache(Collections.emptyList(), key);
        } else {
            saveLocalCache(Collections.singletonList(data), key);
        }

        return data;
    }

    /**
     * 从分布式缓存中取列表对象，取出后进行本地缓存
     */
    private List<T> getDataListFromDistributedCacheAndSaveLocalCache(BaseQueryCondition queryCondition, String key) {
        List<T> dataList = getDataListFromDistributedCache(queryCondition, key);

        saveLocalCache(dataList, key);

        return dataList;
    }

    /**
     * 从Redis分布式缓存获取单条数据
     *
     * @param queryCondition 查询条件 仅是ID而已
     * @param key 缓存对应的key，还是传过来吧，虽然它是从queryCondition来的，显得有些冗余
     */
    private T getDataFromDistributedCache(BaseQueryCondition queryCondition, String key) {
        EntityCache<T> distributedCache = redisCacheService.getValue(key);

        if (distributedCache != null) {
            return hitDistributedCache(distributedCache, key).get(0);
        } else {
            return getDataFromDataBaseAndSaveDistributedCache(queryCondition, key);
        }
    }

    /**
     * 从Redis分布式缓存获取列表数据
     *
     * @param queryCondition 包含多个查询条件
     * @param key 缓存对应的key
     */
    private List<T> getDataListFromDistributedCache(BaseQueryCondition queryCondition, String key) {
        EntityCache<T> distributedCache = redisCacheService.getValue(key);

        if (distributedCache != null) {
            return hitDistributedCache(distributedCache, key);
        } else {
            return getDataListFromDataBaseAndSaveDistributedCache(queryCondition, key);
        }
    }

    /**
     * 命中分布式缓存，直接返回缓存对象
     * 所查询数据不存在时，同样也再向本地缓存中存空的列表对象
     */
    private List<T> hitDistributedCache(EntityCache<T> distributedCache, String key) {
        log.info("命中分布式缓存, {}", JSONObject.toJSONString(distributedCache));

        if (distributedCache.isExist()) {
            return distributedCache.getDataList();
        } else {
            saveLocalCache(Collections.emptyList(), key);

            throw new RepositoryException(DATA_NOT_FOUND);
        }
    }

    /**
     * 未命中分布式缓存：先获取分布式锁，成功后在数据库中查，之后保存在分布式缓存中
     * 获取分布式锁失败，则抛出业务异常
     */
    private T getDataFromDataBaseAndSaveDistributedCache(BaseQueryCondition queryCondition, String key) {
        RLock lock = redissonClient.getLock(String.format(UPDATE_LOCK_PREFIX, key));

        T data = null;
        try {
            if (lock.tryLock(WAIT_TIME, LEASE_TIME, TimeUnit.SECONDS)) {
                try {
                    data = getSingleDataFromDataBase(queryCondition);

                    saveDistributedCache(Collections.singletonList(data), key);
                } catch (DomainException e) {
                    // 查询不存在的数据，同样加入缓存中，防止缓存穿透
                    saveDistributedCache(Collections.emptyList(), key);
                } finally {
                    lock.unlock();
                }
            } else {
                throw new RepositoryException(TRY_LATTER);
            }
        } catch (InterruptedException e) {
            log.error("分布式锁获取失败", e);
        }

        return data;
    }

    /**
     * 未命中分布式缓存：先获取分布式锁，成功后在数据库中查，之后保存在分布式缓存中
     * 获取分布式锁失败，则抛出业务异常
     *
     * 列表查询，数据不存在时，不会抛出DomainException
     * 相比getDataFromDataBaseAndSaveDistributedCache方法少了一个catch语句
     */
    private List<T> getDataListFromDataBaseAndSaveDistributedCache(BaseQueryCondition queryCondition, String key) {
        RLock lock = redissonClient.getLock(String.format(UPDATE_LOCK_PREFIX, key));

        List<T> data = null;
        try {
            if (lock.tryLock(WAIT_TIME, LEASE_TIME, TimeUnit.SECONDS)) {
                try {
                    data = getDataListFromDataBase(queryCondition);

                    saveDistributedCache(data, key);
                } finally {
                    lock.unlock();
                }
            } else {
                throw new RepositoryException(TRY_LATTER);
            }
        } catch (InterruptedException e) {
            log.error("分布式锁获取失败", e);
        }

        return data;
    }

    /**
     * 保存在分布式缓存中
     */
    private void saveDistributedCache(List<T> dataList, String key) {
        // 创建缓存对象，并设置所查数据是否存在
        EntityCache<T> entityCache = new EntityCache<>();
        entityCache.setDataList(dataList).setExist(!CollectionUtils.isEmpty(dataList));

        redisCacheService.setValue(key, entityCache, DISTRIBUTED_CACHE_LIVE_TIME);
        log.info("分布式缓存已更新, {}", JSONObject.toJSONString(entityCache));
    }

    /**
     * 保存在本地缓存中
     */
    private void saveLocalCache(List<T> dataList, String key) {
        // 创建缓存对象，并设置所查数据是否存在
        EntityCache<T> entityCache = new EntityCache<>();
        entityCache.setDataList(dataList).setExist(!CollectionUtils.isEmpty(dataList));

        flashLocalCache.put(key, entityCache);
        log.info("本地缓存已更新, {}", JSONObject.toJSONString(entityCache));
    }

    /**
     * 根据不同的服务做具体地查询，单个对象
     *
     * @param queryCondition 仅仅包含ID信息
     */
    protected abstract T getSingleDataFromDataBase(BaseQueryCondition queryCondition);

    /**
     * 根据不同的服务做具体地实现，多个对象
     */
    protected abstract List<T> getDataListFromDataBase(BaseQueryCondition queryCondition);
}
