package com.actionworks.flashsale.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.Random;

/**
 * 雪花算法 ID生成工具
 *
 * 为什么使用雪花算法？
 * 1. 所有生成的ID按时间趋势递增，索引效率高
 * 2. 在分布式系统中，不会产生重复的ID 这里我们指定的WORKER_ID是随机数
 * 3. 不依赖数据库生成，高性能高可用
 * 4. 对于秒杀系统，它能够每秒生成数百万的自增ID
 */
@Slf4j
public class SnowflakeIdUtil {
    // 下面两个每个5位，加起来就是10位的工作机器id
    private static final long WORKER_ID;
    private static final long DATACENTER_ID;
    // 长度为5位
    private static final long WORKER_ID_BITS = 5L;
    private static final long DATACENTER_ID_BITS = 5L;

    // 12位的序列号
    private static long SEQUENCE;
    // 序列号id长度
    private static final long SEQUENCE_BITS = 12L;
    // 序列号最大值
    private static final long SEQUENCE_MAX = ~(-1L << SEQUENCE_BITS);

    // 工作id需要左移的位数，12位
    private static final long WORKER_ID_SHIFT = SEQUENCE_BITS;
    // 数据id需要左移位数 12+5=17位
    private static final long DATACENTER_ID_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS;
    //时间戳需要左移位数 12+5+5=22位
    private static final long TIMESTAMP_LEFT_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS + DATACENTER_ID_BITS;

    // 初始时间戳
    private static final long ORIGINAL_TIME_STAMP = 1288834974657L;
    // 上次时间戳，初始值为负数
    private static long LAST_TIMESTAMP = -1L;

    static {
        Random random = new Random(1);
        
        // workerId can't be greater than ~(-1L << workerIdBits) or less than 0
        WORKER_ID = random.nextInt(32);
        // datacenterId can't be greater than ~(-1L << datacenterIdBits) or less than 0
        DATACENTER_ID = 1;
        SEQUENCE = 1;

        log.info("Snowflake starting. timestamp left shift {}, datacenter id bits {}, worker id bits {}, " +
                        "sequence bits {}, worker id {}",
                TIMESTAMP_LEFT_SHIFT, DATACENTER_ID_BITS, WORKER_ID_BITS, SEQUENCE_BITS, WORKER_ID);
    }

    // ID生成算法
    public static synchronized long nextId() {
        long currentTimestamp = System.currentTimeMillis();

        // 获取当前时间戳如果小于上次时间戳，则表示时间戳获取出现异常
        if (currentTimestamp < LAST_TIMESTAMP) {
            log.error("clock is moving backwards. Rejecting requests until {}.", LAST_TIMESTAMP);
            throw new RuntimeException(String.format("Clock moved backwards.  Refusing to generate id for %d milliseconds",
                    LAST_TIMESTAMP - currentTimestamp));
        }

        // 获取当前时间戳如果等于上次时间戳（同一毫秒内），则在序列号加一；否则序列号赋值为0，从0开始。
        if (LAST_TIMESTAMP == currentTimestamp) {
            SEQUENCE = (SEQUENCE + 1) & SEQUENCE_MAX;
            if (SEQUENCE == 0) {
                currentTimestamp = tilNextMillis(LAST_TIMESTAMP);
            }
        } else {
            SEQUENCE = 0;
        }

        // 将上次时间戳值刷新
        LAST_TIMESTAMP = currentTimestamp;

        /*
         * 返回结果：
         * (timestamp - ORIGINAL_TIME_STAMP) << TIMESTAMP_LEFT_SHIFT) 表示将时间戳减去初始时间戳，再左移相应位数
         * (DATACENTER_ID << DATACENTER_ID_SHIFT) 表示将数据id左移相应位数
         * (WORKER_ID << WORKER_ID_SHIFT) 表示将工作id左移相应位数
         * | 是按位或运算符，例如：x | y，只有当x，y都为0的时候结果才为0，其它情况结果都为1。
         * 因为个部分只有相应位上的值有意义，其它位上都是0，所以将各部分的值进行 | 运算就能得到最终拼接好的id
         */
        return ((currentTimestamp - ORIGINAL_TIME_STAMP) << TIMESTAMP_LEFT_SHIFT)
                | (DATACENTER_ID << DATACENTER_ID_SHIFT)
                | (WORKER_ID << WORKER_ID_SHIFT)
                | SEQUENCE;
    }

    // 获取时间戳，并与上次时间戳比较
    private static long tilNextMillis(long lastTimestamp) {
        long timestamp = System.currentTimeMillis();
        while (timestamp <= lastTimestamp) {
            timestamp = System.currentTimeMillis();
        }
        return timestamp;
    }
}
