package com.actionworks.flashsale.app.service.cache.impl;

import com.actionworks.flashsale.app.service.cache.model.EntityCache;
import com.actionworks.flashsale.domain.model.entity.FlashActivity;
import com.actionworks.flashsale.domain.model.query.BaseQueryCondition;
import com.actionworks.flashsale.domain.model.query.FlashActivityQueryCondition;
import com.actionworks.flashsale.domain.model.query.PageResult;
import com.actionworks.flashsale.domain.service.FlashActivityDomainService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Service
public class FlashActivityCacheServiceImpl extends AbstractCacheService<List<FlashActivity>> {

    @Resource
    private FlashActivityDomainService flashActivityDomainService;

    @Override
    protected List<FlashActivity> saveLocalCacheAndGetData(BaseQueryCondition queryCondition, String key) {
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
