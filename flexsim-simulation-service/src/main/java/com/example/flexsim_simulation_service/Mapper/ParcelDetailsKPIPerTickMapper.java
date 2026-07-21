package com.example.flexsim_simulation_service.Mapper;


import com.example.flexsim_simulation_service.DTO.ParcelDetailsKPIPerTickDTO;
import com.example.flexsim_simulation_service.entity.ParcelDetailsKPIPerTick;
import org.springframework.stereotype.Component;

@Component
public class ParcelDetailsKPIPerTickMapper
{
    public ParcelDetailsKPIPerTick fromDTO(ParcelDetailsKPIPerTickDTO dto)
    {
        return ParcelDetailsKPIPerTick.builder()
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

    public ParcelDetailsKPIPerTickDTO toDTO(ParcelDetailsKPIPerTick entity)
    {
        return ParcelDetailsKPIPerTickDTO.builder()
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
