package com.actionworks.flashsale.domain.service.impl;

import com.actionworks.flashsale.domain.exception.DomainException;
import com.actionworks.flashsale.domain.model.entity.FlashItem;
import com.actionworks.flashsale.domain.model.enums.FlashItemStatus;
import com.actionworks.flashsale.domain.repository.FlashItemRepository;
import com.actionworks.flashsale.domain.service.FlashItemDomainService;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import static com.actionworks.flashsale.domain.exception.DomainErrorCode.PUBLISH_FLASH_ITEM_PARAMS_INVALID;

@Slf4j
@Service
public class FlashItemDomainServiceImpl implements FlashItemDomainService {

    @Resource
    private FlashItemRepository flashItemRepository;

    @Override
    public void publishFlashItem(FlashItem flashItem) {
        if (flashItem == null || !flashItem.validateParamsForCreate()) {
            throw new DomainException(PUBLISH_FLASH_ITEM_PARAMS_INVALID);
        }

        // 状态为已发布
        flashItem.setStatus(FlashItemStatus.PUBLISHED.getCode());
        flashItemRepository.save(flashItem);
        log.info("activityPublish|秒杀商品已发布|{}", JSON.toJSONString(flashItem));
    }
}
