package com.actionworks.flashsale.application.command.service.impl;

import com.actionworks.flashsale.application.command.model.FlashActivityOperateCommand;
import com.actionworks.flashsale.application.command.model.FlashActivityPublishCommand;
import com.actionworks.flashsale.application.command.service.FlashActivityAppCommandService;
import com.actionworks.flashsale.application.convertor.FlashActivityConvertor;
import com.actionworks.flashsale.common.exception.AppException;
import com.actionworks.flashsale.domain.model.aggregate.FlashActivity;
import com.actionworks.flashsale.domain.model.aggregate.FlashItem;
import com.actionworks.flashsale.domain.repository.FlashActivityRepository;
import com.actionworks.flashsale.domain.repository.FlashItemRepository;
import com.actionworks.flashsale.domain.service.FlashActivityDomainService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
public class FlashActivityAppCommandServiceImpl implements FlashActivityAppCommandService {

    @Resource
    private FlashActivityDomainService flashActivityDomainService;
    @Resource
    private FlashActivityRepository flashActivityRepository;
    @Resource
    private FlashItemRepository flashItemRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void publishFlashActivity(FlashActivityPublishCommand command) {
        // 校验 command 参数
        checkPublishCommand(command);
        
        // 获取商品信息
        FlashItem flashItem = flashItemRepository.findByCode(command.getFlashItemCode());
        if (flashItem == null) {
            throw new AppException("[发布秒杀活动] 商品不存在: " + command.getFlashItemCode());
        }
        
        // 创建活动对象
        FlashActivity flashActivity = FlashActivityConvertor.publishCommandToDO(command);
        
        // 发布活动（领域服务只处理业务逻辑）
        flashActivityDomainService.publish(flashActivity, flashItem);
        
        // 持久化活动（应用层负责数据访问）
        flashActivityRepository.save(flashActivity);
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
        // 获取活动信息
        FlashActivity flashActivity = flashActivityRepository.findByCode(command.getCode())
                .orElseThrow(() -> new AppException("[变更活动状态] 活动不存在，ID: " + command.getCode()));
        
        // 变更活动状态（领域服务只处理业务逻辑）
        flashActivityDomainService.changeActivityStatus(flashActivity, command.getStatus());
        
        // 持久化活动状态（应用层负责数据访问）
        flashActivityRepository.modifyStatus(flashActivity);
        
        // 活动下线同步更新秒杀商品状态
        if (com.actionworks.flashsale.domain.model.enums.FlashActivityStatus.OFFLINE.getCode().equals(command.getStatus())) {
            flashItemRepository.modifyStatus(flashActivity.getFlashItem());
        }
    }
}
