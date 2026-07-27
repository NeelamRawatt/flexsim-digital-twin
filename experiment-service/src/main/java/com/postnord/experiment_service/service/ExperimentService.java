package com.postnord.experiment_service.service;

import org.springframework.stereotype.Service;

import com.postnord.experiment_service.client.AuthServiceClient;
import com.postnord.experiment_service.dto.ExperimentDto;
import com.postnord.experiment_service.entity.Experiment;
import com.postnord.experiment_service.enums.ExperimentStatus;
import com.postnord.experiment_service.exception.ExperimentNotFoundException;
import com.postnord.experiment_service.exception.InvalidUsernameException;
import com.postnord.experiment_service.mapper.ExperimentMapper;
import com.postnord.experiment_service.repository.ExperimentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ExperimentService {

    private final ExperimentRepository experimentRepository;
    private final ExperimentMapper experimentMapper;
    private final AuthServiceClient authServiceClient; 

    public ExperimentDto createExperiment(ExperimentDto dto)
    {

        if(!authServiceClient.userExists(dto.getUsername()))
        {
            throw new InvalidUsernameException(dto.getUsername());
        }
        Experiment entity = experimentMapper.toEntity(dto);
        entity.setStatus(ExperimentStatus.CREATED);
        Experiment saved = experimentRepository.save(entity);
        return experimentMapper.toDto(saved);
    }

    public ExperimentDto getExperimentById(Long id)
    {
        Experiment entity = experimentRepository.findById(id)
        .orElseThrow(() -> new ExperimentNotFoundException(id));

        return experimentMapper.toDto(entity);
    }

    public ExperimentDto updateExperiment(Long id,ExperimentDto dto)
    {
        Experiment existing = experimentRepository.findById(id)
        .orElseThrow(()-> new ExperimentNotFoundException(id));

        existing.setExperimentName(dto.getExperimentName());
        existing.setTerminal(dto.getTerminal());
        existing.setSortingType(dto.getSortingType());
        existing.setSelectedDate(dto.getSelectedDate());
        existing.setStartTime(dto.getStartTime());
        existing.setEndTime(dto.getEndTime());
        existing.setParcelCount(dto.getParcelCount());
        existing.setNewParcelCount(dto.getNewParcelCount());
        existing.setUseCaseId(dto.getUseCaseId());
        existing.setParcelChangeValue(dto.getParcelChangeValue());
        existing.setParcelChangeMode(dto.getParcelChangeMode());
        existing.setMaxRecirculationCount(dto.getMaxRecirculationCount());

        return experimentMapper.toDto(experimentRepository.save(existing));
    }


    public void deleteExperiment(Long id)
    {
        if(!experimentRepository.existsById(id))
        {
            throw new ExperimentNotFoundException(id);
        }
        experimentRepository.deleteById(id);
    }

    public Page<ExperimentDto> getHistory(String username,Pageable pageable)
    {
        return experimentRepository.findByUsernameIgnoreCase(username,pageable)
        .map(experimentMapper::toDto);
    }

    public void updateStatus(Long experimentId, String status) {
    Experiment experiment = experimentRepository.findById(experimentId)
            .orElseThrow(() -> new ExperimentNotFoundException(experimentId));
    experiment.setStatus(ExperimentStatus.valueOf(status));
    experimentRepository.save(experiment);
}
    
}
