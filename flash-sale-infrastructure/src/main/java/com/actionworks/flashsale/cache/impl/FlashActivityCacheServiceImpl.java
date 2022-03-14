package com.actionworks.flashsale.cache.impl;

import com.actionworks.flashsale.domain.model.entity.FlashActivity;
import com.actionworks.flashsale.domain.model.query.BaseQueryCondition;
import com.actionworks.flashsale.domain.model.query.FlashActivityQueryCondition;
import com.actionworks.flashsale.domain.model.query.PageResult;
import com.actionworks.flashsale.domain.service.FlashActivityDomainService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class FlashActivityCacheServiceImpl extends AbstractCacheService<FlashActivity> {

    @Resource
    private FlashActivityDomainService flashActivityDomainService;

    @Override
    protected List<FlashActivity> getDataListFromDataBase(BaseQueryCondition queryCondition) {
        PageResult<FlashActivity> flashActivityPageResult =
                flashActivityDomainService.listByQueryCondition((FlashActivityQueryCondition) queryCondition);

        return flashActivityPageResult.getData();
    }

    @Override
    protected FlashActivity getSingleDataFromDataBase(BaseQueryCondition queryCondition) {
        return flashActivityDomainService.getFlashActivity(((FlashActivityQueryCondition) queryCondition).getActivityId());
    }

    @Override
    public void refreshCache(Long activityId) {
        FlashActivityQueryCondition queryCondition = new FlashActivityQueryCondition(activityId);

        // 更新分布式缓存
        getDataFromDataBaseAndSaveDistributedCache(queryCondition, queryCondition.toString());
    }
}
