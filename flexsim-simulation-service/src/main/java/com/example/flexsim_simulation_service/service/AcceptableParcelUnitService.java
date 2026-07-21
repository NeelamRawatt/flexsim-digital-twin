package com.example.flexsim_simulation_service.service;

import org.springframework.stereotype.Service;

import com.example.flexsim_simulation_service.DTO.AcceptableParcelUnitDTO;
import com.example.flexsim_simulation_service.entity.AcceptableParcelUnit;
import com.example.flexsim_simulation_service.repository.AcceptableParcelUnitRepository;
import com.example.flexsim_simulation_service.repository.experiment.ExperimentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AcceptableParcelUnitService {

    private final AcceptableParcelUnitRepository acceptableParcelUnitRepository;
    private final ExperimentRepository experimentRepository;

   public AcceptableParcelUnit save(AcceptableParcelUnitDTO dto) {

    AcceptableParcelUnit entity = new AcceptableParcelUnit();

    Long experimentId = dto.getExperimentId();
 

    entity.setExperiment(

        experimentRepository.findById(experimentId)

            .orElseThrow(() -> new RuntimeException("Experiment not found with id: " + experimentId))

    );

    entity.setMinHeight(dto.getMinHeight());

    entity.setMaxHeight(dto.getMaxHeight());

    entity.setMinLength(dto.getMinLength());

    entity.setMaxLength(dto.getMaxLength());

    entity.setMinWidth(dto.getMinWidth());

    entity.setMaxWidth(dto.getMaxWidth());

    entity.setMinWeight(dto.getMinWeight());

    entity.setMaxWeight(dto.getMaxWeight());

    return acceptableParcelUnitRepository.save(entity);

}
 
}
