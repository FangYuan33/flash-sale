package com.actionworks.flashsale.controller.resource;

import com.actionworks.flashsale.app.model.command.FlashItemPublishCommand;
import com.actionworks.flashsale.app.model.query.FlashItemQuery;
import com.actionworks.flashsale.app.model.result.AppResult;
import com.actionworks.flashsale.app.service.item.FlashItemAppService;
import com.actionworks.flashsale.controller.model.convertor.FlashItemConvertor;
import com.actionworks.flashsale.controller.model.convertor.ResponseConvertor;
import com.actionworks.flashsale.controller.model.request.FlashItemPublishRequest;
import com.actionworks.flashsale.controller.model.request.FlashItemQueryRequest;
import com.actionworks.flashsale.controller.model.response.FlashItemResponse;
import com.alibaba.cola.dto.SingleResponse;
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

    @ApiOperation(value = "发布秒杀商品")
    @ApiImplicitParam(name = "activityId", value = "秒杀活动ID")
    @PostMapping("/activities/{activityId}/flash-items/publish")
    public <T> SingleResponse<T> publishFlashItem(@PathVariable Long activityId,
                                                  @RequestBody FlashItemPublishRequest request) {
        FlashItemPublishCommand command = FlashItemConvertor.toCommand(request);

        AppResult<T> appResult = flashItemAppService.publishFlashItem(activityId, command);

        return ResponseConvertor.with(appResult);
    }

    @ApiOperation(value = "上线秒杀商品")
    @ApiImplicitParam(name = "itemId", value = "秒杀商品ID")
    @PutMapping(value = "/flash-items/{itemId}/online")
    public <T> SingleResponse<T> onlineFlashItem(@PathVariable Long itemId) {
        AppResult<T> appResult = flashItemAppService.onlineFlashItem(itemId);

        return ResponseConvertor.with(appResult);
    }

    @ApiOperation(value = "下线秒杀商品")
    @ApiImplicitParam(name = "itemId", value = "秒杀商品ID")
    @PutMapping(value = "/flash-items/{itemId}/offline")
    public <T> SingleResponse<T> offlineFlashItem(@PathVariable Long itemId) {
        AppResult<T> appResult = flashItemAppService.offlineFlashItem(itemId);

        return ResponseConvertor.with(appResult);
    }

    @GetMapping("/flash-items/{itemId}")
    @ApiOperation(value = "通过ID获取秒杀商品信息")
    @ApiImplicitParam(name = "itemId", value = "秒杀商品ID")
    public SingleResponse<FlashItemResponse> getFlashItem(@PathVariable Long itemId) {
        return ResponseConvertor.with(flashItemAppService.getById(itemId));
    }

    @PostMapping("/flash-items")
    @ApiOperation(value = "条件查询秒杀商品")
    public SingleResponse<List<FlashItemResponse>> getFlashItems(@RequestBody FlashItemQueryRequest request) {
        FlashItemQuery query = FlashItemConvertor.toQuery(request);

        return ResponseConvertor.with(flashItemAppService.getFlashItems(query));
    }
}
