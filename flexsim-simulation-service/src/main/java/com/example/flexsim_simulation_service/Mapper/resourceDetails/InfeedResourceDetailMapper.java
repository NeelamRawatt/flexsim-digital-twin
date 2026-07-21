package com.example.flexsim_simulation_service.Mapper.resourceDetails;

import com.example.flexsim_simulation_service.DTO.resourceDetails.FlexsimInfeedResourceDetailDto;
import com.example.flexsim_simulation_service.DTO.resourceDetails.InfeedResourceDetailDto;
import com.example.flexsim_simulation_service.entity.resourceDetails.InfeedResourceDetail;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface InfeedResourceDetailMapper {
    @Mapping(target = "experimentId", source = "experiment.experimentId")
    InfeedResourceDetailDto toInfeedResourceDetailDto(InfeedResourceDetail infeedResourceDetail);

    List<InfeedResourceDetailDto> toInfeedResourceDetailDtos(List<InfeedResourceDetail> infeedResourceDetails);

    FlexsimInfeedResourceDetailDto toFlexsimInfeedResourceDetailDto(InfeedResourceDetail infeedResourceDetail);

    List<FlexsimInfeedResourceDetailDto> toFlexsimInfeedResourceDetailDtos(List<InfeedResourceDetail> infeedResourceDetails);

    InfeedResourceDetail fromInfeedResourceDetailDto(InfeedResourceDetailDto infeedResourceDetailDto);

    List<InfeedResourceDetail> fromInfeedResourceDetailDtos(List<InfeedResourceDetailDto> infeedResourceDetailDtos);
}
