package com.actionworks.flashsale.app.service.activity;

import com.actionworks.flashsale.app.exception.BizException;
import com.actionworks.flashsale.app.model.convertor.FlashActivityAppConvertor;
import com.actionworks.flashsale.app.model.command.FlashActivityPublishCommand;
import com.actionworks.flashsale.app.model.dto.FlashActivityDTO;
import com.actionworks.flashsale.app.model.query.FlashActivitiesQuery;
import com.actionworks.flashsale.app.model.result.AppResult;
import com.actionworks.flashsale.domain.model.entity.FlashActivity;
import com.actionworks.flashsale.domain.model.query.FlashActivityQueryCondition;
import com.actionworks.flashsale.domain.model.query.PageResult;
import com.actionworks.flashsale.domain.service.FlashActivityDomainService;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.List;
import java.util.stream.Collectors;

import static com.actionworks.flashsale.app.exception.AppErrorCode.INVALID_PARAMS;

@Slf4j
@Service
public class DefaultActivityAppService implements FlashActivityAppService {

    @Resource
    private FlashActivityDomainService flashActivityDomainService;

    @Override
    public <T> AppResult<T> publishFlashActivity(FlashActivityPublishCommand activityPublishCommand) {
        log.info("activityPublish|发布秒杀活动|{}", JSON.toJSONString(activityPublishCommand));

        if (activityPublishCommand == null) {
            throw new BizException(INVALID_PARAMS);
        }

        flashActivityDomainService.publishActivity(FlashActivityAppConvertor.toDomain(activityPublishCommand));

        return AppResult.success();
    }

    @Override
    public <T> AppResult<T> onlineFlashActivity(Long activityId) {
        log.info("activityOnline|上线秒杀活动|{}", activityId);

        flashActivityDomainService.onlineActivity(activityId);

        return AppResult.success();
    }

    @Override
    public <T> AppResult<T> offlineFlashActivity(Long activityId) {
        log.info("activityOffline|下线秒杀活动|{}", activityId);

        flashActivityDomainService.offlineActivity(activityId);

        return AppResult.success();
    }

    @Override
    @SuppressWarnings("unchecked")
    public AppResult<FlashActivityDTO> getFlashActivity(Long activityId) {
        FlashActivity flashActivity = flashActivityDomainService.getFlashActivity(activityId);

        return AppResult.success(FlashActivityAppConvertor.toFlashActivityDTO(flashActivity));
    }

    @Override
    @SuppressWarnings("unchecked")
    public AppResult<List<FlashActivityDTO>> getFlashActivities(FlashActivitiesQuery flashActivitiesQuery) {
        FlashActivityQueryCondition flashActivityQueryCondition =
                FlashActivityAppConvertor.toFlashActivityQueryCondition(flashActivitiesQuery);

        PageResult<FlashActivity> flashActivities =
                flashActivityDomainService.getFlashActivities(flashActivityQueryCondition);

        // stream 完成对象转换
        List<FlashActivityDTO> result = flashActivities.getData()
                .stream().map(FlashActivityAppConvertor::toFlashActivityDTO).collect(Collectors.toList());

        return AppResult.success(result);
    }
}
