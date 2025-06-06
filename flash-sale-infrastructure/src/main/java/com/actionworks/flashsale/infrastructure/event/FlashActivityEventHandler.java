package com.actionworks.flashsale.infrastructure.event;

import com.actionworks.flashsale.application.ability.CacheService;
import com.actionworks.flashsale.common.constants.CacheConstants;
import com.actionworks.flashsale.domain.event.entity.FlashActivityEvent;
import com.actionworks.flashsale.domain.model.activity.aggregate.FlashActivity;
import com.alibaba.cola.dto.Response;
import com.alibaba.cola.event.EventHandler;
import com.alibaba.cola.event.EventHandlerI;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;

@Slf4j
@EventHandler
public class FlashActivityEventHandler implements EventHandlerI<Response, FlashActivityEvent> {

    @Resource
    private CacheService<FlashActivity> cacheService;

    @Override
    public Response execute(FlashActivityEvent flashActivityEvent) {
        log.info("开始处理秒杀活动事件, {}", flashActivityEvent.toString());

        if (flashActivityEvent.getId() == null) {
            log.error("秒杀活动事件参数错误");
            return Response.buildFailure("500", "秒杀活动事件参数错误");
        }

        // 刷新单条缓存和清除列表缓存
        cacheService.refreshCache(CacheConstants.FLASH_ACTIVITY_SINGLE_CACHE_PREFIX, flashActivityEvent.getId());
        cacheService.refreshCaches(CacheConstants.FLASH_ACTIVITY_CACHE_LIST_PREFIX);

        return Response.buildSuccess();
    }
}
