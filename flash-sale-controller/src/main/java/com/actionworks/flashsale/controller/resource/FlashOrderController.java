package com.actionworks.flashsale.controller.resource;

import com.actionworks.flashsale.app.service.order.FlashOrderAppService;
import org.springframework.web.bind.annotation.RestController;

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


}
