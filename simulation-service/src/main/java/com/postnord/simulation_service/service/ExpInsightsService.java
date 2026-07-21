package com.postnord.simulation_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.postnord.simulation_service.dto.ExpInsightsDTO;
import com.postnord.simulation_service.entity.ExpInsights;
import com.postnord.simulation_service.exception.InsightsNotFoundException;
import com.postnord.simulation_service.mapper.ExpInsightsMapper;
import com.postnord.simulation_service.repository.ExpInsightsRepository;

@Service
@RequiredArgsConstructor
public class ExpInsightsService {
    private final ExpInsightsRepository repository;
    private final ExpInsightsMapper mapper;

    public ExpInsightsDTO saveExpInsights(ExpInsightsDTO dto) {
        return mapper.toDto(repository.save(mapper.fromDto(dto)));
    }

    public ExpInsightsDTO getInsightsByExpId(Integer simExpId) {
        ExpInsights entity = repository.findBySimExpId(simExpId)
                .orElseThrow(() -> new InsightsNotFoundException(simExpId));
        return mapper.toDto(entity);
    }
}