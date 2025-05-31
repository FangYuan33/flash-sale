package com.actionworks.flashsale.domain.service.impl;

import com.actionworks.flashsale.common.exception.DomainException;
import com.actionworks.flashsale.domain.adapter.CodeGenerateService;
import com.actionworks.flashsale.domain.model.aggregate.FlashActivity;
import com.actionworks.flashsale.domain.model.aggregate.FlashItem;
import com.actionworks.flashsale.domain.model.enums.FlashActivityStatus;
import com.actionworks.flashsale.domain.repository.FlashActivityRepository;
import com.actionworks.flashsale.domain.repository.FlashItemRepository;
import com.actionworks.flashsale.domain.service.FlashActivityDomainService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class FlashActivityDomainServiceImpl implements FlashActivityDomainService {

    @Resource
    private CodeGenerateService codeGenerateService;
    @Resource
    private FlashActivityRepository flashActivityRepository;
    @Resource
    private FlashItemRepository flashItemRepository;

    @Override
    public void publish(FlashActivity flashActivity) {
        flashActivity.publish(codeGenerateService);

        String itemCode = flashActivity.getFlashItem().getCode();
        FlashItem flashItem = flashItemRepository.findByCode(itemCode);
        if (flashItem == null) {
            throw new DomainException("[发布秒杀活动] 品 " + itemCode + " 不存在");
        }

        flashActivityRepository.save(flashActivity);
    }

    @Override
    public void changeActivityStatus(String code, Integer status) {
        FlashActivity flashActivity = flashActivityRepository.findByCode(code)
                .orElseThrow(() -> new DomainException("[变更活动状态] 活动不存在，ID: " + code));

        flashActivity.changeStatus(FlashActivityStatus.parse(status));
        flashActivityRepository.modifyStatus(flashActivity);
        // 活动下线同步更新秒杀商品状态
        if (FlashActivityStatus.OFFLINE.getCode().equals(status)) {
            flashItemRepository.modifyStatus(flashActivity.getFlashItem());
        }
    }

    @Override
    public FlashActivity getById(Long activityId) {
        return flashActivityRepository.findById(activityId)
                .orElseThrow(() -> new DomainException("[查询活动] 活动不存在，ID: " + activityId));
    }
}
