package com.actionworks.flashsale.application.service.item;

import com.actionworks.flashsale.application.ability.CacheService;
import com.actionworks.flashsale.application.exception.BizException;
import com.actionworks.flashsale.application.model.command.FlashItemPublishCommand;
import com.actionworks.flashsale.application.query.convertor.FlashItemAppConvertor;
import com.actionworks.flashsale.application.model.result.AppResult;
import com.actionworks.flashsale.domain.model.item.aggregate.FlashItem;
import com.actionworks.flashsale.infrastructure.cache.ItemStockCacheService;
import com.actionworks.flashsale.infrastructure.cache.constants.CacheConstants;
import com.actionworks.flashsale.domain.model.activity.aggregate.FlashActivity;
import com.actionworks.flashsale.domain.model.item.enums.FlashItemStatus;
import com.actionworks.flashsale.domain.service.FlashActivityDomainService;
import com.actionworks.flashsale.domain.service.FlashItemDomainService;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.time.LocalDateTime;

import static com.actionworks.flashsale.application.exception.AppErrorCode.ACTIVITY_NOT_EXIST;
import static com.actionworks.flashsale.application.exception.AppErrorCode.INVALID_PARAMS;

@Slf4j
@Service
public class DefaultFlashItemAppService implements FlashItemAppService {

    @Resource
    private FlashActivityDomainService flashActivityDomainService;
    @Resource
    private FlashItemDomainService flashItemDomainService;
    @Resource
    private CacheService<FlashItem> cacheService;
    @Resource
    private ItemStockCacheService itemStockCacheService;

    @Override
    public <T> AppResult<T> publishFlashItem(Long activityId, FlashItemPublishCommand command) {
        log.info("flashItemPublish|发布秒杀商品|{}|{}", activityId, JSON.toJSONString(command));

        // 参数校验
        checkPublishParams(activityId, command);

        FlashItem flashItem = FlashItemAppConvertor.toDomain(command);
        flashItem.setActivityId(activityId);

        flashItemDomainService.publishFlashItem(flashItem);
        log.info("itemPublish|秒杀品已发布");

        return AppResult.success();
    }

    /**
     * 校验发布商品的参数
     */
    private void checkPublishParams(Long activityId, FlashItemPublishCommand command) {
        if (publishParamsIsNull(command)) {
            throw new BizException(INVALID_PARAMS);
        }

        // 校验库存参数
        if (command.getInitialStock() < 0 || command.getAvailableStock() < 0
                || command.getInitialStock() < command.getAvailableStock()) {
            throw new BizException(INVALID_PARAMS);
        }

        // 校验秒杀活动
        FlashActivity flashActivity = flashActivityDomainService.getFlashActivity(activityId);
        if (flashActivity == null) {
            throw new BizException(ACTIVITY_NOT_EXIST);
        }
    }

    /**
     * 发布秒杀商品的必填参数中有空即返回TRUE
     */
    private boolean publishParamsIsNull(FlashItemPublishCommand command) {
        return command == null || command.getInitialStock() == null || command.getAvailableStock() == null
                || command.getItemTitle() == null || command.getOriginalPrice() == null || command.getFlashPrice() == null;
    }

    @Override
    public <T> AppResult<T> onlineFlashItem(Long itemId) {
        log.info("onlineFlashItem|上线秒杀商品|{}", itemId);

        flashItemDomainService.onlineFlashItem(itemId);

        return AppResult.success();
    }

    @Override
    public <T> AppResult<T> offlineFlashItem(Long itemId) {
        log.info("offlineFlashItem|下线秒杀商品|{}", itemId);

        flashItemDomainService.offlineFlashItem(itemId);

        return AppResult.success();
    }

    @Override
    public boolean isAllowPlaceOrderOrNot(Long itemId) {
        FlashItem flashItem = cacheService.getCache(CacheConstants.FLASH_ITEM_SINGLE_CACHE_PREFIX, itemId);

        if (flashItem == null) {
            log.error("isAllowPlaceOrderOrNot|秒杀商品不存在");
            return false;
        }
        if (!FlashItemStatus.ONLINE.getCode().equals(flashItem.getStatus())) {
            log.error("isAllowPlaceOrderOrNot|秒杀商品未上线");
            return false;
        }
        if (LocalDateTime.now().isAfter(flashItem.getEndTime())
                || LocalDateTime.now().isBefore(flashItem.getStartTime())) {
            log.error("isAllowPlaceOrderOrNot|未在秒杀活动时间");
            return false;
        }

        return true;
    }
}
