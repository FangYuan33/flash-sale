package com.actionworks.flashsale.cache.constants;

/**
 * 缓存相关的一些通用常量
 */
public class CacheConstants {
    /**
     * 秒杀活动单个缓存key前缀
     */
    public static final String FLASH_ACTIVITY_SINGLE_CACHE_PREFIX = "FLASH_ACTIVITY_SINGLE_CACHE_%d";

    /**
     * 秒杀商品单个缓存key前缀
     */
    public static final String FLASH_ITEM_SINGLE_CACHE_PREFIX = "FLASH_ITEM_SINGLE_CACHE_%d";

    /**
     * 秒杀活动缓存列表key前缀
     */
    public static final String FLASH_ACTIVITY_CACHE_LIST_PREFIX = "FLASH_ACTIVITY_CACHE_LIST_%s";

    /**
     * 秒杀商品缓存列表key前缀
     */
    public static final String FLASH_ITEM_CACHE_LIST_PREFIX = "FLASH_ITEM_CACHE_LIST_%s";

    /**
     * 用户已经秒杀过某个商品 两个%s分别代表用户id和商品id
     */
    public static final String USER_HAS_FLASH_ITEM_PREFIX = "USER_%s_HAS_FLASH_ITEM_%s";
}
