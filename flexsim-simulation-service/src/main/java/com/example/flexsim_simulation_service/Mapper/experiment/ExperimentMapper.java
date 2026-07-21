package com.example.flexsim_simulation_service.Mapper.experiment;

import com.example.flexsim_simulation_service.DTO.experiment.ExperimentDto;
import com.example.flexsim_simulation_service.entity.experiment.Experiment;
import com.example.flexsim_simulation_service.entity.experiment.ExperimentRunProgress;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ExperimentMapper {

    @Mapping(target = "username", source = "user.username")
    ExperimentDto toExperimentDto(Experiment experiment);

    @Mapping(target = "username", source = "user.username")
    @Mapping(target = "status", source = ".", qualifiedByName = "getExperimentStatus")
    ExperimentDto toExperimentDtoWithStatus(Experiment experiment, @Context ExperimentRunProgress experimentRunProgress);

    List<ExperimentDto> toExperimentDtos(List<Experiment> experiments);

    Experiment fromExperimentDto(ExperimentDto experimentDto);

    List<Experiment> fromExperimentDtos(List<ExperimentDto> experimentDtos);

    @Named("getExperimentStatus")
    default String getExperimentStatus(Experiment experiment, @Context ExperimentRunProgress experimentRunProgress){
        return experimentRunProgress.getStatus().name();
    }

}
