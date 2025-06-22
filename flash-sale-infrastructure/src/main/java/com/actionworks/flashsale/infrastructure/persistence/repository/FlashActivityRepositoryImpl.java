package com.actionworks.flashsale.infrastructure.persistence.repository;

import com.actionworks.flashsale.domain.model.aggregate.FlashActivity;
import com.actionworks.flashsale.domain.model.aggregate.FlashItem;
import com.actionworks.flashsale.domain.repository.FlashActivityRepository;
import com.actionworks.flashsale.domain.repository.FlashItemRepository;
import com.actionworks.flashsale.infrastructure.persistence.convertor.FlashActivityConvertor;
import com.actionworks.flashsale.infrastructure.persistence.mapper.FlashActivityMapper;
import com.actionworks.flashsale.infrastructure.persistence.model.FlashActivityPO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Optional;

@Repository
public class FlashActivityRepositoryImpl implements FlashActivityRepository {

    @Resource
    private FlashActivityMapper flashActivityMapper;
    @Resource
    private FlashItemRepository flashItemRepository;

    @Override
    public Optional<FlashActivity> findById(Long activityId) {
        return Optional.empty();
    }

    @Override
    public void save(FlashActivity flashActivity) {
        FlashActivityPO flashActivityPO = FlashActivityConvertor.toPersistentObject(flashActivity);
        flashActivityMapper.insert(flashActivityPO);
    }

    @Override
    public void modifyStatus(FlashActivity flashActivity) {
        LambdaUpdateWrapper<FlashActivityPO> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(FlashActivityPO::getStatus, flashActivity.getStatus().getCode())
                .eq(FlashActivityPO::getCode, flashActivity.getCode());

        flashActivityMapper.update(updateWrapper);
    }

    @Override
    public Optional<FlashActivity> findByCode(String code) {
        LambdaQueryWrapper<FlashActivityPO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(FlashActivityPO::getCode, code);

        FlashActivityPO flashActivityPO = flashActivityMapper.selectOne(queryWrapper);
        if (flashActivityPO != null) {
            FlashItem flashItem = flashItemRepository.findByCode(flashActivityPO.getItemCode());

            return Optional.of(FlashActivityConvertor.toDomainObject(flashActivityPO, flashItem));
        } else {
            return Optional.empty();
        }
    }
}
