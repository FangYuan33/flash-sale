package com.actionworks.flashsale.application.query.assembler;

import com.actionworks.flashsale.application.query.model.dto.FlashActivityDTO;
import com.actionworks.flashsale.infrastructure.persistence.model.FlashActivityPO;

public class FlashActivityAppAssembler {

    public static FlashActivityDTO toDTO(FlashActivityPO flashActivityPO) {
        if (flashActivityPO == null) {
            return null;
        }

        FlashActivityDTO flashActivityDTO = new FlashActivityDTO();
        flashActivityDTO.setId(flashActivityPO.getId());
        flashActivityDTO.setCode(flashActivityPO.getCode());
        flashActivityDTO.setActivityName(flashActivityPO.getActivityName());
        flashActivityDTO.setActivityDesc(flashActivityPO.getActivityDesc());
        flashActivityDTO.setStartTime(flashActivityPO.getStartTime());
        flashActivityDTO.setEndTime(flashActivityPO.getEndTime());
        flashActivityDTO.setStatus(flashActivityPO.getStatus());

        return flashActivityDTO;
    }
}
