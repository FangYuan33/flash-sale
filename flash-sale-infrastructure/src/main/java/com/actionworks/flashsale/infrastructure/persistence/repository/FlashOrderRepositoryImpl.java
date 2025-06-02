package com.actionworks.flashsale.infrastructure.persistence.repository;

import com.actionworks.flashsale.domain.model.aggregate.FlashOrder;
import com.actionworks.flashsale.domain.repository.FlashOrderRepository;
import com.actionworks.flashsale.infrastructure.persistence.convertor.FlashOrderConvertor;
import com.actionworks.flashsale.infrastructure.persistence.mapper.FlashOrderMapper;
import com.actionworks.flashsale.infrastructure.persistence.model.FlashOrderPO;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Optional;

@Repository
public class FlashOrderRepositoryImpl implements FlashOrderRepository {

    @Resource
    private FlashOrderMapper flashOrderMapper;

    @Override
    public void save(FlashOrder flashOrder) {
        FlashOrderPO flashOrderPO = FlashOrderConvertor.toFlashOrderPO(flashOrder);
        flashOrderMapper.insert(flashOrderPO);
    }

    @Override
    public Optional<FlashOrder> findByCode(String code) {
        FlashOrderPO flashOrderPO = flashOrderMapper.selectByCode(code);
        if (flashOrderPO == null) {
            return Optional.empty();
        }

        return Optional.of(FlashOrderConvertor.toFlashOrder(flashOrderPO));
    }
}
