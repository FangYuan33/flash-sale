package com.actionworks.flashsale.cache.impl;

import com.actionworks.flashsale.cache.ItemStockCacheService;
import com.actionworks.flashsale.cache.redis.RedisCacheService;
import com.actionworks.flashsale.domain.model.entity.FlashItem;
import com.actionworks.flashsale.domain.model.enums.FlashItemStatus;
import com.actionworks.flashsale.domain.service.FlashItemDomainService;
import com.actionworks.flashsale.exception.RepositoryException;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

import static com.actionworks.flashsale.exception.RepositoryErrorCode.FLASH_ITEM_STOCK_CACHE_FAILED;

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
         * 秒杀商品不存在 -1
         * 库存不够 -3
         * 其他 -2
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
                "    return -2;" +
                "end;" +
                "return -1;";


        INCREASE_ITEM_STOCK_LUA =
                "if (redis.call('exists', KEYS[1]) == 1) then" +
                "    local stock = tonumber(redis.call('get', KEYS[1]));" +
                "    local num = tonumber(ARGV[1]);" +
                "    redis.call('incrby', KEYS[1] , num);" +
                "    return 1;" +
                "end;" +
                "return -1;";
    }

    @Resource
    private FlashItemDomainService flashItemDomainService;

    @Resource
    private RedisCacheService<Object> redisCacheService;

    /**
     * 库存本地缓存
     */
    private final Cache<String, Integer> stockLocalCache;
    {        // 初始化最小大小为10, 最大大小暂定33，允许并发修改的线程数是5, 过期时间指定10s
        stockLocalCache = CacheBuilder.newBuilder()
                .initialCapacity(10).maximumSize(33)
                .concurrencyLevel(5).expireAfterWrite(10, TimeUnit.SECONDS).build();
    }

    @Override
    public boolean initialItemStocks(Long itemId) {
        // 符合条件返回可用库存值
        Integer stock = checkItemAndGetAvailableStock(itemId);

        // 更新库存缓存
        return doInitialItemStocks(itemId, stock);
    }

    /**
     * 校验缓存商品是否符合条件，符合条件返回可用库存值
     */
    private Integer checkItemAndGetAvailableStock(Long itemId) {
        FlashItem flashItem = flashItemDomainService.getById(itemId);

        if (flashItem == null) {
            log.error("秒杀商品不存在, itemId {}", itemId);
            throw new RepositoryException(FLASH_ITEM_STOCK_CACHE_FAILED);
        }
        if (!FlashItemStatus.ONLINE.getCode().equals(flashItem.getStatus())) {
            log.error("秒杀商品未上线 itemId {}", itemId);
            throw new RepositoryException(FLASH_ITEM_STOCK_CACHE_FAILED);
        }

        return flashItem.getAvailableStock();
    }

    /**
     * 更新分布式库存缓存
     */
    private boolean doInitialItemStocks(Long itemId, Integer stock) {
        String cacheKey = String.format(ITEM_STOCK_KEY, itemId);

        Long result = redisCacheService.executeLua(INIT_ITEM_STOCK_LUA, Collections.singletonList(cacheKey), stock);

        if (result == null) {
            log.error("初始化库存缓存出错，秒杀品ID {}", itemId);
            return false;
        }
        if (result == -1L) {
            log.info("库存已经写入，秒杀品ID {}", itemId);
            return true;
        }
        if (result == 1L) {
            log.info("初始化缓存成功，秒杀品ID {}", itemId);
            return true;
        }

        return false;
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

        boolean exist = checkItemStockExist(key);

        if (exist) {
            return doDecreaseItemStock(key, itemNum);
        } else {
            return false;
        }
    }

    @Override
    public boolean increaseItemStock(Long itemId, Integer itemNum) {
        String key = String.format(ITEM_STOCK_KEY, itemId);

        boolean exist = checkItemStockExist(key);

        if (exist) {
            return doIncreaseItemStock(key, itemNum);
        } else {
            return false;
        }
    }

    /**
     * 校验秒杀库存缓存是否存在
     */
    private boolean checkItemStockExist(String key) {
        if (!redisCacheService.hasKey(key)) {
            log.info("秒杀品未预热，key {}", key);
            return false;
        }

        return true;
    }

    /**
     * 执行扣减库存并返回结果
     */
    private boolean doDecreaseItemStock(String key, Integer itemNum) {
        Long result = redisCacheService.executeLua(DECREASE_ITEM_STOCK_LUA, Collections.singletonList(key), itemNum);

        if (result == null) {
            log.info("库存扣减失败, key {}", key);
            return false;
        }
        if (result == 1L) {
            log.info("库存扣减成功, key {}", key);
            return true;
        }
        if (result == -3L) {
            log.info("库存不足, key {}", key);
            return false;
        }
        if (result == -1L) {
            log.info("秒杀品不存在, key {}", key);
            return false;
        }
        if (result == -2L) {
            log.info("库存扣减异常, key {}", key);
            return false;
        }

        return false;
    }

    /**
     * 执行库存新增并返回结果
     */
    private boolean doIncreaseItemStock(String key, Integer itemNum) {
        Long result = redisCacheService.executeLua(INCREASE_ITEM_STOCK_LUA, Collections.singletonList(key), itemNum);

        if (result == null) {
            log.info("库存增加失败, key {}", key);
            return false;
        }
        if (result == 1L) {
            log.info("库存增加成功, key {}", key);
            return true;
        }
        if (result == -1L) {
            log.info("秒杀品库存不存在, key {}", key);
            return false;
        }

        return false;
    }
}
