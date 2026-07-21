package com.postnord.simulation_service.mapper;


import org.springframework.stereotype.Component;

import com.postnord.simulation_service.dto.ResourceWeightHandledKPIDTO;
import com.postnord.simulation_service.entity.ResourceWeightHandledKPI;

@Component
public class ResourceWeightHandledKPIMapper {

    public ResourceWeightHandledKPIDTO toDTO(ResourceWeightHandledKPI entity)
    {
        return ResourceWeightHandledKPIDTO.builder()
                .sim_exp_id(entity.getSimExpId())
                .resource_id(entity.getResource_id())
                .zone_name(entity.getZone_name())
                .chute_id(entity.getChute_id())
                .total_parcels_handled(entity.getTotal_parcels_handled())
                .total_weight_handled(entity.getTotal_weight_handled())
                .tick_value(entity.getTick_value())
                .shift_id(entity.getShift_id())
                .build();


    }

    public ResourceWeightHandledKPI fromDTO(ResourceWeightHandledKPIDTO dto)
    {

        return ResourceWeightHandledKPI.builder()
                .simExpId(dto.getSim_exp_id())
                .resource_id(dto.getResource_id())
                .zone_name(dto.getZone_name())
                .chute_id(dto.getChute_id())
                .total_parcels_handled(dto.getTotal_parcels_handled())
                .total_weight_handled(dto.getTotal_weight_handled())
                .tick_value(dto.getTick_value())
                .shift_id(dto.getShift_id())
                .build();
    }
}
