package com.actionworks.flashsale.cache;

/**
 * 秒杀许可
 */
public interface ItemPermissionCacheService {

    /**
     * 初始化商品库存秒杀许可
     *
     * @param itemId 商品ID
     */
    boolean initialItemPermission(Long itemId);

    /**
     * 扣减秒杀许可
     */
    boolean decreaseItemPermission(Long itemId);

    /**
     * 恢复秒杀许可
     */
    boolean recoverItemPermission(Long itemId);
}
