package com.actionworks.flashsale.controller.resource;

import com.actionworks.flashsale.app.model.command.FlashItemPublishCommand;
import com.actionworks.flashsale.app.model.result.AppResult;
import com.actionworks.flashsale.app.service.item.FlashItemAppService;
import com.actionworks.flashsale.controller.model.convertor.FlashItemConvertor;
import com.actionworks.flashsale.controller.model.convertor.ResponseConvertor;
import com.actionworks.flashsale.controller.model.request.FlashItemPublishRequest;
import com.alibaba.cola.dto.SingleResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 秒杀品Controller
 *
 * @author fangyuan
 */
@RestController
public class FlashItemController {

    @Resource
    private FlashItemAppService flashItemAppService;

    /**
     * 发布秒杀商品
     *
     * @param activityId 秒杀活动ID
     */
    @PostMapping("/activities/{activityId}/flash-items")
    public <T> SingleResponse<T> publishFlashItem(@PathVariable Long activityId,
                                                  @RequestBody FlashItemPublishRequest request) {
        FlashItemPublishCommand command = FlashItemConvertor.toCommand(request);

        AppResult<T> appResult = flashItemAppService.publishFlashItem(activityId, command);

        return ResponseConvertor.with(appResult);
    }
}
