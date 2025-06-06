package com.actionworks.flashsale.infrastructure.persistence.repository;

import com.actionworks.flashsale.domain.model.order.aggregate.FlashOrder;
import com.actionworks.flashsale.domain.model.query.FlashOrderQueryCondition;
import com.actionworks.flashsale.domain.repository.FlashOrderRepository;
import com.actionworks.flashsale.infrastructure.exception.RepositoryException;
import com.actionworks.flashsale.infrastructure.persistence.convertor.FlashOrderConvertor;
import com.actionworks.flashsale.infrastructure.persistence.mapper.FlashOrderMapper;
import com.actionworks.flashsale.infrastructure.persistence.model.FlashOrderDO;
import com.actionworks.flashsale.infrastructure.utils.SnowflakeIdUtil;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.actionworks.flashsale.infrastructure.exception.RepositoryErrorCode.ID_NOT_EXIST;

@Repository
public class FlashOrderRepositoryImpl implements FlashOrderRepository {

    @Resource
    private FlashOrderMapper flashOrderMapper;

    @Override
    public void save(FlashOrder flashOrder) {
        // 雪花算法生成ID
        flashOrder.setId(SnowflakeIdUtil.nextId());
        FlashOrderDO flashOrderDO = FlashOrderConvertor.toDataObject(flashOrder);

        flashOrderMapper.insert(flashOrderDO);
    }

    @Override
    public Optional<FlashOrder> getById(Long orderId) {
        FlashOrderDO flashOrderDO = flashOrderMapper.selectById(orderId);
        FlashOrder flashOrder = FlashOrderConvertor.toDomainObject(flashOrderDO);

        return Optional.ofNullable(flashOrder);
    }

    @Override
    public boolean updateById(FlashOrder flashOrder) {
        FlashOrderDO flashOrderDO = FlashOrderConvertor.toDataObject(flashOrder);
        if (flashOrderDO.getId() == null) {
            throw new RepositoryException(ID_NOT_EXIST);
        }

        return flashOrderMapper.updateById(flashOrderDO) == 1;
    }

    @Override
    public Optional<List<FlashOrder>> listByQueryCondition(FlashOrderQueryCondition queryCondition) {
        List<FlashOrderDO> orderDOList = flashOrderMapper.listByQueryCondition(queryCondition);

        // stream 转换出参类型
        List<FlashOrder> flashOrders = orderDOList.stream()
                .map(FlashOrderConvertor::toDomainObject).collect(Collectors.toList());

        return Optional.of(flashOrders);
    }

    @Override
    public Integer countByQueryCondition(FlashOrderQueryCondition queryCondition) {
        return flashOrderMapper.countByQueryCondition(queryCondition);
    }
}
