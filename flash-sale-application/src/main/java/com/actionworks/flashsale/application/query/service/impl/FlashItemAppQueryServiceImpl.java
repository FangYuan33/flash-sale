package com.actionworks.flashsale.application.query.service.impl;

import com.actionworks.flashsale.application.query.assembler.FlashItemAppAssembler;
import com.actionworks.flashsale.application.query.model.item.dto.FlashItemDTO;
import com.actionworks.flashsale.application.query.service.FlashItemAppQueryService;
import com.actionworks.flashsale.domain.model.item.aggregate.FlashItem;
import com.actionworks.flashsale.domain.service.FlashItemDomainService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class FlashItemAppQueryServiceImpl implements FlashItemAppQueryService {

    @Resource
    private FlashItemDomainService flashItemDomainService;

    @Override
    public FlashItemDTO getById(Long itemId) {
        FlashItem flashItem = flashItemDomainService.getById(itemId);
        return FlashItemAppAssembler.toFlashItemDTO(flashItem);
    }

}
