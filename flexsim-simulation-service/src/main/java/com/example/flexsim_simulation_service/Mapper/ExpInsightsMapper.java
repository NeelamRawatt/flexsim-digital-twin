package com.example.flexsim_simulation_service.Mapper;


import com.example.flexsim_simulation_service.DTO.ExpInsightsDTO;
import com.example.flexsim_simulation_service.entity.ExpInsights;
import lombok.Builder;
import org.springframework.stereotype.Component;

@Component

public class ExpInsightsMapper {

    public ExpInsightsDTO toDTO(ExpInsights entity){
        return ExpInsightsDTO.builder()
                .sim_exp_id(entity.getSimExpId())
                .total_parcels_scanned(entity.getTotal_parcels_scanned())
                .total_parcels_blocked(entity.getTotal_parcels_blocked())
                .total_parcel_throughput(entity.getTotal_parcel_throughput())
                .total_parcels_unloaded(entity.getTotal_parcels_unloaded())
                .total_parcels_rejected(entity.getTotal_parcels_rejected())
                .max_recirculation_count(entity.getMax_recirculation_count())
                .total_parcels_throughput_without_rejected(entity.getTotal_parcels_throughput_without_rejected())
                .total_recirculation_count(entity.getTotal_recirculation_count())
                .total_parcel_not_sorted(entity.getTotal_parcel_not_sorted())
                .build();

    }

    public ExpInsights fromDTO(ExpInsightsDTO dto)
    {
        return ExpInsights.builder()
                .simExpId(dto.getSim_exp_id())
                .total_parcels_scanned(dto.getTotal_parcels_scanned())
                .total_parcels_blocked(dto.getTotal_parcels_blocked())
                .total_parcel_throughput(dto.getTotal_parcel_throughput())
                .total_parcels_unloaded(dto.getTotal_parcels_unloaded())
                .total_parcels_rejected(dto.getTotal_parcels_rejected())
                .max_recirculation_count(dto.getMax_recirculation_count())
                .total_parcels_throughput_without_rejected(dto.getTotal_parcels_throughput_without_rejected())
                .total_recirculation_count(dto.getTotal_recirculation_count())
                .total_parcel_not_sorted(dto.getTotal_parcel_not_sorted())
                .build();
    }

}
