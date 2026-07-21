package com.example.flexsim_simulation_service.service.resourceDetails;

import com.example.flexsim_simulation_service.DTO.resourceDetails.FlexsimInfeedResourceDetailDto;
import com.example.flexsim_simulation_service.DTO.resourceDetails.InfeedResourceDetailDto;
import com.example.flexsim_simulation_service.Mapper.resourceDetails.InfeedResourceDetailMapper;
import com.example.flexsim_simulation_service.entity.experiment.Experiment;
import com.example.flexsim_simulation_service.entity.resourceDetails.InfeedResourceDetail;
import com.example.flexsim_simulation_service.repository.experiment.ExperimentRepository;
import com.example.flexsim_simulation_service.repository.resourceDetails.InfeedResourceDetailRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class InfeedResourceDetailService {
    InfeedResourceDetailRepository infeedResourceDetailRepository;

    InfeedResourceDetailMapper infeedResourceDetailMapper;

    ExperimentRepository experimentRepository;

    public String saveInfeedResourceDetails(List<InfeedResourceDetailDto> dtos)
    {
        List<InfeedResourceDetail> infeedResourceDetails = infeedResourceDetailMapper.fromInfeedResourceDetailDtos(dtos);

        Long experimentId = dtos.get(0).getExperimentId();

        experimentRepository.findById(experimentId)
                .ifPresent(experiment -> infeedResourceDetails.forEach(detail -> detail.setExperiment(experiment)));

        infeedResourceDetailRepository.saveAll(infeedResourceDetails);

        return "Infeed Resource Details Saved";
    }

    public List<FlexsimInfeedResourceDetailDto> getInfeedResourceDetailsForExperiment(Long experimentId) {
        List<InfeedResourceDetail> infeedResourceDetails = infeedResourceDetailRepository
                .findByExperimentExperimentId(
                        experimentId,
                        Sort.by("shiftId").ascending()
                                .and(Sort.by("tc").ascending())
                );

        return infeedResourceDetailMapper.toFlexsimInfeedResourceDetailDtos(infeedResourceDetails);
    }
}
