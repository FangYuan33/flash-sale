package com.actionworks.flashsale.nacos;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * 存一些Nacos配置中需要动态变更的参数
 */
@Getter
@Component
@RefreshScope
public class NacosProperties {

    /**
     * 控制秒杀商品库存预热
     */
    @Value("${scheduler.warmUpFlag}")
    private Boolean warmUpFlag;

    /**
     * 控制秒杀商品下单许可初始化
     */
    @Value("${scheduler.initialItemPermissionFlag}")
    private Boolean initialItemPermissionFlag;

    /**
     * 秒杀下单许可的系数
     */
    @Value("${placeOrder.permissionFactor}")
    private BigDecimal permissionFactor;
}
