package com.actionworks.flashsale.cache;

/**
 * 商品库存缓存服务
 *
 * @author fangyuan
 */
public interface ItemStockCacheService {

    /**
     * 初始化商品库存缓存
     *
     * @param itemId 商品ID
     */
    boolean initialItemStocks(Long itemId);

    /**
     * 获取商品的可用库存
     */
    Integer getAvailableItemStock(Long itemId);

    /**
     * 扣减库存
     *
     * @param itemNum 商品数量
     */
    boolean decreaseItemStock(Long itemId, Integer itemNum);

    /**
     * 增加库存
     */
    boolean increaseItemStock(Long itemId, Integer itemNum);
}
