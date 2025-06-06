package com.actionworks.flashsale.application.scheduler;

import com.actionworks.flashsale.domain.model.item.aggregate.FlashItem;
import com.actionworks.flashsale.infrastructure.cache.ItemStockCacheService;
import com.actionworks.flashsale.common.enums.BaseEnums;
import com.actionworks.flashsale.domain.model.item.enums.FlashItemStatus;
import com.actionworks.flashsale.domain.model.query.FlashItemQueryCondition;
import com.actionworks.flashsale.domain.service.FlashItemDomainService;
import com.actionworks.flashsale.infrastructure.nacos.NacosProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
public class FlashItemWarmUpScheduler {

    @Resource
    private NacosProperties nacosProperties;

    @Resource
    private ItemStockCacheService itemStockCacheService;
    @Resource
    private FlashItemDomainService flashItemDomainService;

    /**
     * 预热秒杀商品缓存
     */
    @Scheduled(cron = "*/5 * * * * ?")
    public void warmUpFlashItemTask() {
        if (nacosProperties.getWarmUpFlag()) {
            log.info("秒杀商品库存预热");

            // 未预热 已上线 秒杀时间未结束 的秒杀商品
            FlashItemQueryCondition queryCondition = new FlashItemQueryCondition().setWarmUp(BaseEnums.NO.getValue())
                    .setStatus(FlashItemStatus.ONLINE.getCode()).setEndTime(LocalDateTime.now());
            List<FlashItem> flashItemEntities = flashItemDomainService.listByQueryConditionWithoutPageSize(queryCondition);

            // 更新缓存并重置预热标识
            flashItemEntities.forEach(item -> {
                boolean success = itemStockCacheService.initialItemStocks(item.getId());

                if (success) {
                    flashItemDomainService.updateById(item.setWarmUp(BaseEnums.YES.getValue()));
                }
            });
        }
    }
}
