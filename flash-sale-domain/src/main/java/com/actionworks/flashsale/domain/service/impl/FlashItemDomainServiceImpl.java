package com.actionworks.flashsale.domain.service.impl;

import com.actionworks.flashsale.domain.exception.DomainException;
import com.actionworks.flashsale.domain.model.entity.FlashItem;
import com.actionworks.flashsale.domain.model.enums.FlashItemStatus;
import com.actionworks.flashsale.domain.model.query.FlashItemQueryCondition;
import com.actionworks.flashsale.domain.model.query.PageResult;
import com.actionworks.flashsale.domain.repository.FlashItemRepository;
import com.actionworks.flashsale.domain.service.FlashItemDomainService;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.actionworks.flashsale.domain.exception.DomainErrorCode.FLASH_ITEM_NOT_EXIST;
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

    @Override
    public void onlineFlashItem(Long itemId) {
        FlashItem flashItem = getFlashItemById(itemId);

        // 状态校验，若为上线直接返回
        if (FlashItemStatus.ONLINE.getCode().equals(flashItem.getStatus())) {
            return;
        }

        // 更新状态为已上线
        flashItem.setStatus(FlashItemStatus.ONLINE.getCode());
        flashItemRepository.updateById(flashItem);
        log.info("onlineFlashItem|秒杀商品已上线|{}", JSON.toJSONString(flashItem));
    }

    @Override
    public void offlineFlashItem(Long itemId) {
        FlashItem flashItem = getFlashItemById(itemId);

        // 状态校验，若为已下线直接返回
        if (FlashItemStatus.OFFLINE.getCode().equals(flashItem.getStatus())) {
            return;
        }

        // 更新结果为已下线
        flashItem.setStatus(FlashItemStatus.OFFLINE.getCode());
        flashItemRepository.updateById(flashItem);
        log.info("offlineFlashItem|秒杀商品已下线|{}", JSON.toJSONString(flashItem));
    }

    @Override
    public FlashItem getById(Long itemId) {
        return getFlashItemById(itemId);
    }

    private FlashItem getFlashItemById(Long itemId) {
        Optional<FlashItem> flashItemOptional = flashItemRepository.getById(itemId);

        // 无对应的秒杀商品则抛出业务异常
        return flashItemOptional.orElseThrow(() -> new DomainException(FLASH_ITEM_NOT_EXIST));
    }

    @Override
    @SuppressWarnings("unchecked")
    public PageResult<FlashItem> listByQueryCondition(FlashItemQueryCondition queryCondition) {
        // 校正分页参数
        queryCondition.buildParams();

        Optional<List<FlashItem>> flashItems = flashItemRepository.listByQueryCondition(queryCondition);
        int count = flashItemRepository.countByQueryCondition(queryCondition);

        return PageResult.with(flashItems.orElse(Collections.EMPTY_LIST), count);
    }
}
