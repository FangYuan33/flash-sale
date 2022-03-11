package com.actionworks.flashsale.app.service.item;

import com.actionworks.flashsale.app.exception.BizException;
import com.actionworks.flashsale.app.model.command.FlashItemPublishCommand;
import com.actionworks.flashsale.app.model.convertor.FlashItemAppConvertor;
import com.actionworks.flashsale.app.model.dto.FlashItemDTO;
import com.actionworks.flashsale.app.model.query.FlashItemQuery;
import com.actionworks.flashsale.app.model.result.AppResult;
import com.actionworks.flashsale.app.service.cache.CacheService;
import com.actionworks.flashsale.domain.model.entity.FlashActivity;
import com.actionworks.flashsale.domain.model.entity.FlashItem;
import com.actionworks.flashsale.domain.model.enums.FlashItemStatus;
import com.actionworks.flashsale.domain.model.query.FlashItemQueryCondition;
import com.actionworks.flashsale.domain.service.FlashActivityDomainService;
import com.actionworks.flashsale.domain.service.FlashItemDomainService;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.actionworks.flashsale.app.exception.AppErrorCode.ACTIVITY_NOT_EXIST;
import static com.actionworks.flashsale.app.exception.AppErrorCode.INVALID_PARAMS;

@Slf4j
@Service
public class DefaultFlashItemAppService implements FlashItemAppService {

    @Resource
    private FlashActivityDomainService flashActivityDomainService;
    @Resource
    private FlashItemDomainService flashItemDomainService;
    @Resource
    private CacheService<FlashItem> cacheService;

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
        if (publishParamsIsNull(command)) {
            throw new BizException(INVALID_PARAMS);
        }

        // 校验库存参数
        if (command.getInitialStock() < 0 || command.getAvailableStock() < 0
                || command.getInitialStock() < command.getAvailableStock()) {
            throw new BizException(INVALID_PARAMS);
        }

        // 校验秒杀活动
        FlashActivity flashActivity = flashActivityDomainService.getFlashActivity(activityId);
        if (flashActivity == null) {
            throw new BizException(ACTIVITY_NOT_EXIST);
        }
    }

    /**
     * 发布秒杀商品的必填参数中有空即返回TRUE
     */
    private boolean publishParamsIsNull(FlashItemPublishCommand command) {
        return command == null || command.getInitialStock() == null || command.getAvailableStock() == null
                || command.getItemTitle() == null || command.getOriginalPrice() == null || command.getFlashPrice() == null;
    }

    @Override
    public <T> AppResult<T> onlineFlashItem(Long itemId) {
        log.info("onlineFlashItem|上线秒杀商品|{}", itemId);

        flashItemDomainService.onlineFlashItem(itemId);

        return AppResult.success();
    }

    @Override
    public <T> AppResult<T> offlineFlashItem(Long itemId) {
        log.info("offlineFlashItem|下线秒杀商品|{}", itemId);

        flashItemDomainService.offlineFlashItem(itemId);

        return AppResult.success();
    }

    @Override
    @SuppressWarnings("unchecked")
    public AppResult<FlashItemDTO> getById(Long itemId) {
        FlashItem flashItem = cacheService.getCache(new FlashItemQueryCondition(itemId));

        FlashItemDTO flashItemDTO = FlashItemAppConvertor.toFlashItemDTO(flashItem);

        return AppResult.success(flashItemDTO);
    }

    @Override
    @SuppressWarnings("unchecked")
    public AppResult<List<FlashItemDTO>> getFlashItems(FlashItemQuery query) {
        FlashItemQueryCondition queryCondition = FlashItemAppConvertor.toFlashItemQueryCondition(query);

        List<FlashItem> flashItems = cacheService.getCaches(queryCondition);

        // stream 转换对象类型
        List<FlashItemDTO> itemDTOS = flashItems.stream()
                .map(FlashItemAppConvertor::toFlashItemDTO).collect(Collectors.toList());

        return AppResult.success(itemDTOS);
    }

    @Override
    public boolean isAllowPlaceOrderOrNot(Long itemId) {
        FlashItem flashItem = flashItemDomainService.getById(itemId);

        if (flashItem == null) {
            log.error("isAllowPlaceOrderOrNot|秒杀商品不存在");
            return false;
        }
        if (!FlashItemStatus.ONLINE.getCode().equals(flashItem.getStatus())) {
            log.error("isAllowPlaceOrderOrNot|秒杀商品未上线");
            return false;
        }
        if (LocalDateTime.now().isAfter(flashItem.getEndTime())
                || LocalDateTime.now().isBefore(flashItem.getStartTime())) {
            log.error("isAllowPlaceOrderOrNot|未在秒杀活动时间");
            return false;
        }

        return true;
    }
}
