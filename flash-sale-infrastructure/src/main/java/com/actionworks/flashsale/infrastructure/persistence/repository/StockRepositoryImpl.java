package com.actionworks.flashsale.infrastructure.persistence.repository;

import com.actionworks.flashsale.domain.repository.StockRepository;
import com.actionworks.flashsale.infrastructure.persistence.mapper.StockMapper;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

@Repository
public class StockRepositoryImpl implements StockRepository {
    @Resource
    private StockMapper stockMapper;

    @Override
    public boolean deduct(String itemCode, Integer quantity) {
        return stockMapper.deduct(itemCode, quantity) <= 0;
    }
}
