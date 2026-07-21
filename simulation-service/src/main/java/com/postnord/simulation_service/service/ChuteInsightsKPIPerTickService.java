package com.postnord.simulation_service.service;


import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import com.postnord.simulation_service.dto.ChuteInsightsKPIPerTickDTO;
import com.postnord.simulation_service.entity.ChuteInsightsKPIPerTick;
import com.postnord.simulation_service.mapper.ChuteInsightsKPIPerTickMapper;
import com.postnord.simulation_service.repository.ChuteInsightsKPIPerTickRepository;

import java.util.List;
import java.util.stream.Collector;
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

    public List<ChuteInsightsKPIPerTickDTO> getBySimExpId(Integer simExpId) {
        return chuteInsightsKPIPerTickRepository.findBySimExpId(simExpId).stream()
                .map(chuteInsightsKPIPerTickMapper::toDTO)
                .collect(Collectors.toList());

}
}
