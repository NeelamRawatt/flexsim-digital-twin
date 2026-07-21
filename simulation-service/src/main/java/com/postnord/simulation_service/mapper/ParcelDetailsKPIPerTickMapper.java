package com.postnord.simulation_service.mapper;



import org.springframework.stereotype.Component;

import com.postnord.simulation_service.dto.ParcelInsightsKPIPerTickDTO;
import com.postnord.simulation_service.entity.ParcelInsightsKPIPerTick;

@Component
public class ParcelDetailsKPIPerTickMapper
{
    public ParcelInsightsKPIPerTick fromDTO(ParcelInsightsKPIPerTickDTO dto)
    {
        return ParcelInsightsKPIPerTick.builder()
                .simExpId(dto.getSimExpId())
                .tick_value(dto.getTick_value())
                .parcel_scanned(dto.getParcel_scanned())
                .parcel_throughput(dto.getParcel_throughput())
                .parcel_rejected(dto.getParcel_rejected())
                .parcel_throughput_without_rejected(dto.getParcel_throughput_without_rejected())
                .parcel_blocked(dto.getParcel_blocked())
                .shift_id(dto.getShift_id())
                .build();
    }

    public ParcelInsightsKPIPerTickDTO toDTO(ParcelInsightsKPIPerTick entity)
    {
        return ParcelInsightsKPIPerTickDTO.builder()
                .simExpId(entity.getSimExpId())
                .tick_value(entity.getTick_value())
                .parcel_scanned(entity.getParcel_scanned())
                .parcel_throughput(entity.getParcel_throughput())
                .parcel_rejected(entity.getParcel_rejected())
                .parcel_throughput_without_rejected(entity.getParcel_throughput_without_rejected())
                .parcel_blocked(entity.getParcel_blocked())
                .shift_id(entity.getShift_id())
                .build();
    }

}
