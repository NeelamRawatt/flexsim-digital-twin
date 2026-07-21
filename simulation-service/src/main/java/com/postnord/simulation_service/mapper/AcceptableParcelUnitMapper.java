package com.postnord.simulation_service.mapper;



import org.mapstruct.Mapper;

import com.postnord.simulation_service.dto.AcceptableParcelUnitDTO;
import com.postnord.simulation_service.entity.AcceptableParcelUnit;

@Mapper(componentModel = "spring")
public interface AcceptableParcelUnitMapper {
    AcceptableParcelUnit fromDto(AcceptableParcelUnitDTO dto);
    AcceptableParcelUnitDTO toDto(AcceptableParcelUnit entity);
}