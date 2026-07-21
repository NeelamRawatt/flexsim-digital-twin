package com.postnord.simulation_service.service;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.postnord.simulation_service.dto.FlexsimInfeedResourceDetailDto;
import com.postnord.simulation_service.dto.InfeedResourceDetailDto;
import com.postnord.simulation_service.entity.InfeedResourceDetail;
import com.postnord.simulation_service.mapper.InfeedResourceDetailMapper;
import com.postnord.simulation_service.repository.InfeedResourceDetailRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InfeedResourceDetailService {

    private final InfeedResourceDetailRepository infeedResourceDetailRepository;
    private final InfeedResourceDetailMapper infeedResourceDetailMapper;

    public String saveInfeedResourceDetails(List<InfeedResourceDetailDto> dtos) {
        List<InfeedResourceDetail> entities = infeedResourceDetailMapper.fromInfeedResourceDetailDtos(dtos);
        infeedResourceDetailRepository.saveAll(entities);
        return "Infeed Resource Details Saved";
    }

    public List<FlexsimInfeedResourceDetailDto> getInfeedResourceDetailsForExperiment(Long experimentId) {
        List<InfeedResourceDetail> entities = infeedResourceDetailRepository.findByExperimentId(
                experimentId, Sort.by("shiftId").ascending().and(Sort.by("tc").ascending())
        );
        return infeedResourceDetailMapper.toFlexsimInfeedResourceDetailDtos(entities);
    }
}