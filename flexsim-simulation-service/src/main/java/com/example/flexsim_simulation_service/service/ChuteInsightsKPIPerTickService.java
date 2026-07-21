package com.example.flexsim_simulation_service.service;

import com.example.flexsim_simulation_service.DTO.ChuteInsightsKPIPerTickDTO;
import com.example.flexsim_simulation_service.Mapper.ChuteInsightsKPIPerTickMapper;
import com.example.flexsim_simulation_service.entity.ChuteInsightsKPIPerTick;
import com.example.flexsim_simulation_service.repository.ChuteDetailsKPIRepository;
import com.example.flexsim_simulation_service.repository.ChuteInsightsKPIPerTickRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@AllArgsConstructor
@Service
public class ChuteInsightsKPIPerTickService {


    private final ChuteInsightsKPIPerTickMapper chuteInsightsKPIPerTickMapper;
    private final ChuteInsightsKPIPerTickRepository chuteInsightsKPIPerTickRepository ;

    public List<ChuteInsightsKPIPerTickDTO> saveInsights(List<ChuteInsightsKPIPerTickDTO> dto)
    {
        List<ChuteInsightsKPIPerTick> entityList=dto.stream()
                .map(chuteInsightsKPIPerTickMapper::fromDTO)
                .collect(Collectors.toList());

        return chuteInsightsKPIPerTickRepository.saveAll(entityList).stream()
                .map(chuteInsightsKPIPerTickMapper::toDTO)
                .collect(Collectors.toList());

    }
}
