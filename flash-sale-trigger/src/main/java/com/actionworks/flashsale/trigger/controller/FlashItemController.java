package com.actionworks.flashsale.trigger.controller;

import com.actionworks.flashsale.application.command.model.FlashItemPublishCommand;
import com.actionworks.flashsale.application.command.service.FlashItemAppCommandService;
import com.actionworks.flashsale.application.query.model.req.FlashItemQuery;
import com.actionworks.flashsale.application.query.service.FlashItemAppQueryService;
import com.actionworks.flashsale.application.query.model.dto.FlashItemDTO;
import com.actionworks.flashsale.common.exception.AppException;
import com.actionworks.flashsale.trigger.convertor.FlashItemConvertor;
import com.actionworks.flashsale.trigger.model.request.FlashItemPublishRequest;
import com.actionworks.flashsale.trigger.model.request.FlashItemQueryRequest;
import com.actionworks.flashsale.trigger.model.response.Response;
import com.actionworks.flashsale.trigger.model.response.SingleResponse;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
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
    private FlashItemAppCommandService flashItemAppCommandService;
    @Resource
    private FlashItemAppQueryService flashItemAppQueryService;

    @ApiOperation(value = "发布秒杀商品")
    @PostMapping("/activities/{activityId}/flash-items/publish")
    public Response publishFlashItem(@RequestBody FlashItemPublishRequest request) {
        if (StringUtils.isBlank(request.getItemTitle())) {
            throw new AppException("[发布秒杀商品] 标题为空");
        }
        if (StringUtils.isBlank(request.getItemDesc())) {
            throw new AppException("[发布秒杀商品] 描述为空");
        }
        if (request.getInitialStock() == null) {
            throw new AppException("[发布秒杀商品] 初始库存为空");
        }
        if (request.getAvailableStock() == null) {
            throw new AppException("[发布秒杀商品] 可用库存为空");
        }
        if (request.getOriginalPrice() == null) {
            throw new AppException("[发布秒杀商品] 秒杀品原价为空");
        }
        if (request.getFlashPrice() == null) {
            throw new AppException("[发布秒杀商品] 秒杀价为空");
        }

        FlashItemPublishCommand command = FlashItemConvertor.toPublishCommand(request);
        flashItemAppCommandService.publishFlashItem(command);
        return Response.buildSuccess();
    }


    @GetMapping("/flash-items/{itemId}")
    @ApiOperation(value = "通过ID获取秒杀商品信息")
    @ApiImplicitParam(name = "itemId", value = "秒杀商品ID", dataTypeClass = Long.class)
    public SingleResponse<FlashItemDTO> getFlashItem(@PathVariable Long itemId) {
        FlashItemDTO flashItemDTO = flashItemAppQueryService.getById(itemId);
        return SingleResponse.of(flashItemDTO);
    }

    @PostMapping("/flash-items")
    @ApiOperation(value = "条件查询秒杀商品")
    public SingleResponse<List<FlashItemDTO>> getFlashItems(@RequestBody FlashItemQueryRequest request) {
        FlashItemQuery query = FlashItemConvertor.toQuery(request);
        return SingleResponse.of(flashItemAppQueryService.getFlashItems(query));
    }
}
