package com.postnord.simulation_service.mapper;


import org.mapstruct.Mapper;

import com.postnord.simulation_service.dto.FlexsimInfeedResourceDetailDto;
import com.postnord.simulation_service.dto.InfeedResourceDetailDto;
import com.postnord.simulation_service.entity.InfeedResourceDetail;

import java.util.List;

@Mapper(componentModel = "spring")
public interface InfeedResourceDetailMapper {
    InfeedResourceDetailDto toInfeedResourceDetailDto(InfeedResourceDetail entity);
    List<InfeedResourceDetailDto> toInfeedResourceDetailDtos(List<InfeedResourceDetail> entities);

    FlexsimInfeedResourceDetailDto toFlexsimInfeedResourceDetailDto(InfeedResourceDetail entity);
    List<FlexsimInfeedResourceDetailDto> toFlexsimInfeedResourceDetailDtos(List<InfeedResourceDetail> entities);

    InfeedResourceDetail fromInfeedResourceDetailDto(InfeedResourceDetailDto dto);
    List<InfeedResourceDetail> fromInfeedResourceDetailDtos(List<InfeedResourceDetailDto> dtos);
}