package com.actionworks.flashsale.app.service.activity;

import com.actionworks.flashsale.app.exception.BizException;
import com.actionworks.flashsale.app.model.convertor.FlashActivityAppConvertor;
import com.actionworks.flashsale.app.model.command.FlashActivityPublishCommand;
import com.actionworks.flashsale.app.model.dto.FlashActivityDTO;
import com.actionworks.flashsale.app.model.query.FlashActivitiesQuery;
import com.actionworks.flashsale.app.model.result.AppResult;
import com.actionworks.flashsale.cache.CacheService;
import com.actionworks.flashsale.cache.constants.CacheConstants;
import com.actionworks.flashsale.domain.model.entity.FlashActivity;
import com.actionworks.flashsale.domain.model.enums.FlashActivityStatus;
import com.actionworks.flashsale.domain.model.query.FlashActivityQueryCondition;
import com.actionworks.flashsale.domain.service.FlashActivityDomainService;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.actionworks.flashsale.app.exception.AppErrorCode.INVALID_PARAMS;

@Slf4j
@Service
public class DefaultActivityAppService implements FlashActivityAppService {

    @Resource
    private FlashActivityDomainService flashActivityDomainService;
    @Resource
    private CacheService<FlashActivity> cacheService;

    @Override
    public <T> AppResult<T> publishFlashActivity(FlashActivityPublishCommand activityPublishCommand) {
        log.info("activityPublish|发布秒杀活动|{}", JSON.toJSONString(activityPublishCommand));

        if (activityPublishCommand == null) {
            throw new BizException(INVALID_PARAMS);
        }

        flashActivityDomainService.publishActivity(FlashActivityAppConvertor.toDomain(activityPublishCommand));

        return AppResult.success();
    }

    @Override
    public <T> AppResult<T> onlineFlashActivity(Long activityId) {
        log.info("activityOnline|上线秒杀活动|{}", activityId);

        flashActivityDomainService.onlineActivity(activityId);

        return AppResult.success();
    }

    @Override
    public <T> AppResult<T> offlineFlashActivity(Long activityId) {
        log.info("activityOffline|下线秒杀活动|{}", activityId);

        flashActivityDomainService.offlineActivity(activityId);

        return AppResult.success();
    }

    @Override
    @SuppressWarnings("unchecked")
    public AppResult<FlashActivityDTO> getFlashActivity(Long activityId) {
        FlashActivity flashActivity = cacheService.getCache(CacheConstants.FLASH_ACTIVITY_SINGLE_CACHE_PREFIX, activityId);

        return AppResult.success(FlashActivityAppConvertor.toFlashActivityDTO(flashActivity));
    }

    @Override
    @SuppressWarnings("unchecked")
    public AppResult<List<FlashActivityDTO>> getFlashActivities(FlashActivitiesQuery flashActivitiesQuery) {
        FlashActivityQueryCondition flashActivityQueryCondition =
                FlashActivityAppConvertor.toFlashActivityQueryCondition(flashActivitiesQuery);

        List<FlashActivity> caches = cacheService
                .getCaches(CacheConstants.FLASH_ACTIVITY_CACHE_LIST_PREFIX, flashActivityQueryCondition);

        // stream 完成对象转换
        List<FlashActivityDTO> result = caches.stream()
                .map(FlashActivityAppConvertor::toFlashActivityDTO).collect(Collectors.toList());

        return AppResult.success(result);
    }

    @Override
    public boolean isAllowPlaceOrderOrNot(Long activityId) {
        FlashActivity flashActivity = cacheService.getCache(CacheConstants.FLASH_ACTIVITY_SINGLE_CACHE_PREFIX, activityId);

        if (flashActivity == null) {
            log.error("isAllowPlaceOrderOrNot|秒杀活动不存在");
            return false;
        }
        if (!FlashActivityStatus.ONLINE.getCode().equals(flashActivity.getStatus())) {
            log.error("isAllowPlaceOrderOrNot|秒杀活动未上线");
            return false;
        }
        if (LocalDateTime.now().isAfter(flashActivity.getEndTime())
                || LocalDateTime.now().isBefore(flashActivity.getStartTime())) {
            log.error("isAllowPlaceOrderOrNot|未在秒杀活动时间");
            return false;
        }

        return true;
    }
}
