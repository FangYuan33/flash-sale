package com.actionworks.flashsale.domain.service.impl;

import com.actionworks.flashsale.domain.exception.DomainException;
import com.actionworks.flashsale.domain.model.entity.FlashOrder;
import com.actionworks.flashsale.domain.model.enums.FlashOrderStatus;
import com.actionworks.flashsale.domain.model.query.FlashOrderQueryCondition;
import com.actionworks.flashsale.domain.model.query.PageResult;
import com.actionworks.flashsale.domain.repository.FlashOrderRepository;
import com.actionworks.flashsale.domain.service.FlashOrderDomainService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.actionworks.flashsale.domain.exception.DomainErrorCode.FLASH_ORDER_NOT_EXIST;

@Service
public class FlashOrderDomainServiceImpl implements FlashOrderDomainService {

    @Resource
    private FlashOrderRepository flashOrderRepository;

    @Override
    public void doPlaceOrder(FlashOrder flashOrder) {
        flashOrderRepository.save(flashOrder);
    }

    @Override
    public void cancelOrder(Long orderId) {
        Optional<FlashOrder> optionalFlashOrder = flashOrderRepository.getById(orderId);
        FlashOrder flashOrder = optionalFlashOrder.orElseThrow(() -> new DomainException(FLASH_ORDER_NOT_EXIST));

        // 状态判断
        if (FlashOrderStatus.CANCEL.getCode().equals(flashOrder.getStatus())) {
            return;
        }

        // 状态变更
        flashOrder.setStatus(FlashOrderStatus.CANCEL.getCode());

        flashOrderRepository.updateById(flashOrder);
    }

    @Override
    @SuppressWarnings("unchecked")
    public PageResult<FlashOrder> listByQueryCondition(FlashOrderQueryCondition queryCondition) {
        // 校正分页参数
        queryCondition.buildParams();

        Optional<List<FlashOrder>> flashOrders = flashOrderRepository.listByQueryCondition(queryCondition);
        Integer totalCount = flashOrderRepository.countByQueryCondition(queryCondition);

        return PageResult.with(flashOrders.orElse(Collections.EMPTY_LIST), totalCount);
    }

    @Override
    public FlashOrder getById(Long orderId) {
        Optional<FlashOrder> flashOrderOptional = flashOrderRepository.getById(orderId);

        return flashOrderOptional.orElseThrow(() -> new DomainException(FLASH_ORDER_NOT_EXIST));
    }
}
