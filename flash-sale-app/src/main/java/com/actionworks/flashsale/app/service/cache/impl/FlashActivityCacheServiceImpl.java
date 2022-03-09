package com.actionworks.flashsale.app.service.cache.impl;

import com.actionworks.flashsale.app.service.cache.CacheService;
import com.actionworks.flashsale.app.service.cache.model.EntityCache;
import com.actionworks.flashsale.domain.model.entity.FlashActivity;
import com.actionworks.flashsale.domain.model.query.BaseQueryCondition;
import com.actionworks.flashsale.domain.model.query.FlashActivityQueryCondition;
import com.actionworks.flashsale.domain.model.query.PageResult;
import com.actionworks.flashsale.domain.service.FlashActivityDomainService;
import com.actionworks.flashsale.exception.RepositoryException;
import com.alibaba.fastjson.JSONObject;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.actionworks.flashsale.exception.RepositoryErrorCode.DATA_NOT_FOUND;

@Slf4j
@Service
public class FlashActivityCacheServiceImpl implements CacheService<List<FlashActivity>> {

    @Resource
    private FlashActivityDomainService flashActivityDomainService;

    private final static Cache<String, EntityCache<List<FlashActivity>>> FLASH_ACTIVITY_LOCAL_CACHE;


    static {
        // 初始化最小大小为10, 允许并发修改的线程数是5, 过期时间指定10s
        FLASH_ACTIVITY_LOCAL_CACHE = CacheBuilder.newBuilder()
                .initialCapacity(10).concurrencyLevel(5).expireAfterWrite(10, TimeUnit.SECONDS).build();
    }

    @Override
    public List<FlashActivity> getCaches(BaseQueryCondition queryCondition) {
        String key = queryCondition.toString();

        EntityCache<List<FlashActivity>> flashActivityCaches = FLASH_ACTIVITY_LOCAL_CACHE.getIfPresent(key);

        if (flashActivityCaches != null) {
            // 命中缓存
            return hitLocalCache(flashActivityCaches);
        } else {
            // 未命中缓存
            return saveLocalCacheAndGetData(queryCondition, key);
        }
    }

    /**
     * 命中本地缓存直接返回缓存对象
     */
    private List<FlashActivity> hitLocalCache(EntityCache<List<FlashActivity>> flashActivityCaches) {
        log.info("FlashActivity命中本地缓存, {}", JSONObject.toJSONString(flashActivityCaches));

        if (flashActivityCaches.isExist()) {
            return flashActivityCaches.getData();
        } else {
            throw new RepositoryException(DATA_NOT_FOUND);
        }
    }

    /**
     * 未命中本地缓存，查库，并保存在本地缓存中
     *
     * @param key 缓存对应的key值
     * @return 返回查询条件对应的对象
     */
    private List<FlashActivity> saveLocalCacheAndGetData(BaseQueryCondition queryCondition, String key) {
        // 查库获取数据
        PageResult<FlashActivity> flashActivityPageResult =
                flashActivityDomainService.listByQueryCondition((FlashActivityQueryCondition) queryCondition);
        List<FlashActivity> flashActivityList = flashActivityPageResult.getData();

        // 创建缓存对象，并设置所差数据是否存在
        EntityCache<List<FlashActivity>> entityCache = new EntityCache<>();
        entityCache.setData(flashActivityList).setExist(!flashActivityList.isEmpty());

        // 存在本地缓存中， key: 查询条件的JSON字符串
        FLASH_ACTIVITY_LOCAL_CACHE.put(key, entityCache);

        return flashActivityList;
    }
}
