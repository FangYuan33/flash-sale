package com.actionworks.flashsale.app.service.placeOrder.normal;

import com.actionworks.flashsale.app.model.command.FlashPlaceOrderCommand;
import com.actionworks.flashsale.app.service.placeOrder.AbstractPlaceOrderService;
import com.actionworks.flashsale.app.service.placeOrder.DoPlaceOrderService;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


/**
 * 同步下单服务层实现
 *
 * @author fangyuan
 */
@Slf4j
@Service
@ConditionalOnProperty(name = "placeOrder.type", havingValue = "normal")
public class NormalPlaceOrderServiceImpl extends AbstractPlaceOrderService {

    @Resource
    private DoPlaceOrderService doPlaceOrderService;

    @Override
    protected boolean doSyncPlaceOrderOrPostPlaceOrderTask(Long userId, FlashPlaceOrderCommand command) {
        log.info("开始同步下单, userId {}, {}", userId, JSON.toJSONString(command));

        return doPlaceOrderService.doPlaceOrder(userId, command);
    }
}
