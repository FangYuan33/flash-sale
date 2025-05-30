package com.actionworks.flashsale.trigger.resource;

import com.actionworks.flashsale.application.model.command.FlashItemPublishCommand;
import com.actionworks.flashsale.application.query.model.FlashItemQuery;
import com.actionworks.flashsale.application.model.result.AppResult;
import com.actionworks.flashsale.application.query.FlashItemQueryAppService;
import com.actionworks.flashsale.application.service.item.FlashItemAppService;
import com.actionworks.flashsale.trigger.model.convertor.FlashItemConvertor;
import com.actionworks.flashsale.trigger.model.convertor.ResponseConvertor;
import com.actionworks.flashsale.trigger.model.request.FlashItemPublishRequest;
import com.actionworks.flashsale.trigger.model.request.FlashItemQueryRequest;
import com.actionworks.flashsale.trigger.model.response.FlashItemResponse;
import com.alibaba.cola.dto.SingleResponse;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 秒杀品Controller
 *
 * @author fangyuan
 */
@RestController
public class FlashItemController {

    @Resource
    private FlashItemAppService flashItemAppService;
    @Resource
    private FlashItemQueryAppService flashItemQueryAppService;

    @ApiOperation(value = "发布秒杀商品")
    @PostMapping("/activities/{activityId}/flash-items/publish")
    @ApiImplicitParam(name = "activityId", value = "秒杀活动ID", dataTypeClass = Long.class)
    public <T> SingleResponse<T> publishFlashItem(@PathVariable Long activityId,
                                                  @RequestBody FlashItemPublishRequest request) {
        FlashItemPublishCommand command = FlashItemConvertor.toCommand(request);

        AppResult<T> appResult = flashItemAppService.publishFlashItem(activityId, command);

        return ResponseConvertor.with(appResult);
    }

    @ApiOperation(value = "上线秒杀商品")
    @PutMapping(value = "/flash-items/{itemId}/online")
    @ApiImplicitParam(name = "itemId", value = "秒杀商品ID", dataTypeClass = Long.class)
    public <T> SingleResponse<T> onlineFlashItem(@PathVariable Long itemId) {
        AppResult<T> appResult = flashItemAppService.onlineFlashItem(itemId);

        return ResponseConvertor.with(appResult);
    }

    @ApiOperation(value = "下线秒杀商品")
    @PutMapping(value = "/flash-items/{itemId}/offline")
    @ApiImplicitParam(name = "itemId", value = "秒杀商品ID", dataTypeClass = Long.class)
    public <T> SingleResponse<T> offlineFlashItem(@PathVariable Long itemId) {
        AppResult<T> appResult = flashItemAppService.offlineFlashItem(itemId);

        return ResponseConvertor.with(appResult);
    }

    @SentinelResource("getFlashItem")
    @GetMapping("/flash-items/{itemId}")
    @ApiOperation(value = "通过ID获取秒杀商品信息")
    @ApiImplicitParam(name = "itemId", value = "秒杀商品ID", dataTypeClass = Long.class)
    public SingleResponse<FlashItemResponse> getFlashItem(@PathVariable Long itemId) {
        return ResponseConvertor.with(flashItemQueryAppService.getById(itemId));
    }

    @PostMapping("/flash-items")
    @SentinelResource("getFlashItems")
    @ApiOperation(value = "条件查询秒杀商品")
    public SingleResponse<List<FlashItemResponse>> getFlashItems(@RequestBody FlashItemQueryRequest request) {
        FlashItemQuery query = FlashItemConvertor.toQuery(request);
        return ResponseConvertor.with(flashItemQueryAppService.getFlashItems(query));
    }
}
