package com.postnord.simulation_service.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.postnord.simulation_service.dto.ChuteDetailsKPIDTO;
import com.postnord.simulation_service.entity.ChuteDetailsKPI;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ChuteDetailsKPIMapper {

    @Mapping(target = "simExpId", source = "sim_exp_id")
    ChuteDetailsKPI fromDto(ChuteDetailsKPIDTO dto);

    @Mapping(target = "sim_exp_id", source = "simExpId")
    ChuteDetailsKPIDTO toDto(ChuteDetailsKPI entity);

    List<ChuteDetailsKPI> fromDtoList(List<ChuteDetailsKPIDTO> dtos);
    List<ChuteDetailsKPIDTO> toDtoList(List<ChuteDetailsKPI> entities);
}
