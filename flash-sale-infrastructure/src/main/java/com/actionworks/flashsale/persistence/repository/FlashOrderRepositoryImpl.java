package com.actionworks.flashsale.persistence.repository;

import com.actionworks.flashsale.domain.repository.FlashOrderRepository;
import com.actionworks.flashsale.persistence.mapper.FlashOrderMapper;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

@Repository
public class FlashOrderRepositoryImpl implements FlashOrderRepository {

    @Resource
    private FlashOrderMapper flashOrderMapper;

}
