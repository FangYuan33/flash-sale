package com.actionworks.flashsale.persistence.convertor;

import com.actionworks.flashsale.domain.model.entity.FlashActivity;
import com.actionworks.flashsale.persistence.model.FlashActivityDO;
import org.springframework.beans.BeanUtils;

/**
 * Domain层和infrastructure层对象的转换
 */
public class FlashActivityConvertor {

    public static FlashActivityDO toDataObjectForCreate(FlashActivity flashActivity) {
        FlashActivityDO flashActivityDO = new FlashActivityDO();
        BeanUtils.copyProperties(flashActivity, flashActivityDO);

        return flashActivityDO;
    }

    public static FlashActivity toDomainObject(FlashActivityDO flashActivityDO) {
        FlashActivity flashActivity = new FlashActivity();
        BeanUtils.copyProperties(flashActivityDO, flashActivity);
        return flashActivity;
    }
}
