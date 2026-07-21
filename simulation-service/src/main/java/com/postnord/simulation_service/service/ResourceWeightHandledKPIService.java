package com.postnord.simulation_service.service;



import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.postnord.simulation_service.dto.ResourceWeightHandledKPIDTO;
import com.postnord.simulation_service.entity.ResourceWeightHandledKPI;
import com.postnord.simulation_service.mapper.ResourceWeightHandledKPIMapper;
import com.postnord.simulation_service.repository.ResourceWeightHandledKPIRepository;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ResourceWeightHandledKPIService {

    private final ResourceWeightHandledKPIRepository resourceWeightHandledKPIRepository;
    private final ResourceWeightHandledKPIMapper resourceWeightHandledKPIMapper;

    public List<ResourceWeightHandledKPIDTO> saveResourceKPI(List<ResourceWeightHandledKPIDTO> dtoList)
    {
        List<ResourceWeightHandledKPI> entityList = dtoList.stream()
                .map(resourceWeightHandledKPIMapper::fromDTO)
                .collect(Collectors.toList());

        List<ResourceWeightHandledKPI> savedEntities= resourceWeightHandledKPIRepository.saveAll(entityList);

        return savedEntities.stream()
                .map(resourceWeightHandledKPIMapper::toDTO)
                .collect(Collectors.toList());
    }


    public List<ResourceWeightHandledKPIDTO> getResourceByExpId(Integer simExpId)
    {
        return resourceWeightHandledKPIRepository.findBySimExpId(simExpId)
                .stream()
                .map(resourceWeightHandledKPIMapper::toDTO)
                .collect(Collectors.toList());
    }
}
