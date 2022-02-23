package com.actionworks.flashsale.controller.model.convertor;

import com.actionworks.flashsale.app.model.command.FlashActivityPublishCommand;
import com.actionworks.flashsale.app.model.query.FlashActivitiesQuery;
import com.actionworks.flashsale.controller.model.request.FlashActivityPublishRequest;
import com.actionworks.flashsale.controller.model.request.FlashActivityQueryRequest;
import org.springframework.beans.BeanUtils;

/**
 * controller层对象转换为app层对象
 */
public class FlashActivityConvertor {

    /**
     * 将controller层的请求对象转成app层的command对象
     */
    public static FlashActivityPublishCommand toCommand(FlashActivityPublishRequest request) {
        if (request == null) {
            return null;
        }

        FlashActivityPublishCommand activityPublishCommand = new FlashActivityPublishCommand();
        BeanUtils.copyProperties(request, activityPublishCommand);

        return activityPublishCommand;
    }

    public static FlashActivitiesQuery toQuery(FlashActivityQueryRequest request) {
        if (request == null) {
            return null;
        }

        FlashActivitiesQuery flashActivitiesQuery = new FlashActivitiesQuery();
        BeanUtils.copyProperties(request, flashActivitiesQuery);

        return flashActivitiesQuery;
    }
}
