package com.actionworks.flashsale.application.ability;

public interface AbilityService<CMD, RES> {

    RES execute(CMD cmd);

}
