package com.actionworks.flashsale.app.scheduler;

import com.actionworks.flashsale.cache.ItemPermissionCacheService;
import com.actionworks.flashsale.domain.model.entity.FlashItem;
import com.actionworks.flashsale.domain.model.enums.FlashItemStatus;
import com.actionworks.flashsale.domain.model.query.FlashItemQueryCondition;
import com.actionworks.flashsale.domain.service.FlashItemDomainService;
import com.actionworks.flashsale.nacos.NacosProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
public class ItemPermissionScheduler {
    @Resource
    private NacosProperties nacosProperties;

    @Resource
    private ItemPermissionCacheService itemPermissionCacheService;
    @Resource
    private FlashItemDomainService flashItemDomainService;

    /**
     * 预热秒杀下单许可
     */
    @Scheduled(cron = "*/5 * * * * ?")
    public void initialItemPermission() {
        if (nacosProperties.getInitialItemPermissionFlag()) {
            log.info("秒杀商品下单许可初始化");

            // 已上线 秒杀时间未结束 的秒杀商品
            FlashItemQueryCondition queryCondition = new FlashItemQueryCondition()
                    .setStatus(FlashItemStatus.ONLINE.getCode()).setEndTime(LocalDateTime.now());
            List<FlashItem> flashItems = flashItemDomainService.listByQueryConditionWithoutPageSize(queryCondition);

            // 更新下单许可
            flashItems.forEach(item -> itemPermissionCacheService.initialItemPermission(item.getId()));
        }
    }
}
