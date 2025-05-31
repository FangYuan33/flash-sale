package com.actionworks.flashsale.trigger.model.request;

import lombok.Data;

@Data
public class FlashActivityPublishRequest {

    private String activityName;

    private String activityDesc;

    private String flashItemCode;

    private String startTime;

    private String endTime;

}
