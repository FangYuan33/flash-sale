package com.actionworks.flashsale.cache.impl;

import com.actionworks.flashsale.cache.ItemStockCacheService;
import com.actionworks.flashsale.domain.model.entity.FlashItem;
import com.actionworks.flashsale.domain.model.enums.FlashItemStatus;
import com.actionworks.flashsale.domain.service.FlashItemDomainService;
import com.actionworks.flashsale.exception.RepositoryException;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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
     * 某商品库存对应的锁 key值 %d拼接商品ID
     */
    private static final String ITEM_STOCK_ALIGN_LOCK_KEY = "ITEM_STOCK_ALIGN_LOCK_KEY_%d";
    /**
     * 锁等待时间
     */
    private static final long WAIT_TIME = 5;
    /**
     * 锁持有时间
     */
    private static final long LEASE_TIME = 5;

    /**
     * 初始化or校正缓存的lua脚本
     */
    private static final String INIT_OR_ALIGN_ITEM_STOCK_LUA;

    static {
        /*
         * 如果该缓存对应的分布式锁的key存在 说明有线程在修改该缓存
         * 否则为该缓存添加对应的锁key并将值设置为 1
         * 之后则是更新该商品的缓存的库存值 更新成功返回 1
         */
        INIT_OR_ALIGN_ITEM_STOCK_LUA =
                "if (redis.call('exists', KEYS[2]) == 1) then" +
                "    return 500;" +
                "end;" +
                "redis.call('set', KEYS[2] , 1);" +
                "local stockNumber = tonumber(ARGV[1]);" +
                "redis.call('set', KEYS[1] , stockNumber);" +
                "redis.call('del', KEYS[2]);" +
                "return 1";
    }

    @Resource
    private FlashItemDomainService flashItemDomainService;

    @Resource
    private RedissonClient redissonClient;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public boolean alignItemStocks(Long itemId) {
        // 符合条件返回可用库存值
        Integer stock = checkItemAndGetAvailableStock(itemId);

        // 更新库存缓存
        return doAlignItemStocks(itemId, stock);
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
    private boolean doAlignItemStocks(Long itemId, Integer stock) {
        String lockKey = String.format(ITEM_STOCK_ALIGN_LOCK_KEY, itemId);
        String cacheKey = String.format(ITEM_STOCK_KEY, itemId);

        RLock lock = redissonClient.getLock(lockKey);
        try {
            if (lock.tryLock(WAIT_TIME, LEASE_TIME, TimeUnit.SECONDS)) {
                try {
                    DefaultRedisScript<Long> redisScript =
                            new DefaultRedisScript<>(INIT_OR_ALIGN_ITEM_STOCK_LUA, Long.class);
                    Long result = redisTemplate.execute(redisScript, Lists.list(cacheKey, lockKey), stock);

                    if (result == null) {
                        log.error("校正库存缓存出错，秒杀品ID {}", itemId);
                        return false;
                    }
                    if (result == 500L) {
                        log.error("库存缓存正在写入，秒杀品ID {}", itemId);
                        return false;
                    }
                    if (result == 1L) {
                        log.info("校正缓存成功，秒杀品ID {}", itemId);
                        return true;
                    }
                } finally {
                    lock.unlock();
                }
            } else {
                return false;
            }
        } catch (InterruptedException e) {
            log.error("分布式锁获取失败", e);
            return false;
        }

        return false;
    }
}
