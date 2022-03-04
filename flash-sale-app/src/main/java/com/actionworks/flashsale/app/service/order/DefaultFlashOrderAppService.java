package com.actionworks.flashsale.app.service.order;

import com.actionworks.flashsale.domain.service.FlashOrderDomainService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class DefaultFlashOrderAppService implements FlashOrderAppService {

    @Resource
    private FlashOrderDomainService flashOrderDomainService;
}
