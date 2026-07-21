package com.example.flexsim_simulation_service.service.resourceDetails;

import com.example.flexsim_simulation_service.DTO.resourceDetails.FlexsimZoneResourceDetailDto;
import com.example.flexsim_simulation_service.DTO.resourceDetails.ZoneResourceDetailDto;
import com.example.flexsim_simulation_service.Mapper.resourceDetails.ZoneResourceDetailMapper;
import com.example.flexsim_simulation_service.entity.resourceDetails.ZoneResourceDetail;
import com.example.flexsim_simulation_service.repository.experiment.ExperimentRepository;
import com.example.flexsim_simulation_service.repository.resourceDetails.ZoneResourceDetailRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ZoneResourceDetailService {
    ZoneResourceDetailRepository zoneResourceDetailRepository;

    ZoneResourceDetailMapper zoneResourceDetailMapper;

    ExperimentRepository experimentRepository;

    public String saveZoneResourceDetails(List<ZoneResourceDetailDto> zoneResourceDetailDtos) {
        List<ZoneResourceDetail> zoneResourceDetails = zoneResourceDetailMapper.fromZoneResourceDetailDtos(zoneResourceDetailDtos);

        Long experimentId = zoneResourceDetailDtos.get(0).getExperimentId();

        experimentRepository.findById(experimentId)
                .ifPresent(experiment -> zoneResourceDetails.forEach(detail -> detail.setExperiment(experiment)));

        zoneResourceDetailRepository.saveAll(zoneResourceDetails);

        return "Zone Resource Details Saved";
    }

    public List<FlexsimZoneResourceDetailDto> getZoneResourceDetailsForExperiment(Long experimentId) {
        List<ZoneResourceDetail> zoneResourceDetails = zoneResourceDetailRepository
                .findByExperimentExperimentId(
                        experimentId,
                        Sort.by("shiftId").ascending()
                                .and(Sort.by("zoneId").ascending())
                                .and(Sort.by("resourceId").ascending())
                );

        return zoneResourceDetailMapper.toFlexsimZoneResourceDetailDtos(zoneResourceDetails);
    }


}
