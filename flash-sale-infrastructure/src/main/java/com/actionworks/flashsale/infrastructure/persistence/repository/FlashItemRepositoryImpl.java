package com.actionworks.flashsale.infrastructure.persistence.repository;

import com.actionworks.flashsale.domain.model.item.aggregate.FlashItem;
import com.actionworks.flashsale.domain.repository.FlashItemRepository;
import com.actionworks.flashsale.infrastructure.persistence.convertor.FlashItemConvertor;
import com.actionworks.flashsale.infrastructure.persistence.mapper.FlashItemMapper;
import com.actionworks.flashsale.infrastructure.persistence.model.FlashItemPO;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Optional;

@Repository
public class FlashItemRepositoryImpl implements FlashItemRepository {

    @Resource
    private FlashItemMapper flashItemMapper;

    @Override
    public Optional<FlashItem> findById(Long itemId) {
        FlashItemPO flashItemDO = flashItemMapper.selectById(itemId);

        if (flashItemDO == null) {
            return Optional.empty();
        }

        return Optional.of(FlashItemConvertor.toDomainObject(flashItemDO));
    }

}
