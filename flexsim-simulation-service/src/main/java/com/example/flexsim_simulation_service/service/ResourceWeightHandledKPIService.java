package com.example.flexsim_simulation_service.service;


import com.example.flexsim_simulation_service.DTO.ResourceWeightHandledKPIDTO;
import com.example.flexsim_simulation_service.Mapper.ResourceWeightHandledKPIMapper;
import com.example.flexsim_simulation_service.entity.ResourceWeightHandledKPI;
import com.example.flexsim_simulation_service.repository.ResourceWeightHandledKPIRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

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
