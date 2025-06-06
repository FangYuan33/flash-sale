package com.actionworks.flashsale.infrastructure.cache.impl;

import com.actionworks.flashsale.domain.model.item.aggregate.FlashItem;
import com.actionworks.flashsale.infrastructure.cache.ItemStockCacheService;
import com.actionworks.flashsale.infrastructure.cache.enums.LuaResult;
import com.actionworks.flashsale.infrastructure.cache.redis.RedisCacheService;
import com.actionworks.flashsale.domain.model.item.enums.FlashItemStatus;
import com.actionworks.flashsale.domain.service.FlashItemDomainService;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

import static com.actionworks.flashsale.infrastructure.cache.enums.LuaResult.SUCCESS;

@Slf4j
@Service
public class ItemStockCacheServiceImpl implements ItemStockCacheService {

    /**
     * 商品缓存Key %d拼接商品ID
     */
    private static final String ITEM_STOCK_KEY = "ITEM_STOCK_%d";

    /**
     * 初始化缓存的lua脚本
     */
    private static final String INIT_ITEM_STOCK_LUA;

    /**
     * 获取库存缓存的lua脚本
     */
    private static final String GET_ITEM_STOCK_LUA;

    /**
     * 扣减库存的lua脚本
     */
    private static final String DECREASE_ITEM_STOCK_LUA;

    /**
     * 增加库存的lua脚本
     */
    private static final String INCREASE_ITEM_STOCK_LUA;

    static {
        /*
         * 如果该缓存库存已经存在，返回-1
         * 不存在则添加在缓存中
         */
        INIT_ITEM_STOCK_LUA =
                "if (redis.call('exists', KEYS[1]) == 1) then" +
                        "    return -1;" +
                        "end;" +
                        "local stockNumber = tonumber(ARGV[1]);" +
                        "redis.call('set', KEYS[1] , stockNumber);" +
                        "return 1";

        GET_ITEM_STOCK_LUA =
                "if (redis.call('exists', KEYS[1]) == 1) then" +
                        "    return redis.call('get', KEYS[1]);" +
                        "end;" +
                        "return -1";

        /*
         * 扣减库存成功 1
         * 秒杀商品不存在 -2
         * 库存不够 -3
         * 其他 -4
         */
        DECREASE_ITEM_STOCK_LUA =
                "if (redis.call('exists', KEYS[1]) == 1) then" +
                        "    local stock = tonumber(redis.call('get', KEYS[1]));" +
                        "    local num = tonumber(ARGV[1]);" +
                        "    if (stock < num) then" +
                        "        return -3" +
                        "    end;" +
                        "    if (stock >= num) then" +
                        "        redis.call('incrby', KEYS[1], 0 - num);" +
                        "        return 1" +
                        "    end;" +
                        "    return -4;" +
                        "end;" +
                        "return -2;";

        /*
         * 增加成功 1
         * 库存缓存不存在 -2
         */
        INCREASE_ITEM_STOCK_LUA =
                "if (redis.call('exists', KEYS[1]) == 1) then" +
                        "    local stock = tonumber(redis.call('get', KEYS[1]));" +
                        "    local num = tonumber(ARGV[1]);" +
                        "    redis.call('incrby', KEYS[1] , num);" +
                        "    return 1;" +
                        "end;" +
                        "return -2;";
    }

    @Resource
    private FlashItemDomainService flashItemDomainService;
    @Resource
    private RedisCacheService<Object> redisCacheService;

    /**
     * 库存本地缓存
     */
    private final Cache<String, Integer> stockLocalCache;

    {
        // 初始化最小大小为10, 最大大小暂定33，允许并发修改的线程数是5, 过期时间指定10s
        stockLocalCache = CacheBuilder.newBuilder()
                .initialCapacity(10).maximumSize(33)
                .concurrencyLevel(5).expireAfterWrite(10, TimeUnit.SECONDS).build();
    }

