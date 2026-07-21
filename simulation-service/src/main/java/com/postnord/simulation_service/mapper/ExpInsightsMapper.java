package com.postnord.simulation_service.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.postnord.simulation_service.dto.ExpInsightsDTO;
import com.postnord.simulation_service.entity.ExpInsights;

@Mapper(componentModel = "spring")
public interface ExpInsightsMapper {
    @Mapping(target = "simExpId", source = "sim_exp_id")
    ExpInsights fromDto(ExpInsightsDTO dto);

    @Mapping(target = "sim_exp_id", source = "simExpId")
    ExpInsightsDTO toDto(ExpInsights entity);
}
