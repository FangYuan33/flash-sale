package com.actionworks.flashsale.persistence.repository;

import com.actionworks.flashsale.domain.model.entity.FlashItem;
import com.actionworks.flashsale.domain.repository.FlashItemRepository;
import com.actionworks.flashsale.exception.RepositoryException;
import com.actionworks.flashsale.persistence.convertor.FlashItemConvertor;
import com.actionworks.flashsale.persistence.mapper.FlashItemMapper;
import com.actionworks.flashsale.persistence.model.FlashItemDO;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Optional;

import static com.actionworks.flashsale.exception.RepositoryErrorCode.ID_NOT_EXIST;

@Repository
public class FlashItemRepositoryImpl implements FlashItemRepository {

    @Resource
    private FlashItemMapper flashItemMapper;

    @Override
    public void save(FlashItem flashItem) {
        FlashItemDO flashItemDO = FlashItemConvertor.toDataObject(flashItem);

        flashItemMapper.insert(flashItemDO);
    }

    @Override
    public Optional<FlashItem> getById(Long itemId) {
        FlashItemDO flashItemDO = flashItemMapper.selectById(itemId);

        if (flashItemDO == null) {
            return Optional.empty();
        }

        return Optional.of(FlashItemConvertor.toDomainObject(flashItemDO));
    }

    @Override
    public void updateById(FlashItem flashItem) {
        if (flashItem.getId() == null) {
            throw new RepositoryException(ID_NOT_EXIST);
        }

        FlashItemDO flashItemDO = FlashItemConvertor.toDataObject(flashItem);

        flashItemMapper.updateById(flashItemDO);
    }
}
