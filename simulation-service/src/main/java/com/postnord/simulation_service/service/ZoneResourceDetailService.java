package com.postnord.simulation_service.service;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.postnord.simulation_service.dto.FlexsimZoneResourceDetailDto;
import com.postnord.simulation_service.dto.ZoneResourceDetailDto;
import com.postnord.simulation_service.entity.ZoneResourceDetail;
import com.postnord.simulation_service.mapper.ZoneResourceDetailMapper;
import com.postnord.simulation_service.repository.ZoneResourceDetailRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ZoneResourceDetailService {

    private final ZoneResourceDetailRepository zoneResourceDetailRepository;
    private final ZoneResourceDetailMapper zoneResourceDetailMapper;

    public String saveZoneResourceDetails(List<ZoneResourceDetailDto> dtos) {
        List<ZoneResourceDetail> entities = zoneResourceDetailMapper.fromZoneResourceDetailDtos(dtos);
        zoneResourceDetailRepository.saveAll(entities);
        return "Zone Resource Details Saved";
    }

    public List<FlexsimZoneResourceDetailDto> getZoneResourceDetailsForExperiment(Long experimentId) {
        List<ZoneResourceDetail> entities = zoneResourceDetailRepository.findByExperimentId(
                experimentId,
                Sort.by("shiftId").ascending().and(Sort.by("zoneId").ascending()).and(Sort.by("resourceId").ascending())
        );
        return zoneResourceDetailMapper.toFlexsimZoneResourceDetailDtos(entities);
    }
}
