package com.actionworks.flashsale.controller.resource;

import com.actionworks.flashsale.app.model.command.FlashPlaceOrderCommand;
import com.actionworks.flashsale.app.model.query.FlashOrderQuery;
import com.actionworks.flashsale.app.model.result.AppResult;
import com.actionworks.flashsale.app.service.order.FlashOrderAppService;
import com.actionworks.flashsale.controller.model.convertor.FlashOrderConvertor;
import com.actionworks.flashsale.controller.model.convertor.ResponseConvertor;
import com.actionworks.flashsale.controller.model.request.FlashOrderPlaceRequest;
import com.actionworks.flashsale.controller.model.request.FlashOrderQueryRequest;
import com.actionworks.flashsale.controller.model.response.FlashOrderResponse;
import com.alibaba.cola.dto.SingleResponse;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

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
    @SentinelResource("placeOrder")
    @ApiOperation(value = "秒杀商品下单")
    @PostMapping("/flash-orders/placeOrder")
    public <T> SingleResponse<T> placeOrder(@RequestBody FlashOrderPlaceRequest request) {
        FlashPlaceOrderCommand command = FlashOrderConvertor.toCommand(request);

        AppResult<T> appResult = flashOrderAppService.placeOrder(request.getUserId(), command);

        return ResponseConvertor.with(appResult);
    }

    @ApiOperation(value = "取消秒杀订单")
    @PutMapping("/flash-orders/{orderId}/cancelOrder")
    @ApiImplicitParam(name = "orderId", value = "秒杀订单ID", dataTypeClass = Long.class)
    public <T> SingleResponse<T> cancelOrder(@PathVariable Long orderId) {
        AppResult<T> appResult = flashOrderAppService.cancelOrder(orderId);

        return ResponseConvertor.with(appResult);
    }

    @ApiOperation(value = "根据ID获取订单")
    @GetMapping("/flash-orders/{orderId}")
    @ApiImplicitParam(name = "orderId", value = "秒杀订单ID", dataTypeClass = Long.class)
    public SingleResponse<FlashOrderResponse> getFlashOrder(@PathVariable Long orderId) {
        return ResponseConvertor.with(flashOrderAppService.getFlashOrder(orderId));
    }

    @PostMapping("/flash-orders")
    @ApiOperation(value = "多条件查询秒杀订单")
    public SingleResponse<List<FlashOrderResponse>> getFlashOrders(@RequestBody FlashOrderQueryRequest request) {
        FlashOrderQuery flashOrderQuery = FlashOrderConvertor.toQuery(request);

        return ResponseConvertor.with(flashOrderAppService.getFlashOrders(flashOrderQuery));
    }
}
