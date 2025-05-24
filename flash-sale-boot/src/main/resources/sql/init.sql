CREATE database `flash_sale`;

-- 初始化秒杀活动表
CREATE TABLE IF NOT EXISTS flash_sale.`flash_activity` (
    `id`            bigint  NOT NULL AUTO_INCREMENT COMMENT '主键',
    `activity_name` varchar(32) NOT NULL COMMENT '秒杀活动名称',
    `activity_desc` varchar(64) DEFAULT NULL COMMENT '秒杀活动描述',
    `item_id`       bigint  NOT NULL COMMENT '秒杀品ID',
    `start_time`    datetime    NOT NULL COMMENT '秒杀活动开始时间',
    `end_time`      datetime    NOT NULL COMMENT '秒杀活动结束时间',
    `status`        tinyint  DEFAULT NULL COMMENT '秒杀活动状态 10-已发布 20-已上线 30-已下线',
    `create_time`   datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `modified_time` datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `key_status`(`status`),
    KEY `key_start_time`(`start_time`)
    ) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '秒杀活动表';

-- 初始化秒杀商品表
CREATE TABLE IF NOT EXISTS flash_sale.`flash_item` (
    `id`              bigint  NOT NULL AUTO_INCREMENT COMMENT '秒杀品ID',
    `code`            varchar(64) NOT NULL COMMENT '商品唯一编码',
    `item_title`      varchar(50) NOT NULL COMMENT '秒杀品名称标题',
    `item_desc`       varchar(100) DEFAULT NULL COMMENT '秒杀品介绍文案',
    `original_price`  bigint(20)  NOT NULL COMMENT '秒杀品原价',
    `flash_price`     bigint(20)  NOT NULL COMMENT '秒杀价',
    `status`          tinyint(2)  NOT NULL COMMENT '秒杀品状态 10-已发布 20-已上线 30-已下线',
    `create_time`     datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `modified_time`   datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `key_code`(`code`),
    KEY `key_status`(`status`)
    ) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '秒杀品';

-- 初始化商品库存表
CREATE TABLE IF NOT EXISTS flash_sale.`stock` (
    `id`              bigint  NOT NULL AUTO_INCREMENT COMMENT '库存 ID',
    `code`            varchar(64) NOT NULL COMMENT '商品唯一编码',
    `initial_stock`   int     NOT NULL DEFAULT 0 COMMENT '秒杀品初始库存',
    `available_stock` int     NOT NULL DEFAULT 0 COMMENT '秒杀品可用库存',
    `create_time`     datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `modified_time`   datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `key_code`(`code`)
    ) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '商品库存';

-- 初始化秒杀订单表
CREATE TABLE IF NOT EXISTS flash_sale.`flash_order` (
    `id`            bigint  NOT NULL AUTO_INCREMENT COMMENT '秒杀订单ID',
    `code`          varchar(64) NOT NULL COMMENT '订单唯一编码',
    `item_code`     varchar(64) NOT NULL COMMENT '商品唯一编码',
    `user_id`       bigint  NOT NULL COMMENT '用户ID',
    `item_title`    varchar(50) NOT NULL COMMENT '秒杀品名称',
    `quantity`      int     NOT NULL COMMENT '数量',
    `total_amount`  bigint  NOT NULL COMMENT '总价格',
    `status`        tinyint  NOT NULL COMMENT '订单状态 10-已创建 20-已取消',
    `create_time`   datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `modified_time` datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `key_code`(`code`),
    KEY `key_user_id` (`user_id`)
    ) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '秒杀订单表';

