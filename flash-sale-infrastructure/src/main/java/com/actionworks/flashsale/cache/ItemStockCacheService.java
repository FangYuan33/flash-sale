package com.actionworks.flashsale.cache;

/**
 * 商品库存缓存服务
 *
 * @author fangyuan
 */
public interface ItemStockCacheService {

    /**
     * 校正商品库存缓存
     *
     * @param itemId 商品ID
     */
    boolean alignItemStocks(Long itemId);
}
