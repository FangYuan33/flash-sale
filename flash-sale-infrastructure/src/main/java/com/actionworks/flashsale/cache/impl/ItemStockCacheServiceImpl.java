package com.actionworks.flashsale.cache.impl;

import com.actionworks.flashsale.cache.ItemStockCacheService;
import org.springframework.stereotype.Service;

@Service
public class ItemStockCacheServiceImpl implements ItemStockCacheService {

    @Override
    public boolean alignItemStocks(Long itemId) {
        return false;
    }
}
