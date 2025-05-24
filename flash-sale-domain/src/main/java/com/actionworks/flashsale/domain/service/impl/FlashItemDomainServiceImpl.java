package com.actionworks.flashsale.domain.service.impl;

import com.actionworks.flashsale.common.exception.DomainException;
import com.actionworks.flashsale.domain.model.item.aggregate.FlashItem;
import com.actionworks.flashsale.domain.repository.FlashItemRepository;
import com.actionworks.flashsale.domain.service.FlashItemDomainService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Optional;

import static com.actionworks.flashsale.common.exception.DomainErrorCode.FLASH_ITEM_NOT_EXIST;

@Slf4j
@Service
public class FlashItemDomainServiceImpl implements FlashItemDomainService {

    @Resource
    private FlashItemRepository flashItemRepository;

    @Override
    public FlashItem getById(Long itemId) {
        return getFlashItemById(itemId);
    }

    private FlashItem getFlashItemById(Long itemId) {
        Optional<FlashItem> flashItemOptional = flashItemRepository.findById(itemId);
        // 无对应的秒杀商品则抛出业务异常
        return flashItemOptional.orElseThrow(() -> new DomainException(FLASH_ITEM_NOT_EXIST));
    }

}
