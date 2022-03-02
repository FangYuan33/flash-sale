package com.actionworks.flashsale.persistence.repository;

import com.actionworks.flashsale.domain.model.entity.FlashItem;
import com.actionworks.flashsale.domain.repository.FlashItemRepository;
import com.actionworks.flashsale.persistence.convertor.FlashItemConvertor;
import com.actionworks.flashsale.persistence.mapper.FlashItemMapper;
import com.actionworks.flashsale.persistence.model.FlashItemDO;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

@Repository
public class FlashItemRepositoryImpl implements FlashItemRepository {

    @Resource
    private FlashItemMapper flashItemMapper;

    @Override
    public void save(FlashItem flashItem) {
        FlashItemDO flashItemDO = FlashItemConvertor.toDataObject(flashItem);

        flashItemMapper.insert(flashItemDO);
    }
}
