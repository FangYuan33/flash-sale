package com.actionworks.flashsale.cache.redis;

import com.actionworks.flashsale.cache.model.EntityCache;
import com.alibaba.fastjson.support.spring.FastJsonRedisSerializer;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
public class RedisCacheService<T> {

    private static final Long ONE_WEEK_SECONDS = 607800L;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @SuppressWarnings("unchecked")
    public EntityCache<T> getValue(String key) {
        return (EntityCache<T>) redisTemplate.opsForValue().get(key);
    }

    /**
     * 保存长期的key，目前长期定的是1周
     */
    public void setValue(String key, Object value) {
        setValue(key, value, ONE_WEEK_SECONDS);
    }

    /**
     * 保存有时间限制的key
     *
     * @param second 秒数
     */
    public void setValue(String key, Object value, Long second) {
        redisTemplate.opsForValue().set(key, value, second, TimeUnit.SECONDS);
    }

    /**
     * 根据前缀模糊删除所有相关的缓存
     */
    public void deleteByPrefix(String keyPrefix) {
        String key = String.format(keyPrefix, "*");
        Set<String> keys = redisTemplate.keys(key);

        if (keys != null) {
            redisTemplate.delete(keys);
        }
    }

    /**
     * 执行lua脚本(不带参数)
     */
    public Long executeLuaWithoutArgs(String luaStr, List<String> keys) {
        return executeLua(luaStr, keys, "");
    }

    /**
     * 执行lua脚本
     * 指定了args和result的Serializer，因为在RedisConfig里没有配置value的Serializer会导致lua脚本执行出错
     *
     * @param luaStr lua脚本String
     * @param keys   需要用到的key们
     * @param args   参数们
     */
    public Long executeLua(String luaStr, List<String> keys, Object... args) {
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>(luaStr, Long.class);

        return redisTemplate.execute(redisScript, new FastJsonRedisSerializer<>(Integer.class),
                new FastJsonRedisSerializer<>(Long.class), keys, args);
    }
}
