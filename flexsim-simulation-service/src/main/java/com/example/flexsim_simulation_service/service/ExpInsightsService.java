package com.example.flexsim_simulation_service.service;

import com.example.flexsim_simulation_service.DTO.ExpInsightsDTO;
import com.example.flexsim_simulation_service.Mapper.ExpInsightsMapper;
import com.example.flexsim_simulation_service.entity.ExpInsights;
import com.example.flexsim_simulation_service.repository.ExpInsightsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExpInsightsService {

    private final ExpInsightsRepository expInsightsRepository;

    private final ExpInsightsMapper expInsightsMapper;

    public ExpInsightsDTO saveExpInsights(ExpInsightsDTO dto)
    {
        ExpInsights entity = expInsightsMapper.fromDTO(dto);
        ExpInsights savedInsights = expInsightsRepository.save(entity);
        return expInsightsMapper.toDTO(savedInsights);
    }

    public List<ExpInsightsDTO> getAllInsights()
    {
        return expInsightsRepository.findAll()
                .stream()
                .map(expInsightsMapper::toDTO)
                .collect(Collectors.toList());
    }

    public ExpInsightsDTO getInsightsByExpId(Integer ExpId)
    {
        ExpInsights entity = expInsightsRepository.findBySimExpId(ExpId)
                .orElseThrow(()-> new RuntimeException("Insights not found for Experiement id : " + ExpId));

        return expInsightsMapper.toDTO(entity);
    }





}
