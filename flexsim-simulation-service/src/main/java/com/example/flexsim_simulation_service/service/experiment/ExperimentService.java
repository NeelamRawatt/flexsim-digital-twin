package com.example.flexsim_simulation_service.service.experiment;

import com.example.flexsim_simulation_service.DTO.experiment.ExperimentDto;
import com.example.flexsim_simulation_service.Mapper.experiment.ExperimentMapper;
import com.example.flexsim_simulation_service.entity.experiment.Experiment;
import com.example.flexsim_simulation_service.entity.experiment.ExperimentRunProgress;
import com.example.flexsim_simulation_service.enums.ExperimentRunStatus;
import com.example.flexsim_simulation_service.repository.UserLoginRepository;
import com.example.flexsim_simulation_service.repository.experiment.ExperimentRepository;
import com.example.flexsim_simulation_service.repository.experiment.ExperimentRunProgressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ExperimentService {

    private final ExperimentRepository experimentRepository;

    private final ExperimentMapper experimentMapper;

    private final UserLoginRepository userLoginRepository;

    private final ExperimentRunProgressRepository experimentRunProgressRepository;

    public ExperimentDto saveExperiment(ExperimentDto dto) {
        Experiment entity = experimentMapper.fromExperimentDto(dto);
        entity.setUser(userLoginRepository.findByUsername(dto.getUsername()));
        Experiment savedEntity = experimentRepository.save(entity);
        return experimentMapper.toExperimentDto(savedEntity);
    }

    public ExperimentDto getExperimentById(Long experimentId) {
        Experiment entity = experimentRepository.findById(experimentId)
                .orElseThrow(() -> new RuntimeException("Experiment  not found with id: " + experimentId));
        return experimentMapper.toExperimentDto(entity);
    }

    public ExperimentDto updateExperiment(Long experimentId, ExperimentDto dto) {
        Experiment existingEntity = experimentRepository.findById(experimentId)
                .orElseThrow(() -> new RuntimeException("Experiment  not found with id: " + experimentId));
        existingEntity.setTerminal(dto.getTerminal());
        existingEntity.setExperimentName(dto.getExperimentName());
        existingEntity.setSortingType(dto.getSortingType());
        existingEntity.setSelectedDate(dto.getSelectedDate());
        existingEntity.setStartTime(dto.getStartTime());
        existingEntity.setEndTime(dto.getEndTime());
        existingEntity.setParcelCount(dto.getParcelCount());
        existingEntity.setNewParcelCount(dto.getNewParcelCount());
        existingEntity.setUseCaseId(dto.getUseCaseId());
        existingEntity.setParcelChangeValue(dto.getParcelChangeValue());
        existingEntity.setParcelChangeMode(dto.getParcelChangeMode());
        existingEntity.setMaxRecirculationCount(dto.getMaxRecirculationCount());
        Experiment updatedEntity = experimentRepository.save(existingEntity);



        return experimentMapper.toExperimentDto(updatedEntity);
    }

    public void deleteExperiment(Long experimentId) {
        Experiment existingEntity = experimentRepository.findById(experimentId)
                .orElseThrow(() -> new RuntimeException("Experiment  not found with id: " + experimentId));
        experimentRepository.delete(existingEntity);
    }

//    public List<ExperimentDto> getExperimentsForUser(String username) {
//        List<Experiment> experiments = experimentRepository.findByUserUsername(username);
//        return experimentMapper.toExperimentDtos(experiments);
//    }

    public Page<ExperimentDto> getHistory(String username, Pageable pageable) {
         Page<Experiment> experimentPage  = experimentRepository.findByUserUsernameIgnoreCase(username, pageable);

         return experimentPage.map(experiment -> {
             ExperimentRunProgress progress = experimentRunProgressRepository
                     .findTopByExperimentExperimentIdOrderByRunIdDesc(experiment.getExperimentId())
                     .orElse(ExperimentRunProgress.builder().status(ExperimentRunStatus.FAILED).build());

             return experimentMapper.toExperimentDtoWithStatus(experiment, progress);
         });
    }
}
