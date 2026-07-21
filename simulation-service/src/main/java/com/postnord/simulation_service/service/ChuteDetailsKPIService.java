package com.postnord.simulation_service.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.postnord.simulation_service.dto.ChuteDetailsKPIDTO;
import com.postnord.simulation_service.mapper.ChuteDetailsKPIMapper;
import com.postnord.simulation_service.repository.ChuteDetailsKPIRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChuteDetailsKPIService {
    private final ChuteDetailsKPIRepository repository;
    private final ChuteDetailsKPIMapper mapper;

    public List<ChuteDetailsKPIDTO> saveChuteDetails(List<ChuteDetailsKPIDTO> dtoList) {
        return mapper.toDtoList(repository.saveAll(mapper.fromDtoList(dtoList)));
    }

    public List<ChuteDetailsKPIDTO> getBySimExpId(Integer simExpId) {
        return mapper.toDtoList(repository.findBySimExpId(simExpId));
    }
}
