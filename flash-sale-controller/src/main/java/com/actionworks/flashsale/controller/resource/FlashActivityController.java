package com.actionworks.flashsale.controller.resource;

import com.actionworks.flashsale.app.model.command.FlashActivityPublishCommand;
import com.actionworks.flashsale.app.model.query.FlashActivitiesQuery;
import com.actionworks.flashsale.app.model.result.AppResult;
import com.actionworks.flashsale.app.service.activity.FlashActivityAppService;
import com.actionworks.flashsale.controller.model.convertor.FlashActivityConvertor;
import com.actionworks.flashsale.controller.model.convertor.ResponseConvertor;
import com.actionworks.flashsale.controller.model.request.FlashActivityPublishRequest;
import com.actionworks.flashsale.controller.model.request.FlashActivityQueryRequest;
import com.actionworks.flashsale.controller.model.response.FlashActivityResponse;
import com.alibaba.cola.dto.SingleResponse;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 秒杀活动Controller
 *
 * @author fangyuan
 */
@RestController
public class FlashActivityController {

    @Resource
    private FlashActivityAppService flashActivityAppService;

    /**
     * 发布秒杀活动
     *
     * @param request 活动发布请求对象
     */
    @PostMapping(value = "/flash-activities")
    public <T> SingleResponse<T> publishFlashActivity(@RequestBody FlashActivityPublishRequest request) {
        FlashActivityPublishCommand activityPublishCommand = FlashActivityConvertor.toCommand(request);

        AppResult<T> appResult = flashActivityAppService.publishFlashActivity(activityPublishCommand);

        return ResponseConvertor.with(appResult);
    }

    /**
     * 上线秒杀活动
     *
     * @param activityId 秒杀活动ID
     */
    @PutMapping(value = "/flash-activities/{activityId}/online")
    public <T> SingleResponse<T> onlineFlashActivity(@PathVariable Long activityId) {
        AppResult<T> appResult = flashActivityAppService.onlineFlashActivity(activityId);

        return ResponseConvertor.with(appResult);
    }

    /**
     * 下线秒杀活动
     *
     * @param activityId 秒杀活动ID
     */
    @PutMapping(value = "/flash-activities/{activityId}/offline")
    public <T> SingleResponse<T> offlineFlashActivity(@PathVariable Long activityId) {
        AppResult<T> appResult = flashActivityAppService.offlineFlashActivity(activityId);

        return ResponseConvertor.with(appResult);
    }

    /**
     * 根据ID获取活动
     */
    @GetMapping(value = "/flash-activities/{activityId}")
    public SingleResponse<FlashActivityResponse> getFlashActivity(@PathVariable("activityId") Long activityId) {
        return ResponseConvertor.with(flashActivityAppService.getFlashActivity(activityId));
    }

    /**
     * 根据条件获取秒杀活动
     */
    @GetMapping(value = "/flash-activities")
    public SingleResponse<List<FlashActivityResponse>> getFlashActivities(@RequestBody FlashActivityQueryRequest request) {
        FlashActivitiesQuery flashActivitiesQuery = FlashActivityConvertor.toQuery(request);

        return ResponseConvertor.with(flashActivityAppService.getFlashActivities(flashActivitiesQuery));
    }
}
