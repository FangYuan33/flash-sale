package com.actionworks.flashsale.persistence.repository;

import com.actionworks.flashsale.domain.model.entity.FlashOrder;
import com.actionworks.flashsale.domain.repository.FlashOrderRepository;
import com.actionworks.flashsale.persistence.convertor.FlashOrderConvertor;
import com.actionworks.flashsale.persistence.mapper.FlashOrderMapper;
import com.actionworks.flashsale.persistence.model.FlashOrderDO;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

@Repository
public class FlashOrderRepositoryImpl implements FlashOrderRepository {

    @Resource
    private FlashOrderMapper flashOrderMapper;

    @Override
    public void save(FlashOrder flashOrder) {
        FlashOrderDO flashOrderDO = FlashOrderConvertor.toDataObject(flashOrder);
        flashOrderMapper.insert(flashOrderDO);
    }
}
