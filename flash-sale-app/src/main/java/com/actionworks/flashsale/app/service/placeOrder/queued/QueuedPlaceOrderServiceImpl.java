package com.actionworks.flashsale.app.service.placeOrder.queued;

import com.actionworks.flashsale.app.exception.BizException;
import com.actionworks.flashsale.app.model.command.FlashPlaceOrderCommand;
import com.actionworks.flashsale.app.model.result.AppResult;
import com.actionworks.flashsale.app.service.activity.FlashActivityAppService;
import com.actionworks.flashsale.app.service.item.FlashItemAppService;
import com.actionworks.flashsale.app.service.placeOrder.PlaceOrderService;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import static com.actionworks.flashsale.app.exception.AppErrorCode.DO_PLACE_ORDER;

/**
 * 异步下单服务层实现
 *
 * @author fangyuan
 */
@Slf4j
@Service
@ConditionalOnProperty(name = "place_order_type", havingValue = "queued")
public class QueuedPlaceOrderServiceImpl implements PlaceOrderService {

    @Resource
    private FlashActivityAppService flashActivityAppService;
    @Resource
    private FlashItemAppService flashItemAppService;

    @Override
    public <T> AppResult<T> doPlaceOrder(Long userId, FlashPlaceOrderCommand command) {
        log.info("开始异步下单, userId {}, {}", userId, JSON.toJSONString(command));

        // 校验秒杀活动和秒杀商品的秒杀条件
        checkPlaceOrderCondition(command.getActivityId(), command.getItemId());

        // 扣减下单许可

        // 扣减下单许可成功，提交任务

        return null;
    }

    /**
     * 校验秒杀活动、秒杀时间条件
     *
     * @param activityId 秒杀活动id
     * @param itemId     秒杀商品id
     */
    private void checkPlaceOrderCondition(Long activityId, Long itemId) {
        boolean activityAllow = flashActivityAppService.isAllowPlaceOrderOrNot(activityId);
        boolean itemAllow = flashItemAppService.isAllowPlaceOrderOrNot(itemId);

        if (!activityAllow || !itemAllow) {
            throw new BizException(DO_PLACE_ORDER);
        }
    }
}
