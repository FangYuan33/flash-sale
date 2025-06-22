package com.actionworks.flashsale.trigger.controller;

import com.actionworks.flashsale.application.command.service.FlashOrderAppCommandService;
import com.actionworks.flashsale.application.query.model.dto.FlashOrderDTO;
import com.actionworks.flashsale.application.query.service.FlashOrderAppQueryService;
import com.actionworks.flashsale.trigger.convertor.FlashOrderConvertor;
import com.actionworks.flashsale.trigger.model.request.FlashOrderCreateRequest;
import com.actionworks.flashsale.trigger.model.response.Response;
import com.actionworks.flashsale.trigger.model.response.SingleResponse;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
public class FlashOrderController {

    @Resource
    private FlashOrderAppQueryService flashOrderAppQueryService;

    @Resource
    private FlashOrderAppCommandService flashOrderAppCommandService;

    @ApiOperation(value = "通过 code 获取订单信息")
    @GetMapping("/orders/{code}")
    public SingleResponse<FlashOrderDTO> getOrderByCode(@PathVariable String code) {
        FlashOrderDTO flashOrder = flashOrderAppQueryService.findByCode(code);
        return SingleResponse.of(flashOrder);
    }

    @ApiOperation(value = "创建订单")
    @PostMapping("/orders")
    public Response createOrder(@RequestBody FlashOrderCreateRequest request) {
        flashOrderAppCommandService.createOrder(FlashOrderConvertor.toCreateCommand(request));
        return Response.buildSuccess();
    }
}
