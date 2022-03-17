package com.actionworks.flashsale.cache.impl;

import com.actionworks.flashsale.cache.ItemStockCacheService;
import com.actionworks.flashsale.domain.model.entity.FlashItem;
import com.actionworks.flashsale.domain.model.enums.FlashItemStatus;
import com.actionworks.flashsale.domain.service.FlashItemDomainService;
import com.actionworks.flashsale.exception.RepositoryException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
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

    static {
        /*
         * 如果该缓存对应的分布式锁的key存在 说明有线程在修改该缓存
         * 否则为该缓存添加对应的锁key并将值设置为 1
         * 之后则是更新该商品的缓存的库存值 更新成功返回 1
         */
        INIT_ITEM_STOCK_LUA =
                "if (redis.call('exists', KEYS[1]) == 1) then" +
                        "    return -1;" +
                        "end;" +
                        "local stockNumber = tonumber(ARGV[1]);" +
                        "redis.call('set', KEYS[1] , stockNumber);" +
                        "return 1";
    }

    @Resource
    private FlashItemDomainService flashItemDomainService;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

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

        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>(INIT_ITEM_STOCK_LUA, Long.class);
        Long result = redisTemplate.execute(redisScript, Collections.singletonList(cacheKey), stock);

        if (result == null) {
            log.error("校正库存缓存出错，秒杀品ID {}", itemId);
            return false;
        }
        if (result == -1L) {
            log.error("库存已经写入，秒杀品ID {}", itemId);
            return false;
        }
        if (result == 1L) {
            log.info("校正缓存成功，秒杀品ID {}", itemId);
            return true;
        }

        return false;
    }
}
