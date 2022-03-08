package com.actionworks.flashsale.cache.localCache.impl;

import com.actionworks.flashsale.cache.localCache.LocalCacheService;
import com.actionworks.flashsale.cache.model.EntityCache;
import com.actionworks.flashsale.domain.model.entity.FlashActivity;
import com.actionworks.flashsale.domain.model.query.BaseQueryCondition;
import com.actionworks.flashsale.domain.model.query.FlashActivityQueryCondition;
import com.actionworks.flashsale.domain.repository.FlashActivityRepository;
import com.actionworks.flashsale.exception.RepositoryException;
import com.alibaba.fastjson.JSONObject;
import com.google.common.cache.CacheBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.google.common.cache.Cache;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static com.actionworks.flashsale.exception.RepositoryErrorCode.DATA_NOT_FOUND;
import static com.actionworks.flashsale.exception.RepositoryErrorCode.TRY_LATTER;

@Slf4j
@Service
public class FlashActivityLocalCacheServiceImpl implements LocalCacheService<List<FlashActivity>> {

    @Resource
    private FlashActivityRepository flashActivityRepository;

    private final static Cache<String, EntityCache<List<FlashActivity>>> FLASH_ACTIVITY_LOCAL_CACHE;

    private final Lock localCacheUpdatelock = new ReentrantLock();


    static {
        //
        FLASH_ACTIVITY_LOCAL_CACHE = CacheBuilder.newBuilder()
                .initialCapacity(10).concurrencyLevel(5).expireAfterWrite(10, TimeUnit.SECONDS).build();
    }

    @Override
    public List<FlashActivity> getCaches(BaseQueryCondition queryCondition) {
        // TODO look this different

        // find
        String key = JSONObject.toJSONString(queryCondition);
        EntityCache<List<FlashActivity>> flashActivityCaches = FLASH_ACTIVITY_LOCAL_CACHE.getIfPresent(key);

        if (flashActivityCaches != null) {
            log.info("FlashActivity命中本地缓存, {}", JSONObject.toJSONString(flashActivityCaches));

            if (flashActivityCaches.isExist()) {
                return flashActivityCaches.getData();
            } else {
                throw new RepositoryException(DATA_NOT_FOUND);
            }
        } else {
            Optional<List<FlashActivity>> flashActivities =
                    flashActivityRepository.listByQueryCondition((FlashActivityQueryCondition) queryCondition);

            List<FlashActivity> flashActivityList = flashActivities.get();

            EntityCache<List<FlashActivity>> entityCache = new EntityCache<>();
            entityCache.setData(flashActivityList).setExist(!flashActivityList.isEmpty());

            boolean tryLockSuccess = localCacheUpdatelock.tryLock();
            if (tryLockSuccess) {
                try {
                    FLASH_ACTIVITY_LOCAL_CACHE.put(key, entityCache);
                } finally {
                    localCacheUpdatelock.unlock();
                }
            } else {
                throw new RepositoryException(TRY_LATTER);
            }

            return flashActivityList;
        }
    }
}
