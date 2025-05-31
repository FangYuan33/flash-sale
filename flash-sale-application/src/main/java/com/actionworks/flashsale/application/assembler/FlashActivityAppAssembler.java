package com.actionworks.flashsale.application.assembler;

import com.actionworks.flashsale.application.query.model.dto.FlashActivityDTO;
import com.actionworks.flashsale.domain.model.aggregate.FlashActivity;

public class FlashActivityAppAssembler {

    public static FlashActivityDTO toDTO(FlashActivity flashActivity) {
        if (flashActivity == null) {
            return null;
        }

        FlashActivityDTO flashActivityDTO = new FlashActivityDTO();
        flashActivityDTO.setId(flashActivity.getId());
        flashActivityDTO.setCode(flashActivity.getCode());
        flashActivityDTO.setActivityName(flashActivity.getActivityName());
        flashActivityDTO.setActivityDesc(flashActivity.getActivityDesc());
        flashActivityDTO.setFlashItem(FlashItemAppAssembler.toFlashItemDTO(flashActivity.getFlashItem()));
        flashActivityDTO.setStartTime(flashActivity.getStartTime());
        flashActivityDTO.setEndTime(flashActivity.getEndTime());
        flashActivityDTO.setStatus(flashActivity.getStatus() == null ? null : flashActivity.getStatus().getCode());

        return flashActivityDTO;
    }
}
