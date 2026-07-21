package com.example.flexsim_simulation_service.Mapper;


import com.example.flexsim_simulation_service.DTO.ChuteInsightsKPIPerTickDTO;
import com.example.flexsim_simulation_service.entity.ChuteInsightsKPIPerTick;
import org.springframework.stereotype.Component;

@Component
public class ChuteInsightsKPIPerTickMapper {

    public ChuteInsightsKPIPerTick fromDTO(ChuteInsightsKPIPerTickDTO dto)
    {
        return ChuteInsightsKPIPerTick.builder()
                .simExpId(dto.getSimExpId())
                .chuteId(dto.getChuteId())
                .chuteType(dto.getChuteType())
                .parcel_blocked(dto.getParcel_blocked())
                .parcel_scanned(dto.getParcel_scanned())
                .parcel_throughput(dto.getParcel_throughput())
                .shift_id(dto.getShift_id())
                .tickValue(dto.getTickValue())
                .parcel_weight(dto.getParcel_weight())
                .build();
    }

    public ChuteInsightsKPIPerTickDTO toDTO(ChuteInsightsKPIPerTick entity)
    {
        return ChuteInsightsKPIPerTickDTO.builder()
                .simExpId(entity.getSimExpId())
                .chuteId(entity.getChuteId())
                .chuteType(entity.getChuteType())
                .parcel_blocked(entity.getParcel_blocked())
                .parcel_scanned(entity.getParcel_scanned())
                .parcel_throughput(entity.getParcel_throughput())
                .shift_id(entity.getShift_id())
                .tickValue(entity.getTickValue())
                .parcel_weight(entity.getParcel_weight())
                .build();
    }
}
