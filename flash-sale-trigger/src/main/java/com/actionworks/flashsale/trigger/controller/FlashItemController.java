package com.actionworks.flashsale.trigger.controller;

import com.actionworks.flashsale.application.query.model.req.FlashItemQuery;
import com.actionworks.flashsale.application.query.service.FlashItemAppQueryService;
import com.actionworks.flashsale.application.query.model.dto.FlashItemDTO;
import com.actionworks.flashsale.trigger.convertor.FlashItemConvertor;
import com.actionworks.flashsale.trigger.model.request.FlashItemQueryRequest;
import com.actionworks.flashsale.trigger.model.response.SingleResponse;
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
    private FlashItemAppQueryService flashItemAppQueryService;

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
