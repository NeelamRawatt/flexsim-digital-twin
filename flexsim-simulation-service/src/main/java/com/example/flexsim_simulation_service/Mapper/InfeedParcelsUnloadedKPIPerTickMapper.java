package com.example.flexsim_simulation_service.Mapper;

import com.example.flexsim_simulation_service.DTO.InfeedParcelsUnloadedKPIPerTickDTO;
import com.example.flexsim_simulation_service.entity.InfeedParcelsUnloadedKPIPerTick;
import org.springframework.stereotype.Component;

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
