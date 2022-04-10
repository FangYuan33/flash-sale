package com.actionworks.flashsale.cache;

/**
 * 下单许可Service
 */
public interface ItemPermissionCacheService {

    /**
     * 初始化商品库存下单许可
     *
     * @param itemId 商品ID
     */
    boolean initialItemPermission(Long itemId);

    /**
     * 扣减下单许可
     */
    boolean decreaseItemPermission(Long itemId);

    /**
     * 恢复下单许可
     */
    boolean recoverItemPermission(Long itemId);
}
