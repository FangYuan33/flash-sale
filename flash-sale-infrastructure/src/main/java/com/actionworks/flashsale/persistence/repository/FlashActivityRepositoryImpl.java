package com.actionworks.flashsale.persistence.repository;

import com.actionworks.flashsale.domain.model.entity.FlashActivity;
import com.actionworks.flashsale.domain.repository.FlashActivityRepository;
import com.actionworks.flashsale.exception.RepositoryException;
import com.actionworks.flashsale.persistence.convertor.FlashActivityConvertor;
import com.actionworks.flashsale.persistence.mapper.FlashActivityMapper;
import com.actionworks.flashsale.persistence.model.FlashActivityDO;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Optional;

import static com.actionworks.flashsale.exception.RepositoryErrorCode.ID_NOT_EXIST;

@Repository
public class FlashActivityRepositoryImpl implements FlashActivityRepository {

    @Resource
    private FlashActivityMapper flashActivityMapper;

    @Override
    public void save(FlashActivity flashActivity) {
        FlashActivityDO flashActivityDO = FlashActivityConvertor.toDataObjectForCreate(flashActivity);

        flashActivityMapper.insert(flashActivityDO);
    }

    @Override
    public Optional<FlashActivity> findById(Long activityId) {
        FlashActivityDO flashActivity = flashActivityMapper.getById(activityId);

        if (flashActivity == null) {
            return Optional.empty();
        }

        return Optional.of(FlashActivityConvertor.toDomainObject(flashActivity));
    }

    @Override
    public void updateById(FlashActivity flashActivity) {
        if (flashActivity.getId() == null) {
            throw new RepositoryException(ID_NOT_EXIST);
        }

        FlashActivityDO flashActivityDO = FlashActivityConvertor.toDataObjectForCreate(flashActivity);

        flashActivityMapper.updateById(flashActivityDO);
    }
}