    @Override
    public boolean initialItemStocks(Long itemId) {
        String cacheKey = String.format(ITEM_STOCK_KEY, itemId);
        log.info("初始化商品库存缓存, key {}", cacheKey);
        // 符合条件返回可用库存值
        Integer stock = checkItemAndGetAvailableStock(itemId);

        if (stock != null) {
            // 更新库存缓存
            return doInitialItemStocks(cacheKey, stock);
        } else {
            return false;
        }
    }

    /**
     * 校验缓存商品是否符合条件，符合条件返回可用库存值，否则返回null
     */
    private Integer checkItemAndGetAvailableStock(Long itemId) {
        FlashItem flashItem = flashItemDomainService.getById(itemId);

        if (flashItem == null) {
            log.error("秒杀商品不存在, itemId {}", itemId);
            return null;
        }
        if (!FlashItemStatus.ONLINE.getCode().equals(flashItem.getStatus())) {
            log.error("秒杀商品未上线 itemId {}", itemId);
            return null;
        }

        return flashItem.getAvailableStock();
    }

    /**
     * 更新分布式库存缓存
     */
    private boolean doInitialItemStocks(String cacheKey, Integer stock) {
        Long resultCode = redisCacheService.executeLua(INIT_ITEM_STOCK_LUA, Collections.singletonList(cacheKey), stock);

        return processLuaResultCode(resultCode, cacheKey);
    }

    @Override
    public Integer getAvailableItemStock(Long itemId) {
        String key = String.format(ITEM_STOCK_KEY, itemId);

        Integer localStock = stockLocalCache.getIfPresent(key);

        // 优先拿本地缓存，拿不到再去读分布式缓存
        if (localStock != null) {
            return localStock;
        } else {
            int distributedCache = redisCacheService
                    .executeLuaWithoutArgs(GET_ITEM_STOCK_LUA, Collections.singletonList(key)).intValue();
            stockLocalCache.put(key, distributedCache);

            return distributedCache;
        }
    }

    @Override
    public boolean decreaseItemStock(Long itemId, Integer itemNum) {
        String key = String.format(ITEM_STOCK_KEY, itemId);
        log.info("扣减商品库存缓存, key {} num {}", key, itemNum);

        boolean exist = checkKeyExist(key);

        if (exist) {
            return doDecreaseItemStock(key, itemNum);
        } else {
            return false;
        }
    }

    /**
     * 执行扣减库存并返回结果
     */
    private boolean doDecreaseItemStock(String key, Integer itemNum) {
        Long resultCode = redisCacheService.executeLua(DECREASE_ITEM_STOCK_LUA, Collections.singletonList(key), itemNum);

        return processLuaResultCode(resultCode, key);
    }

    @Override
    public boolean increaseItemStock(Long itemId, Integer itemNum) {
        String key = String.format(ITEM_STOCK_KEY, itemId);
        log.info("增加商品库存缓存, key {} num {}", key, itemNum);

        boolean exist = checkKeyExist(key);

        if (exist) {
            return doIncreaseItemStock(key, itemNum);
        } else {
            return false;
        }
    }

    /**
     * 执行库存新增并返回结果
     */
    private boolean doIncreaseItemStock(String key, Integer itemNum) {
        Long resultCode = redisCacheService.executeLua(INCREASE_ITEM_STOCK_LUA, Collections.singletonList(key), itemNum);

        return processLuaResultCode(resultCode, key);
    }

    /**
     * 校验缓存是否存在
     */
    private boolean checkKeyExist(String key) {
        if (!redisCacheService.hasKey(key)) {
            log.info("{} 缓存不存在", key);
            return false;
        }

        return true;
    }

    /**
     * 处理LUA脚本返回值，使用 LuaResult 枚举解析并打印日志
     *
     * @param resultCode lua脚本返回值
     * @param key        缓存key
     */
    private boolean processLuaResultCode(Long resultCode, String key) {
        LuaResult result = LuaResult.parse(resultCode, key);
        log.info(result.toString());

        return result.equals(SUCCESS);
    }
}
