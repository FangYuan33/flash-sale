package com.actionworks.flashsale.infrastructure.persistence.repository;

import com.actionworks.flashsale.domain.model.activity.aggregate.FlashActivity;
import com.actionworks.flashsale.domain.model.query.FlashActivityQueryCondition;
import com.actionworks.flashsale.domain.repository.FlashActivityRepository;
import com.actionworks.flashsale.infrastructure.exception.RepositoryException;
import com.actionworks.flashsale.infrastructure.persistence.convertor.FlashActivityConvertor;
import com.actionworks.flashsale.infrastructure.persistence.mapper.FlashActivityMapper;
import com.actionworks.flashsale.infrastructure.persistence.model.FlashActivityDO;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.actionworks.flashsale.infrastructure.exception.RepositoryErrorCode.ID_NOT_EXIST;

@Repository
public class FlashActivityRepositoryImpl implements FlashActivityRepository {

    @Resource
    private FlashActivityMapper flashActivityMapper;

    @Override
    public void save(FlashActivity flashActivity) {
        FlashActivityDO flashActivityDO = FlashActivityConvertor.toDataObjectForCreate(flashActivity);

        flashActivityMapper.insert(flashActivityDO);
        flashActivity.setId(flashActivityDO.getId());
    }

    @Override
    public Optional<FlashActivity> getById(Long activityId) {
        FlashActivityDO flashActivity = flashActivityMapper.selectById(activityId);

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

    @Override
    public Optional<List<FlashActivity>> listByQueryCondition(FlashActivityQueryCondition queryCondition) {
        // stream DO转 DOMAIN层对象
        List<FlashActivity> result = flashActivityMapper.listByQueryCondition(queryCondition)
                .stream().map(FlashActivityConvertor::toDomainObject).collect(Collectors.toList());

        return Optional.of(result);
    }

    @Override
    public Integer countByQueryCondition(FlashActivityQueryCondition queryCondition) {
        return flashActivityMapper.countByQueryCondition(queryCondition);
    }
}
