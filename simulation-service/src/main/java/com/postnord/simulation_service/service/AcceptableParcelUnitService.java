package com.postnord.simulation_service.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.postnord.simulation_service.dto.AcceptableParcelUnitDTO;
import com.postnord.simulation_service.exception.AcceptableParcelUnitNotFoundException;
import com.postnord.simulation_service.mapper.AcceptableParcelUnitMapper;
import com.postnord.simulation_service.repository.AcceptableParcelUnitRepository;

@Service
@RequiredArgsConstructor
public class AcceptableParcelUnitService {
    private final AcceptableParcelUnitRepository repository;
    private final AcceptableParcelUnitMapper mapper;

    public AcceptableParcelUnitDTO save(AcceptableParcelUnitDTO dto) {
        return mapper.toDto(repository.save(mapper.fromDto(dto)));
    }

    public AcceptableParcelUnitDTO getByExperimentId(Long experimentId) {
        return repository.findByExperimentId(experimentId)
                .map(mapper::toDto)
                .orElseThrow(() -> new AcceptableParcelUnitNotFoundException(experimentId));
    }
}