package com.actionworks.flashsale.event;

import com.actionworks.flashsale.cache.CacheService;
import com.actionworks.flashsale.cache.constants.CacheConstants;
import com.actionworks.flashsale.domain.event.entity.FlashItemEvent;
import com.actionworks.flashsale.domain.model.entity.FlashItem;
import com.actionworks.flashsale.domain.model.query.FlashItemQueryCondition;
import com.alibaba.cola.dto.Response;
import com.alibaba.cola.event.EventHandler;
import com.alibaba.cola.event.EventHandlerI;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;

@Slf4j
@EventHandler
public class FlashItemEventHandler implements EventHandlerI<Response, FlashItemEvent> {

    @Resource
    private CacheService<FlashItem> cacheService;

    @Override
    public Response execute(FlashItemEvent flashItemEvent) {
        log.info("开始处理秒杀商品事件, {}", flashItemEvent.toString());

        if (flashItemEvent.getId() == null) {
            log.error("秒杀活动事件参数错误");
            return Response.buildFailure("500", "秒杀活动事件参数错误");
        }

        // 刷新单条缓存和清除列表缓存
        cacheService.refreshCache(new FlashItemQueryCondition(flashItemEvent.getId()));
        cacheService.refreshCaches(CacheConstants.FLASH_ITEM_CACHE_LIST_PREFIX);

        return Response.buildSuccess();
    }
}
