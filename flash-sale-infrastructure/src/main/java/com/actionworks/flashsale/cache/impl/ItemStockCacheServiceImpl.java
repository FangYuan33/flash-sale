package com.actionworks.flashsale.cache.impl;

import com.actionworks.flashsale.cache.ItemStockCacheService;
import com.actionworks.flashsale.cache.redis.RedisCacheService;
import com.actionworks.flashsale.domain.model.entity.FlashItem;
import com.actionworks.flashsale.domain.model.enums.FlashItemStatus;
import com.actionworks.flashsale.domain.service.FlashItemDomainService;
import com.actionworks.flashsale.exception.RepositoryException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;

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
    }

    @Resource
    private FlashItemDomainService flashItemDomainService;

    @Resource
    private RedisCacheService<Object> redisCacheService;

    @Override
    public boolean initialItemStocks(Long itemId) {
        // 符合条件返回可用库存值
        Integer stock = checkItemAndGetAvailableStock(itemId);

        // 更新库存缓存
        return doInitialItemStocks(itemId, stock);
    }

    @Override
    public Integer getAvailableItemStock(Long itemId) {
        String key = String.format(ITEM_STOCK_KEY, itemId);

        return redisCacheService.executeLuaWithoutArgs(GET_ITEM_STOCK_LUA, Collections.singletonList(key)).intValue();
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
}
