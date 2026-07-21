package com.postnord.simulation_service.service;



import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.postnord.simulation_service.dto.ParcelInsightsKPIPerTickDTO;
import com.postnord.simulation_service.entity.ParcelInsightsKPIPerTick;
import com.postnord.simulation_service.mapper.ParcelDetailsKPIPerTickMapper;
import com.postnord.simulation_service.repository.ParcelDetailsKPIPerTickRepository;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ParcelDetailsKPIPerTickService {

    private final ParcelDetailsKPIPerTickRepository parcelDetailsKPIPerTickRepository;
    private final ParcelDetailsKPIPerTickMapper parcelDetailsKPIPerTickMapper;

    public List<ParcelInsightsKPIPerTickDTO> saveParcelInsights
            (List<ParcelInsightsKPIPerTickDTO> dtoList)
    {
        List<ParcelInsightsKPIPerTick> entityList = dtoList.stream()
                .map(parcelDetailsKPIPerTickMapper::fromDTO)
                .collect(Collectors.toList());

       List<ParcelInsightsKPIPerTick> savedEntityList= parcelDetailsKPIPerTickRepository.saveAll(entityList);

       return savedEntityList.stream()
               .map(parcelDetailsKPIPerTickMapper::toDTO)
               .collect(Collectors.toList());
    }

    public List<ParcelInsightsKPIPerTickDTO> getByExpId(Integer simExpId)
    {
        return parcelDetailsKPIPerTickRepository.findBySimExpId(simExpId)
                .stream()
                .map(parcelDetailsKPIPerTickMapper::toDTO)
                .collect(Collectors.toList());
    }
}
