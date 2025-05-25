package com.actionworks.flashsale.infrastructure.persistence.repository;

import com.actionworks.flashsale.domain.model.item.aggregate.FlashItem;
import com.actionworks.flashsale.domain.model.query.FlashItemQueryCondition;
import com.actionworks.flashsale.domain.repository.FlashItemRepository;
import com.actionworks.flashsale.infrastructure.persistence.convertor.FlashItemConvertor;
import com.actionworks.flashsale.infrastructure.persistence.convertor.StockConvertor;
import com.actionworks.flashsale.infrastructure.persistence.mapper.FlashItemMapper;
import com.actionworks.flashsale.infrastructure.persistence.mapper.StockMapper;
import com.actionworks.flashsale.infrastructure.persistence.model.FlashItemPO;
import com.actionworks.flashsale.infrastructure.persistence.model.StockPO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.collections4.*;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class FlashItemRepositoryImpl implements FlashItemRepository {

    @Resource
    private FlashItemMapper flashItemMapper;
    @Resource
    private StockMapper stockMapper;

    @Override
    public Optional<FlashItem> findById(Long itemId) {
        FlashItemPO flashItemDO = flashItemMapper.selectById(itemId);

        if (flashItemDO == null) {
            return Optional.empty();
        }
        LambdaQueryWrapper<StockPO> stockPOLambdaQueryWrapper = new LambdaQueryWrapper<>();
        stockPOLambdaQueryWrapper.eq(StockPO::getCode, flashItemDO.getCode());
        StockPO stockPO = stockMapper.selectOne(stockPOLambdaQueryWrapper);

        if (stockPO != null) {
            return Optional.of(FlashItemConvertor.toDomainObject(flashItemDO, stockPO));
        } else {
            return Optional.of(FlashItemConvertor.toDomainObject(flashItemDO));
        }
    }

    @Override
    public List<FlashItem> findByCondition(FlashItemQueryCondition condition) {
        LambdaQueryWrapper<FlashItemPO> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(condition.getCode())) {
            queryWrapper.eq(FlashItemPO::getCode, condition.getCode());
        }
        if (StringUtils.isNotBlank(condition.getItemTitle())) {
            queryWrapper.eq(FlashItemPO::getItemTitle, condition.getItemTitle());
        }
        if (condition.getStatus() != null) {
            queryWrapper.eq(FlashItemPO::getStatus, condition.getStatus());
        }

        List<FlashItemPO> flashItemPOList = flashItemMapper.selectList(queryWrapper);

        return CollectionUtils.isEmpty(flashItemPOList) ? Collections.emptyList() :
                flashItemPOList.stream().map(FlashItemConvertor::toDomainObject).collect(Collectors.toList());
    }

    @Override
    public void save(FlashItem flashItem) {
        FlashItemPO flashItemPO = FlashItemConvertor.toPersistentObject(flashItem);
        flashItemMapper.insert(flashItemPO);

        StockPO stockPO = StockConvertor.toPersistentObject(flashItem.getStock());
        stockPO.setCode(flashItemPO.getCode());
        stockMapper.insert(stockPO);
    }
}
