package com.actionworks.flashsale.cache.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    /**
     * value值没做处理使用的是默认的JdkSerializationRedisSerializer(因为Java的泛型擦除，让我不得不使用这个)
     * 如果不使用JdkSerializationRedisSerializer，而是使用JSON转换EntityCache对象时，会导致对象内List中的类型都是JSONObject
     * 这样的话，还需要根据List中对象具体的类型再在业务层代码中加一层转换，转换成需要的类型，否则会出现强转失败的异常
     *
     * 这也就导致了在在redis中查看数据的时候value值都是看不懂的乱码
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(factory);

        // 缓存的key值用StringRedisSerializer解析
        StringRedisSerializer keySerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(keySerializer);
        redisTemplate.setHashKeySerializer(keySerializer);

        return redisTemplate;
    }
}
