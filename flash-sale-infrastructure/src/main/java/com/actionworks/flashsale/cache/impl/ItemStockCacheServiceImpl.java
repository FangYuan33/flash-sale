package com.actionworks.flashsale.cache.impl;

import com.actionworks.flashsale.cache.ItemStockCacheService;
import com.actionworks.flashsale.cache.enums.LuaResult;
import com.actionworks.flashsale.cache.redis.RedisCacheService;
import com.actionworks.flashsale.domain.model.entity.FlashItem;
import com.actionworks.flashsale.domain.model.enums.FlashItemStatus;
import com.actionworks.flashsale.domain.service.FlashItemDomainService;
import com.actionworks.flashsale.nacos.NacosProperties;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

import static com.actionworks.flashsale.cache.enums.LuaResult.SUCCESS;

@Slf4j
@Service
public class ItemStockCacheServiceImpl implements ItemStockCacheService {

    /**
     * 商品缓存Key %d拼接商品ID
     */
    private static final String ITEM_STOCK_KEY = "ITEM_STOCK_%d";

    /**
     * 商品秒杀许可key %d拼接商品ID
     */
    private static final String ITEM_AVAILABLE_PERMISSION_KEY = "ITEM_AVAILABLE_PERMISSION_%d";

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

    /**
     * 初始化秒杀许可缓存的lua脚本
     */
    private static final String INIT_ITEM_PERMISSION_LUA;

    /**
     * 扣减秒杀许可缓存的lua脚本
     */
    private static final String DECREASE_ITEM_PERMISSION_LUA;

    /**
     * 恢复秒杀许可缓存的lua脚本
     */
    private static final String RECOVER_ITEM_PERMISSION_LUA;

    static {
        /*
         * 操作成功 1
         * 秒杀许可已经初始化 -5
         */
        INIT_ITEM_PERMISSION_LUA =
                "if (redis.call('exists', KEYS[1]) == 1) then" +
                        "    return -5;" +
                        "end;" +
                        "local permissionNumber = tonumber(ARGV[1]);" +
                        "redis.call('set', KEYS[1] , permissionNumber);" +
                        "return 1";

        /*
         * 扣减许可成功 1
         * 秒杀许可不存在 -7
         * 秒杀许可不够 -6
         */
        DECREASE_ITEM_PERMISSION_LUA =
                "if (redis.call('exists', KEYS[1]) == 1) then" +
                "    local permissionNum = tonumber(redis.call('get', KEYS[1]));" +
                "    if (permissionNum <= 0) then" +
                "        return -6" +
                "    end;" +
                "    if (permissionNum > 0) then" +
                "        redis.call('incrby', KEYS[1], -1);" +
                "        return 1" +
                "    end;" +
                "end;" +
                "return -7;";

        /*
         * 恢复成功 1
         * 恢复失败 -9
         */
        RECOVER_ITEM_PERMISSION_LUA =
                "if (redis.call('exists', KEYS[1]) == 1) then" +
                "   redis.call('incrby', KEYS[1], 1);" +
                "   return 1;" +
                "end;" +
                "return -9;";
    }

    @Resource
    private FlashItemDomainService flashItemDomainService;
    @Resource
    private RedisCacheService<Object> redisCacheService;
    @Resource
    private NacosProperties nacosProperties;


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
    public boolean initialItemAvailablePermission(Long itemId) {
        String permissionKey = String.format(ITEM_AVAILABLE_PERMISSION_KEY, itemId);
        log.info("初始化商品秒杀许可, key {}", permissionKey);

        // 校验秒杀商品条件，并返回可用许可值
        BigDecimal itemPermission = checkItemAndGetPermissionNum(itemId);
        // 更新缓存
        return doInitialItemPermission(permissionKey, itemPermission);
    }

    /**
     * 校验秒杀商品条件，并返回可用许可值
     */
    private BigDecimal checkItemAndGetPermissionNum(Long itemId) {
        // 符合条件返回可用库存值
        Integer stock = checkItemAndGetAvailableStock(itemId);

        BigDecimal permissionFactor = nacosProperties.getPermissionFactor();

        // 根据系数计算出秒杀许可数量，并四舍五入不保留小数
        return permissionFactor.multiply(new BigDecimal(stock != null ? stock : 0))
                .setScale(0, RoundingMode.HALF_UP);
    }

    /**
     * 更新秒杀商品许可数量
     */
    private boolean doInitialItemPermission(String permissionKey, BigDecimal itemPermission) {
        Long resultCode = redisCacheService.executeLua(INIT_ITEM_PERMISSION_LUA,
                Collections.singletonList(permissionKey), itemPermission.intValue());

        return processLuaResultCode(resultCode, permissionKey);
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

    @Override
    public boolean decreaseItemAvailablePermission(Long itemId) {
        String key = String.format(ITEM_AVAILABLE_PERMISSION_KEY, itemId);
        log.info("扣减秒杀许可, key {}", key);

        boolean keyExist = checkKeyExist(key);

        if (keyExist) {
            return doDecreaseItemAvailablePermission(key);
        } else {
            return false;
        }
    }

    /**
     * 扣减秒杀许可
     */
    private boolean doDecreaseItemAvailablePermission(String key) {
        Long resultCode = redisCacheService.executeLua(DECREASE_ITEM_PERMISSION_LUA, Collections.singletonList(key));

        return processLuaResultCode(resultCode, key);
    }

    @Override
    public boolean recoverItemAvailablePermission(Long itemId) {
        String key = String.format(ITEM_AVAILABLE_PERMISSION_KEY, itemId);
        log.info("恢复秒杀许可, key {}", key);

        boolean keyExist = checkKeyExist(key);

        if (keyExist) {
            return doRecoverItemAvailablePermission(key);
        } else {
            return false;
        }
    }

    /**
     * 执行秒杀许可恢复并返回结果
     */
    private boolean doRecoverItemAvailablePermission(String key) {
        Long resultCode = redisCacheService.executeLua(RECOVER_ITEM_PERMISSION_LUA, Collections.singletonList(key));

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
