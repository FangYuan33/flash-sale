package com.actionworks.flashsale.domain.service.impl;

import com.actionworks.flashsale.domain.exception.DomainException;
import com.actionworks.flashsale.domain.model.entity.FlashActivity;
import com.actionworks.flashsale.domain.repository.FlashActivityRepository;
import com.actionworks.flashsale.domain.service.FlashActivityDomainService;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.Optional;

import static com.actionworks.flashsale.domain.exception.DomainErrorCode.FLASH_ACTIVITY_NOT_EXIST;
import static com.actionworks.flashsale.domain.exception.DomainErrorCode.PARAMS_INVALID;
import static com.actionworks.flashsale.domain.model.enums.FlashActivityStatus.ONLINE;
import static com.actionworks.flashsale.domain.model.enums.FlashActivityStatus.PUBLISHED;

@Slf4j
@Service
public class FlashActivityDomainServiceImpl implements FlashActivityDomainService {

    @Resource
    private FlashActivityRepository flashActivityRepository;

    @Override
    public void publishActivity(FlashActivity flashActivity) {
        if (flashActivity == null || !flashActivity.validateParamsForCreate()) {
            throw new DomainException(PARAMS_INVALID);
        }

        // 状态为已发布
        flashActivityRepository.save(flashActivity.setStatus(PUBLISHED.getCode()));
        log.info("activityPublish|活动已发布|{}", JSON.toJSONString(flashActivity));
    }

    @Override
    public void onlineActivity(Long activityId) {
        if (activityId == null) {
            throw new DomainException(PARAMS_INVALID);
        }

        Optional<FlashActivity> flashActivityOptional = flashActivityRepository.findById(activityId);

        if (!flashActivityOptional.isPresent()) {
            throw new DomainException(FLASH_ACTIVITY_NOT_EXIST);
        }

        FlashActivity flashActivity = flashActivityOptional.get();
        if (ONLINE.getCode().equals(flashActivity.getStatus())) {
            return;
        }

        // 状态为已上线
        flashActivityRepository.updateById(flashActivity.setStatus(ONLINE.getCode()));
        log.info("activityOnline|活动已上线|{}", activityId);
    }
}
