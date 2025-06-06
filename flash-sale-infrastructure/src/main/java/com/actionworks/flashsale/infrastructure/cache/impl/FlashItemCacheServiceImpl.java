package com.actionworks.flashsale.infrastructure.cache.impl;

import com.actionworks.flashsale.domain.model.item.aggregate.FlashItem;
import com.actionworks.flashsale.domain.model.query.BaseQueryCondition;
import com.actionworks.flashsale.domain.model.query.FlashItemQueryCondition;
import com.actionworks.flashsale.domain.model.query.PageResult;
import com.actionworks.flashsale.domain.service.FlashItemDomainService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class FlashItemCacheServiceImpl extends AbstractCacheService<FlashItem> {

    @Resource
    private FlashItemDomainService flashItemDomainService;

    @Override
    protected FlashItem getSingleDataFromDataBase(Long id) {
        return flashItemDomainService.getById(id);
    }

    @Override
    protected List<FlashItem> getDataListFromDataBase(BaseQueryCondition queryCondition) {
        PageResult<FlashItem> flashItemPageResult =
                flashItemDomainService.listByQueryCondition((FlashItemQueryCondition) queryCondition);

        return flashItemPageResult.getData();
    }
}
