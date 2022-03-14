package com.actionworks.flashsale.domain.service.impl;

import com.actionworks.flashsale.domain.event.DomainEventPublisher;
import com.actionworks.flashsale.domain.event.entity.FlashActivityEvent;
import com.actionworks.flashsale.domain.exception.DomainException;
import com.actionworks.flashsale.domain.model.entity.FlashActivity;
import com.actionworks.flashsale.domain.model.query.FlashActivityQueryCondition;
import com.actionworks.flashsale.domain.model.query.PageResult;
import com.actionworks.flashsale.domain.repository.FlashActivityRepository;
import com.actionworks.flashsale.domain.service.FlashActivityDomainService;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.actionworks.flashsale.domain.event.enums.ActivityEventType.PUBLISH;
import static com.actionworks.flashsale.domain.exception.DomainErrorCode.*;
import static com.actionworks.flashsale.domain.model.enums.FlashActivityStatus.*;

@Slf4j
@Service
public class FlashActivityDomainServiceImpl implements FlashActivityDomainService {

    @Resource
    private FlashActivityRepository flashActivityRepository;
    @Resource
    private DomainEventPublisher domainEventPublisher;

    @Override
    public void publishActivity(FlashActivity flashActivity) {
        if (flashActivity == null || !flashActivity.validateParamsForCreate()) {
            throw new DomainException(PUBLISH_FLASH_ACTIVITY_PARAMS_INVALID);
        }

        // 状态为已发布
        flashActivityRepository.save(flashActivity.setStatus(PUBLISHED.getCode()));
        log.info("activityPublish|活动已发布|{}", JSON.toJSONString(flashActivity));

        // 秒杀活动发布事件，更新缓存
        domainEventPublisher.publish(new FlashActivityEvent(flashActivity.getId(), PUBLISH));
    }

    @Override
    public void onlineActivity(Long activityId) {
        FlashActivity flashActivity = getActivityById(activityId);
        if (ONLINE.getCode().equals(flashActivity.getStatus())) {
            return;
        }

        // 更改状态为已上线
        flashActivityRepository.updateById(flashActivity.setStatus(ONLINE.getCode()));
        log.info("activityOnline|活动已上线|{}", activityId);
    }

    @Override
    public void offlineActivity(Long activityId) {
        FlashActivity flashActivity = getActivityById(activityId);

        if (OFFLINE.getCode().equals(flashActivity.getStatus())) {
            return;
        }
        if (!ONLINE.getCode().equals(flashActivity.getStatus())) {
            throw new DomainException(OFFLINE_FLASH_ACTIVITY_FORBIDDEN);
        }

        // 更新状态为已下线
        flashActivityRepository.updateById(flashActivity.setStatus(OFFLINE.getCode()));
        log.info("activityOffline|活动已下线|{}", activityId);
    }

    @Override
    public FlashActivity getFlashActivity(Long activityId) {
        return getActivityById(activityId);
    }

    private FlashActivity getActivityById(Long activityId) {
        if (activityId == null) {
            throw new DomainException(PARAMS_INVALID);
        }

        Optional<FlashActivity> flashActivityOptional = flashActivityRepository.getById(activityId);

        // orElseThrow 要么返回非空的值，否则抛出异常
        return flashActivityOptional.orElseThrow(() -> new DomainException(FLASH_ACTIVITY_NOT_EXIST));
    }

    @Override
    @SuppressWarnings("unchecked")
    public PageResult<FlashActivity> listByQueryCondition(FlashActivityQueryCondition flashActivityQueryCondition) {
        // 校正分页参数
        flashActivityQueryCondition.buildParams();

        // 查询实体对象和计数
        Optional<List<FlashActivity>> flashActivities =
                flashActivityRepository.listByQueryCondition(flashActivityQueryCondition);
        Integer count = flashActivityRepository.countByQueryCondition(flashActivityQueryCondition);

        return PageResult.with(flashActivities.orElse(Collections.EMPTY_LIST), count);
    }
}
