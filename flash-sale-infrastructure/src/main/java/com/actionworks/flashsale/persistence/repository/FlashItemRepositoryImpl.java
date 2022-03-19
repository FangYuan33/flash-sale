package com.actionworks.flashsale.persistence.repository;

import com.actionworks.flashsale.domain.model.entity.FlashItem;
import com.actionworks.flashsale.domain.model.query.FlashItemQueryCondition;
import com.actionworks.flashsale.domain.repository.FlashItemRepository;
import com.actionworks.flashsale.exception.RepositoryException;
import com.actionworks.flashsale.persistence.convertor.FlashItemConvertor;
import com.actionworks.flashsale.persistence.mapper.FlashItemMapper;
import com.actionworks.flashsale.persistence.model.FlashItemDO;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.actionworks.flashsale.exception.RepositoryErrorCode.ID_NOT_EXIST;

@Repository
public class FlashItemRepositoryImpl implements FlashItemRepository {

    @Resource
    private FlashItemMapper flashItemMapper;

    @Override
    public void save(FlashItem flashItem) {
        FlashItemDO flashItemDO = FlashItemConvertor.toDataObject(flashItem);

        flashItemMapper.insert(flashItemDO);
        flashItem.setId(flashItemDO.getId());
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

    @Override
    public Optional<List<FlashItem>> listByQueryCondition(FlashItemQueryCondition queryCondition) {
        List<FlashItemDO> flashItemDOList = flashItemMapper.listByQueryCondition(queryCondition);

        if (flashItemDOList.isEmpty()) {
            return Optional.empty();
        }

        // stream 转换对象类型
        List<FlashItem> flashItems = flashItemDOList.stream()
                .map(FlashItemConvertor::toDomainObject).collect(Collectors.toList());

        return Optional.of(flashItems);
    }

    @Override
    public List<FlashItem> listByQueryConditionWithoutPageSize(FlashItemQueryCondition queryCondition) {
        List<FlashItemDO> flashItemDOList = flashItemMapper.listByQueryConditionWithoutPageSize(queryCondition);

        // stream 转换对象类型
        return flashItemDOList.stream().map(FlashItemConvertor::toDomainObject).collect(Collectors.toList());
    }

    @Override
    public Integer countByQueryCondition(FlashItemQueryCondition queryCondition) {
        return flashItemMapper.countByQueryCondition(queryCondition);
    }

    @Override
    public boolean decreaseItemStock(Long itemId, Integer quantity) {
        return flashItemMapper.decreaseItemStock(itemId, quantity) == 1;
    }

    @Override
    public boolean increaseItemStock(Long itemId, Integer quantity) {
        return flashItemMapper.increaseItemStock(itemId, quantity) == 1;
    }
}
