package com.actionworks.flashsale.application.query.convertor;

import com.actionworks.flashsale.application.model.command.FlashPlaceOrderCommand;
import com.actionworks.flashsale.application.model.dto.FlashOrderDTO;
import com.actionworks.flashsale.application.model.query.FlashOrderQuery;
import com.actionworks.flashsale.domain.model.order.aggregate.FlashOrder;
import com.actionworks.flashsale.domain.model.query.FlashOrderQueryCondition;
import org.springframework.beans.BeanUtils;

public class FlashOrderAppConvertor {

    public static FlashOrder toDomain(FlashPlaceOrderCommand command) {
        if (command == null) {
            return null;
        }

        FlashOrder flashOrder = new FlashOrder();
        BeanUtils.copyProperties(command, flashOrder);
        return flashOrder;
    }

    public static FlashOrderQueryCondition toQueryCondition(FlashOrderQuery query) {
        if (query == null) {
            return null;
        }

        FlashOrderQueryCondition queryCondition = new FlashOrderQueryCondition();
        BeanUtils.copyProperties(query, queryCondition);
        return queryCondition;
    }

    public static FlashOrderDTO toFlashOrderDTO(FlashOrder flashOrder) {
        if (flashOrder == null) {
            return null;
        }

        FlashOrderDTO flashOrderDTO = new FlashOrderDTO();
        BeanUtils.copyProperties(flashOrder, flashOrderDTO);
        return flashOrderDTO;
    }
}
