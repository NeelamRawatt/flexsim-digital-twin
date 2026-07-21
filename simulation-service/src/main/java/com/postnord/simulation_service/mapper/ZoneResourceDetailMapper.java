package com.postnord.simulation_service.mapper;



import com.postnord.simulation_service.dto.FlexsimZoneResourceDetailDto;
import com.postnord.simulation_service.dto.ZoneResourceDetailDto;
import com.postnord.simulation_service.entity.ZoneResourceDetail;

import java.util.Arrays;
import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface ZoneResourceDetailMapper {

    ZoneResourceDetailDto toZoneResourceDetailDto(ZoneResourceDetail entity);
    List<ZoneResourceDetailDto> toZoneResourceDetailDtos(List<ZoneResourceDetail> entities);

    @Mapping(target = "chuteIds", source = ".", qualifiedByName = "getChuteIds")
    FlexsimZoneResourceDetailDto toFlexsimZoneResourceDetailDto(ZoneResourceDetail entity);
    List<FlexsimZoneResourceDetailDto> toFlexsimZoneResourceDetailDtos(List<ZoneResourceDetail> entities);

    ZoneResourceDetail fromZoneResourceDetailDto(ZoneResourceDetailDto dto);
    List<ZoneResourceDetail> fromZoneResourceDetailDtos(List<ZoneResourceDetailDto> dtos);

    @Named("getChuteIds")
    default List<Integer> getChuteIds(ZoneResourceDetail entity) {
        String chutes = entity.getChutes();
        if (chutes == null || chutes.isEmpty()) return List.of();
        String cleaned = chutes.replaceAll("[\\[\\]]", "").trim();
        if (cleaned.isEmpty()) return List.of();
        return Arrays.stream(cleaned.split(","))
                .map(String::trim).filter(s -> !s.isEmpty())
                .map(Integer::parseInt).toList();
    }
}