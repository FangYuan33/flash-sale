package com.actionworks.flashsale.app.service.cache.impl;

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
public class FlashActivityCacheServiceImpl extends AbstractCacheService<FlashActivity> {

    @Resource
    private FlashActivityDomainService flashActivityDomainService;

    @Override
    protected List<FlashActivity> getDataFromDataBase(BaseQueryCondition queryCondition) {
        PageResult<FlashActivity> flashActivityPageResult =
                flashActivityDomainService.listByQueryCondition((FlashActivityQueryCondition) queryCondition);

        return flashActivityPageResult.getData();
    }

    @Override
    protected FlashActivity getSingleDataFromDataBase(BaseQueryCondition queryCondition) {
        return flashActivityDomainService.getFlashActivity(((FlashActivityQueryCondition) queryCondition).getActivityId());
    }
}
