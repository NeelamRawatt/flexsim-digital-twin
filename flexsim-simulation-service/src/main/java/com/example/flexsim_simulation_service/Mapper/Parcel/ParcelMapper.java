package com.example.flexsim_simulation_service.Mapper.Parcel;

import com.example.flexsim_simulation_service.DTO.parcel.FlexsimParcelDto;
import com.example.flexsim_simulation_service.entity.parcel.Parcel;
import com.example.flexsim_simulation_service.entity.experiment.Experiment;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Mapper(componentModel = "spring")
public interface ParcelMapper {

    @Mapping(target = "flexsimTimeSlot", source = ".", qualifiedByName = "getFlexsimTime")
    FlexsimParcelDto toFlexsimParcelDto(Parcel parcel, @Context Experiment experiment);

    List<FlexsimParcelDto> toFlexsimParcelDtos(List<Parcel> parcels, @Context Experiment experiment);

    @Named("getFlexsimTime")
    default Integer getFlexsimTime(Parcel parcel, @Context Experiment experiment) {
        LocalDateTime startTime = experiment.getStartTime();

        LocalDateTime parcelTimeSlot = parcel.getParcelTimeSlot();

        return (int) Duration.between(startTime, parcelTimeSlot).getSeconds() + 180;
    }
}
