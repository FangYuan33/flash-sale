package com.actionworks.flashsale.trigger.controller;

import com.actionworks.flashsale.application.query.service.impl.FlashOrderAppQueryServiceImpl;
import com.actionworks.flashsale.application.command.service.impl.FlashOrderAppCommandServiceImpl;
import com.actionworks.flashsale.domain.model.aggregate.FlashOrder;
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
    private FlashOrderAppQueryServiceImpl flashOrderAppQueryService;

    @Resource
    private FlashOrderAppCommandServiceImpl flashOrderAppCommandService;

    @ApiOperation(value = "通过 code 获取订单信息")
    @GetMapping("/orders/{code}")
    public SingleResponse<FlashOrder> getOrderByCode(@PathVariable String code) {
        FlashOrder flashOrder = flashOrderAppQueryService.findByCode(code);
        return SingleResponse.of(flashOrder);
    }

    @ApiOperation(value = "创建订单")
    @PostMapping("/orders")
    public Response createOrder(@RequestBody FlashOrderCreateRequest request) {
        flashOrderAppCommandService.createOrder(FlashOrderConvertor.toCreateCommand(request));
        return Response.buildSuccess();
    }
}
