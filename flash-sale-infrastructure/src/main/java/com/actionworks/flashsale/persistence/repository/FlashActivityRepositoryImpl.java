package com.actionworks.flashsale.persistence.repository;

import com.actionworks.flashsale.domain.model.entity.FlashActivity;
import com.actionworks.flashsale.domain.repository.FlashActivityRepository;
import com.actionworks.flashsale.persistence.convertor.FlashActivityConvertor;
import com.actionworks.flashsale.persistence.mapper.FlashActivityMapper;
import com.actionworks.flashsale.persistence.model.FlashActivityDO;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

@Repository
public class FlashActivityRepositoryImpl implements FlashActivityRepository {

    @Resource
    private FlashActivityMapper flashActivityMapper;

    @Override
    public int save(FlashActivity flashActivity) {
        FlashActivityDO flashActivityDO = FlashActivityConvertor.toDataObjectForCreate(flashActivity);

        return flashActivityMapper.insert(flashActivityDO);
    }
}
