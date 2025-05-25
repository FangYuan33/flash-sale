package com.actionworks.flashsale.infrastructure.adapter;

import com.actionworks.flashsale.domain.adapter.ItemCodeGenerateService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ItemCodeGenerateServiceImpl implements ItemCodeGenerateService {

    // 在这里可以依赖三方服务，比如发号器服务，这样便隔离了外部的变化

    @Override
    public String generateCode() {
        // eg: 这里实际上应该为外部服务调用生成ID
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
