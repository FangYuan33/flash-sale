package com.actionworks.flashsale.app.service.item;

import com.actionworks.flashsale.app.exception.BizException;
import com.actionworks.flashsale.app.model.command.FlashItemPublishCommand;
import com.actionworks.flashsale.app.model.convertor.FlashItemAppConvertor;
import com.actionworks.flashsale.app.model.result.AppResult;
import com.actionworks.flashsale.domain.model.entity.FlashActivity;
import com.actionworks.flashsale.domain.model.entity.FlashItem;
import com.actionworks.flashsale.domain.service.FlashActivityDomainService;
import com.actionworks.flashsale.domain.service.FlashItemDomainService;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import static com.actionworks.flashsale.app.exception.AppErrorCode.ACTIVITY_NOT_EXIST;
import static com.actionworks.flashsale.app.exception.AppErrorCode.INVALID_PARAMS;

@Slf4j
@Service
public class DefaultFlashItemAppService implements FlashItemAppService {

    @Resource
    private FlashActivityDomainService flashActivityDomainService;
    @Resource
    private FlashItemDomainService flashItemDomainService;

    @Override
    public <T> AppResult<T> publishFlashItem(Long activityId, FlashItemPublishCommand command) {
        log.info("flashItemPublish|发布秒杀商品|{}|{}", activityId, JSON.toJSONString(command));

        // 参数校验
        checkPublishParams(activityId, command);

        FlashItem flashItem = FlashItemAppConvertor.toDomain(command);
        flashItem.setActivityId(activityId);

        flashItemDomainService.publishFlashItem(flashItem);
        log.info("itemPublish|秒杀品已发布");

        return AppResult.success();
    }

    /**
     * 校验发布商品的参数
     */
    private void checkPublishParams(Long activityId, FlashItemPublishCommand command) {
        if (command == null) {
            throw new BizException(INVALID_PARAMS);
        }

        // 校验秒杀活动
        FlashActivity flashActivity = flashActivityDomainService.getFlashActivity(activityId);
        if (flashActivity == null) {
            throw new BizException(ACTIVITY_NOT_EXIST);
        }
    }

    @Override
    public <T> AppResult<T> onlineFlashItem(Long itemId) {
        log.info("onlineFlashItem|上线秒杀商品|{}", itemId);

        flashItemDomainService.onlineFlashItem(itemId);

        return AppResult.success();
    }
}
