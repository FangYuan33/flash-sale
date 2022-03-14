package com.actionworks.flashsale.event;

import com.actionworks.flashsale.cache.CacheService;
import com.actionworks.flashsale.domain.event.entity.FlashActivityEvent;
import com.actionworks.flashsale.domain.model.entity.FlashActivity;
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
            return Response.buildSuccess();
        }

        cacheService.refreshCache(flashActivityEvent.getId());

        return Response.buildSuccess();
    }
}
