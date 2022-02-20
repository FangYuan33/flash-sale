package com.actionworks.flashsale.domain.service.impl;

import com.actionworks.flashsale.domain.exception.DomainException;
import com.actionworks.flashsale.domain.model.entity.FlashActivity;
import com.actionworks.flashsale.domain.repository.FlashActivityRepository;
import com.actionworks.flashsale.domain.service.FlashActivityDomainService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import static com.actionworks.flashsale.domain.exception.DomainErrorCode.PARAMS_INVALID;
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
        flashActivity.setStatus(PUBLISHED.getCode());
        flashActivityRepository.save(flashActivity);
        log.info("activityPublish|活动已发布");
    }
}
