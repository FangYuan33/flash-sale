package com.actionworks.flashsale.cache.redis;

import com.actionworks.flashsale.cache.model.EntityCache;
import com.alibaba.fastjson.JSONObject;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Service
public class RedisCacheService<T> {

    private static final Long ONE_WEEK_SECONDS = 607800L;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @SuppressWarnings("unchecked")
    public EntityCache<T> getValue(String key) {
        Object o = redisTemplate.opsForValue().get(key);

        // TODO
        if (o != null) {
            return (EntityCache<T>) JSONObject.parseObject(o.toString(), EntityCache.class);
        } else {
            return null;
        }
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
}
