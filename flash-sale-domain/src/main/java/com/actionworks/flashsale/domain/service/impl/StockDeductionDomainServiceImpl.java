package com.actionworks.flashsale.domain.service.impl;

import com.actionworks.flashsale.domain.model.stock.StockDeduction;
import com.actionworks.flashsale.domain.repository.FlashItemRepository;
import com.actionworks.flashsale.domain.service.StockDeductionDomainService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class StockDeductionDomainServiceImpl implements StockDeductionDomainService {

    @Resource
    private FlashItemRepository flashItemRepository;

    @Override
    public boolean decreaseItemStock(StockDeduction stockDeduction) {
        return flashItemRepository.decreaseItemStock(stockDeduction.getItemId(), stockDeduction.getQuantity());
    }

    @Override
    public boolean increaseItemStock(StockDeduction stockDeduction) {
        return flashItemRepository.increaseItemStock(stockDeduction.getItemId(), stockDeduction.getQuantity());
    }
}
