package com.actionworks.flashsale.application.query.service.impl;

import com.actionworks.flashsale.application.assembler.FlashItemAppAssembler;
import com.actionworks.flashsale.application.convertor.FlashItemConvertor;
import com.actionworks.flashsale.application.query.model.dto.FlashItemDTO;
import com.actionworks.flashsale.application.query.model.req.FlashItemQuery;
import com.actionworks.flashsale.application.query.service.FlashItemAppQueryService;
import com.actionworks.flashsale.common.exception.DomainException;
import com.actionworks.flashsale.domain.model.aggregate.FlashItem;
import com.actionworks.flashsale.domain.model.query.FlashItemQueryCondition;
import com.actionworks.flashsale.domain.repository.FlashItemRepository;
import com.actionworks.flashsale.domain.service.FlashItemDomainService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.actionworks.flashsale.common.exception.DomainErrorCode.FLASH_ITEM_NOT_EXIST;

@Service
public class FlashItemAppQueryServiceImpl implements FlashItemAppQueryService {

    @Resource
    private FlashItemDomainService flashItemDomainService;
    @Resource
    private FlashItemRepository flashItemRepository;

    @Override
    public FlashItemDTO getById(Long itemId) {
        Optional<FlashItem> optional = flashItemRepository.findById(itemId);
        // 无对应的秒杀商品则抛出业务异常
        return FlashItemAppAssembler.toFlashItemDTO(optional.orElseThrow(() -> new DomainException(FLASH_ITEM_NOT_EXIST)));
    }

    @Override
    public List<FlashItemDTO> getFlashItems(FlashItemQuery query) {
        FlashItemQueryCondition condition = FlashItemConvertor.query2QueryCondition(query);
        List<FlashItem> itemList = flashItemRepository.findByCondition(condition);
        return itemList.stream().map(FlashItemAppAssembler::toFlashItemDTO).collect(Collectors.toList());
    }

}
