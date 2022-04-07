package com.actionworks.flashsale.cache.impl;

import com.actionworks.flashsale.cache.ItemPermissionCacheService;
import com.actionworks.flashsale.cache.enums.LuaResult;
import com.actionworks.flashsale.cache.redis.RedisCacheService;
import com.actionworks.flashsale.domain.service.FlashItemDomainService;
import com.actionworks.flashsale.nacos.NacosProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;

import static com.actionworks.flashsale.cache.enums.LuaResult.SUCCESS;

@Slf4j
@Service
public class ItemPermissionCacheServiceImpl implements ItemPermissionCacheService {

    /**
     * 商品秒杀许可key %d拼接商品ID
     */
    private static final String ITEM_PERMISSION_KEY = "ITEM_PERMISSION_%d";

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

    @Override
    public boolean initialItemPermission(Long itemId) {
        String permissionKey = String.format(ITEM_PERMISSION_KEY, itemId);
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
        // 获取可用库存值，这里没做校验，是因为该动作在初始化库存缓存之后，前者若成功这里就没有问题
        Integer stock = flashItemDomainService.getById(itemId).getAvailableStock();

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
    public boolean decreaseItemPermission(Long itemId) {
        String key = String.format(ITEM_PERMISSION_KEY, itemId);
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
    public boolean recoverItemPermission(Long itemId) {
        String key = String.format(ITEM_PERMISSION_KEY, itemId);
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
