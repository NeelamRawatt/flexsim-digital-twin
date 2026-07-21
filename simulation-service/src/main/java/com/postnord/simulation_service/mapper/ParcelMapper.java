package com.postnord.simulation_service.mapper;


import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.postnord.simulation_service.dto.FlexsimParcelDto;
import com.postnord.simulation_service.entity.ParcelDetails;
import com.postnord.simulation_service.entity.SimulationContext;

import java.time.Duration;
import java.util.List;

@Mapper(componentModel = "spring")
public interface ParcelMapper {

    @Mapping(target = "flexsimTimeSlot", source = ".", qualifiedByName = "getFlexsimTime")
    FlexsimParcelDto toFlexsimParcelDto(ParcelDetails parcel, @Context SimulationContext context);

    List<FlexsimParcelDto> toFlexsimParcelDtos(List<ParcelDetails> parcels, @Context SimulationContext context);

    @Named("getFlexsimTime")
    default Integer getFlexsimTime(ParcelDetails parcel, @Context SimulationContext context) {
        return (int) Duration.between(context.getStartTime(), parcel.getParcelTimeSlot()).getSeconds() + 180;
    }
}
