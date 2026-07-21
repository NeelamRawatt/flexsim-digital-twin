package com.example.flexsim_simulation_service.Mapper.resourceDetails;

import com.example.flexsim_simulation_service.DTO.resourceDetails.FlexsimZoneResourceDetailDto;
import com.example.flexsim_simulation_service.DTO.resourceDetails.ZoneResourceDetailDto;
import com.example.flexsim_simulation_service.entity.resourceDetails.ZoneResourceDetail;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.Arrays;
import java.util.List;

@Mapper(componentModel = "spring")
public interface ZoneResourceDetailMapper {

    @Mapping(target = "experimentId", source = "experiment.experimentId")
    ZoneResourceDetailDto toZoneResourceDetailDto(ZoneResourceDetail zoneResourceDetail);

    List<ZoneResourceDetailDto> toZoneResourceDetailDtos(List<ZoneResourceDetail> zoneResourceDetails);

    @Mapping(target = "chuteIds", source = ".", qualifiedByName = "getChuteIds")
    FlexsimZoneResourceDetailDto toFlexsimZoneResourceDetailDto(ZoneResourceDetail zoneResourceDetail);

    List<FlexsimZoneResourceDetailDto> toFlexsimZoneResourceDetailDtos(List<ZoneResourceDetail> zoneResourceDetails);

    ZoneResourceDetail fromZoneResourceDetailDto(ZoneResourceDetailDto zoneResourceDetailDto);

    List<ZoneResourceDetail> fromZoneResourceDetailDtos(List<ZoneResourceDetailDto> zoneResourceDetailDtos);

    @Named(value = "getChuteIds")
    default List<Integer> getChuteIds(ZoneResourceDetail zoneResourceDetail){
        String chutes = zoneResourceDetail.getChutes();

        if(chutes == null || chutes.isEmpty()){
            return List.of();
        }

        String cleanedChutes = chutes.replaceAll("[\\[\\]]", "").trim();

        if(cleanedChutes.isEmpty()){
            return List.of();
        }

        // Split by comma and convert to integers
        return Arrays.stream(cleanedChutes.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(Integer::parseInt)
                .toList();
    }
}
