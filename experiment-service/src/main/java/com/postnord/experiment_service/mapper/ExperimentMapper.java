package com.postnord.experiment_service.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.postnord.experiment_service.dto.ExperimentDto;
import com.postnord.experiment_service.entity.Experiment;

@Mapper(componentModel = "spring")
public interface ExperimentMapper 
{

    ExperimentDto toDto(Experiment experiment);
    List<ExperimentDto> toDtoList(List<Experiment> experiments);
    Experiment toEntity(ExperimentDto dto);
    
}
