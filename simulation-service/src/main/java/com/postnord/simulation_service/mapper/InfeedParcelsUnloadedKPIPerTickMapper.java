package com.postnord.simulation_service.mapper;


import org.springframework.stereotype.Component;

import com.postnord.simulation_service.dto.InfeedParcelsUnloadedKPIPerTickDTO;
import com.postnord.simulation_service.entity.InfeedParcelsUnloadedKPIPerTick;

@Component
public class InfeedParcelsUnloadedKPIPerTickMapper {

    public InfeedParcelsUnloadedKPIPerTick fromDTO(InfeedParcelsUnloadedKPIPerTickDTO dto)
    {
        return InfeedParcelsUnloadedKPIPerTick.builder()
                .simExpId(dto.getSimExpId())
                .tick_value(dto.getTick_value())
                .infeed_name(dto.getInfeed_name())
                .parcel_unloaded(dto.getParcel_unloaded())
                .shift_id(dto.getShift_id())
                .build();
    }

    public InfeedParcelsUnloadedKPIPerTickDTO toDTO(InfeedParcelsUnloadedKPIPerTick entity)
    {
        return InfeedParcelsUnloadedKPIPerTickDTO.builder()
                .simExpId(entity.getSimExpId())
                .tick_value(entity.getTick_value())
                .infeed_name(entity.getInfeed_name())
                .parcel_unloaded(entity.getParcel_unloaded())
                .shift_id(entity.getShift_id())
                .build();
    }
}
