package com.actionworks.flashsale.application.command.service.impl;

import com.actionworks.flashsale.application.command.model.FlashActivityOperateCommand;
import com.actionworks.flashsale.application.command.model.FlashActivityPublishCommand;
import com.actionworks.flashsale.application.command.service.FlashActivityAppCommandService;
import com.actionworks.flashsale.application.convertor.FlashActivityConvertor;
import com.actionworks.flashsale.common.exception.AppException;
import com.actionworks.flashsale.domain.model.aggregate.FlashActivity;
import com.actionworks.flashsale.domain.service.FlashActivityDomainService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
public class FlashActivityAppCommandServiceImpl implements FlashActivityAppCommandService {

    @Resource
    private FlashActivityDomainService flashActivityDomainService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void publishFlashActivity(FlashActivityPublishCommand command) {
        // 校验 command 参数
        checkPublishCommand(command);
        FlashActivity flashActivity = FlashActivityConvertor.publishCommandToDO(command);
        flashActivityDomainService.publish(flashActivity);
    }

    private void checkPublishCommand(FlashActivityPublishCommand command) {
        if (StringUtils.isEmpty(command.getStartTime())) {
            throw new AppException("[发布秒杀活动] 开始时间不能为空");
        }
        if (StringUtils.isEmpty(command.getEndTime())) {
            throw new AppException("[发布秒杀活动] 结束时间不能为空");
        }
        if (StringUtils.isEmpty(command.getFlashItemCode())) {
            throw new AppException("[发布秒杀活动] 商品编码不能为空");
        }
        if (StringUtils.isEmpty(command.getActivityName())) {
            throw new AppException("[发布秒杀活动] 活动名称不能为空");
        }
        if (StringUtils.isEmpty(command.getActivityDesc())) {
            throw new AppException("[发布秒杀活动] 活动描述不能为空");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void operateFlashActivity(FlashActivityOperateCommand command) {
        flashActivityDomainService.changeActivityStatus(command.getCode(), command.getStatus());
    }
}
