package com.actionworks.flashsale.domain.service.impl;

import com.actionworks.flashsale.domain.adapter.CodeGenerateService;
import com.actionworks.flashsale.domain.model.aggregate.FlashActivity;
import com.actionworks.flashsale.domain.service.FlashActivityDomainService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class FlashActivityDomainServiceImpl implements FlashActivityDomainService {

    @Resource
    private CodeGenerateService codeGenerateService;

    @Override
    public void publish(FlashActivity flashActivity) {
        // 发布活动（领域模型只处理核心业务逻辑）
        flashActivity.publish();
        
        // 在领域服务层处理外部依赖（编码生成）
        String activityCode = codeGenerateService.generateCode();
        flashActivity.assignCode(activityCode);
    }

}
