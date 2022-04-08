package com.actionworks.flashsale.mq.config;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfig {

    @Value("spring.kafka.bootstrap-servers")
    private String bootstrapServers;

    @Bean
    public KafkaTemplate<String, Object> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    @Bean
    public ProducerFactory<String, Object> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    private Map<String, Object> producerConfigs() {
        Map<String, Object> properties = new HashMap<>();

        // 配置kafka的群组地址
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        // ask 发送消息的响应配置 默认为1 即发送消息到leader，待leader成功将该消息写入本地log之后才确定消息已送达
        // ask 0 发送消息到leader不需要等待leader的写入log 即发了就等于成功了 这种吞吐量最高
        // aks all或-1 需要leader及其followers都将该消息写入log才确定发送成功 可用性最高
        properties.put(ProducerConfig.ACKS_CONFIG, "1");

        // 发送消息失败重试次数和重试时间间隔
        properties.put(ProducerConfig.RETRIES_CONFIG, "10");
        properties.put(ProducerConfig.RETRY_BACKOFF_MS_CONFIG, "100");

        // 请求等待响应的最长时间 默认30000ms
        properties.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, "30000");

        // 序列化
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonDeserializer.class);

        return properties;
    }
}
