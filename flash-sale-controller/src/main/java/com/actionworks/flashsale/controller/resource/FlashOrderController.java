package com.actionworks.flashsale.controller.resource;

import com.actionworks.flashsale.app.model.command.FlashPlaceOrderCommand;
import com.actionworks.flashsale.app.model.result.AppResult;
import com.actionworks.flashsale.app.service.order.FlashOrderAppService;
import com.actionworks.flashsale.controller.model.convertor.FlashOrderConvertor;
import com.actionworks.flashsale.controller.model.convertor.ResponseConvertor;
import com.actionworks.flashsale.controller.model.request.FlashPlaceOrderRequest;
import com.alibaba.cola.dto.SingleResponse;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 秒杀订单Controller
 *
 * @author fangyuan
 */
@RestController
public class FlashOrderController {

    @Resource
    private FlashOrderAppService flashOrderAppService;

    /**
     * 秒杀商品下单
     *
     * @param request 秒杀请求
     *                用户ID 这里我们并没有创建用户管理系统，所以仅以此来作为用户唯一标识
     */
    @ApiOperation(value = "秒杀商品下单")
    @PostMapping("/flash-orders/placeOrder")
    public <T> SingleResponse<T> placeOrder(@RequestBody FlashPlaceOrderRequest request) {
        FlashPlaceOrderCommand command = FlashOrderConvertor.toCommand(request);

        AppResult<T> appResult = flashOrderAppService.placeOrder(request.getUserId(), command);

        return ResponseConvertor.with(appResult);
    }

    /**
     * 取消秒杀订单
     *
     * @param orderId 订单ID
     */
    @ApiOperation(value = "取消秒杀订单")
    @PutMapping("/flash-orders/{orderId}/cancelOrder")
    @ApiImplicitParam(name = "orderId", value = "秒杀订单ID", dataTypeClass = Long.class)
    public <T> SingleResponse<T> cancelOrder(@PathVariable Long orderId) {
        AppResult<T> appResult = flashOrderAppService.cancelOrder(orderId);

        return ResponseConvertor.with(appResult);
    }
}
