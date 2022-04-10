package com.actionworks.flashsale.app.service.placeOrder.impl.queued;

import com.actionworks.flashsale.app.model.command.FlashPlaceOrderCommand;
import com.actionworks.flashsale.app.mq.PlaceOrderTaskPostService;
import com.actionworks.flashsale.app.service.placeOrder.impl.AbstractPlaceOrderService;
import com.actionworks.flashsale.cache.ItemPermissionCacheService;
import com.actionworks.flashsale.mq.message.PlaceOrderTask;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 异步下单服务层实现
 *
 * @author fangyuan
 */
@Slf4j
@Service
@ConditionalOnProperty(name = "placeOrder.type", havingValue = "queued")
public class QueuedPlaceOrderServiceImpl extends AbstractPlaceOrderService {

    @Resource
    private PlaceOrderTaskPostService placeOrderTaskPostService;
    @Resource
    private ItemPermissionCacheService itemPermissionCacheService;

    @Override
    protected boolean doSyncPlaceOrderOrPostPlaceOrderTask(Long userId, FlashPlaceOrderCommand command) {
        log.info("开始提交异步下单任务, userId {}, {}", userId, JSON.toJSONString(command));

        // 扣减下单许可
        boolean decreaseSuccess = itemPermissionCacheService.decreaseItemPermission(command.getItemId());

        // 扣减成功提交任务
        if (decreaseSuccess) {
            boolean postSuccess = postPlaceOrderTask(userId, command);

            if (postSuccess) {
                return true;
            } else {
                // 提交任务失败，恢复下单许可
                itemPermissionCacheService.recoverItemPermission(command.getItemId());

                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * 提交下单任务
     */
    private boolean postPlaceOrderTask(Long userId, FlashPlaceOrderCommand command) {
        PlaceOrderTask placeOrderTask = new PlaceOrderTask();
        BeanUtils.copyProperties(command, placeOrderTask);
        placeOrderTask.setUserId(userId);

        return placeOrderTaskPostService.post(placeOrderTask);
    }
}
