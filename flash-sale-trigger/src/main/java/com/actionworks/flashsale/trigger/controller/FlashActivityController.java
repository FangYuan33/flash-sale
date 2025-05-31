package com.actionworks.flashsale.trigger.controller;

import com.actionworks.flashsale.application.command.model.FlashActivityOperateCommand;
import com.actionworks.flashsale.application.command.model.FlashActivityPublishCommand;
import com.actionworks.flashsale.application.command.service.FlashActivityAppCommandService;
import com.actionworks.flashsale.application.query.model.dto.FlashActivityDTO;
import com.actionworks.flashsale.application.query.service.FlashActivityAppQueryService;
import com.actionworks.flashsale.trigger.convertor.FlashActivityConvertor;
import com.actionworks.flashsale.trigger.model.request.FlashActivityOperateRequest;
import com.actionworks.flashsale.trigger.model.request.FlashActivityPublishRequest;
import com.actionworks.flashsale.trigger.model.response.Response;
import com.actionworks.flashsale.trigger.model.response.SingleResponse;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
public class FlashActivityController {

    @Resource
    private FlashActivityAppCommandService flashActivityAppCommandService;

    @Resource
    private FlashActivityAppQueryService flashActivityAppQueryService;

    @ApiOperation(value = "发布秒杀活动")
    @PostMapping("/activities/publish")
    public Response publishFlashActivity(@RequestBody FlashActivityPublishRequest request) {
        FlashActivityPublishCommand command = FlashActivityConvertor.toPublishCommand(request);
        flashActivityAppCommandService.publishFlashActivity(command);
        return Response.buildSuccess();
    }

    @ApiOperation(value = "操作秒杀活动：上线、下线")
    @PostMapping("/activities/operate")
    public Response operateFlashActivity(@RequestBody FlashActivityOperateRequest request) {
        FlashActivityOperateCommand command = FlashActivityConvertor.toOperateCommand(request);
        flashActivityAppCommandService.operateFlashActivity(command);
        return Response.buildSuccess();
    }

    @ApiOperation(value = "通过 code 获取秒杀活动信息")
    @GetMapping("/activities/{code}")
    public SingleResponse<FlashActivityDTO> getFlashActivity(@PathVariable String code) {
        FlashActivityDTO flashActivityDTO = flashActivityAppQueryService.findByCode(code);
        return SingleResponse.of(flashActivityDTO);
    }
}
