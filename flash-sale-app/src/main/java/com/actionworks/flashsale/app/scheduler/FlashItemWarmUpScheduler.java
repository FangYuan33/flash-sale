package com.actionworks.flashsale.app.scheduler;

import com.actionworks.flashsale.cache.ItemStockCacheService;
import com.actionworks.flashsale.domain.model.entity.FlashItem;
import com.actionworks.flashsale.domain.model.enums.BaseEnums;
import com.actionworks.flashsale.domain.model.enums.FlashItemStatus;
import com.actionworks.flashsale.domain.model.query.FlashItemQueryCondition;
import com.actionworks.flashsale.domain.model.query.PageResult;
import com.actionworks.flashsale.domain.service.FlashItemDomainService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component
public class FlashItemWarmUpScheduler {

    @Resource
    private ItemStockCacheService itemStockCacheService;
    @Resource
    private FlashItemDomainService flashItemDomainService;

    /**
     * 预热秒杀商品缓存
     */
    @Scheduled(cron = "* * * * * ?")
    public void warmUpFlashItemTask() {
        log.info("秒杀商品库存预热");

        // 未预热且已上线的秒杀商品
        FlashItemQueryCondition queryCondition = new FlashItemQueryCondition()
                .setWarmUp(BaseEnums.NO.getValue()).setStatus(FlashItemStatus.ONLINE.getCode());
        PageResult<FlashItem> flashItemPageResult = flashItemDomainService.listByQueryCondition(queryCondition);

        // 更新缓存并重置预热标识
        flashItemPageResult.getData().forEach(item -> {
            boolean success = itemStockCacheService.initialItemStocks(item.getId());

            if (success) {
                flashItemDomainService.updateById(item);
            }
        });
    }
}
