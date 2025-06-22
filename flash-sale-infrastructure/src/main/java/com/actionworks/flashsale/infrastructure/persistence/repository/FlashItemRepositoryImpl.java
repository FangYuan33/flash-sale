package com.actionworks.flashsale.infrastructure.persistence.repository;

import com.actionworks.flashsale.domain.model.aggregate.FlashItem;
import com.actionworks.flashsale.domain.repository.FlashItemRepository;
import com.actionworks.flashsale.infrastructure.persistence.convertor.FlashItemConvertor;
import com.actionworks.flashsale.infrastructure.persistence.convertor.StockConvertor;
import com.actionworks.flashsale.infrastructure.persistence.mapper.FlashItemMapper;
import com.actionworks.flashsale.infrastructure.persistence.mapper.StockMapper;
import com.actionworks.flashsale.infrastructure.persistence.model.FlashItemPO;
import com.actionworks.flashsale.infrastructure.persistence.model.StockPO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Optional;

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
    @Transactional(rollbackFor = Exception.class)
    public void save(FlashItem flashItem) {
        FlashItemPO flashItemPO = FlashItemConvertor.toPersistentObject(flashItem);
        flashItemMapper.insert(flashItemPO);

        StockPO stockPO = StockConvertor.toPersistentObject(flashItem.getStock());
        stockMapper.insert(stockPO);
    }

    @Override
    public void modifyStatus(FlashItem flashItem) {
        LambdaUpdateWrapper<FlashItemPO> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(FlashItemPO::getStatus, flashItem.getStatus().getCode())
                .eq(FlashItemPO::getCode, flashItem.getCode());

        flashItemMapper.update(updateWrapper);
    }

    @Override
    public FlashItem findByCode(String code) {
        LambdaQueryWrapper<FlashItemPO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(FlashItemPO::getCode, code);
        FlashItemPO flashItemPO = flashItemMapper.selectOne(queryWrapper);

        LambdaQueryWrapper<StockPO> stockPOLambdaQueryWrapper = new LambdaQueryWrapper<>();
        stockPOLambdaQueryWrapper.eq(StockPO::getCode, code);
        StockPO stockPO = stockMapper.selectOne(stockPOLambdaQueryWrapper);

        if (stockPO != null) {
            return FlashItemConvertor.toDomainObject(flashItemPO, stockPO);
        } else {
            return FlashItemConvertor.toDomainObject(flashItemPO);
        }
    }

    @Override
    public boolean deduct(String itemCode, Integer quantity) {
        return stockMapper.deduct(itemCode, quantity) <= 0;
    }
}
