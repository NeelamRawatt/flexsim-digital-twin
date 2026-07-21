package com.example.flexsim_simulation_service.service;


import com.example.flexsim_simulation_service.DTO.ParcelDetailsKPIPerTickDTO;
import com.example.flexsim_simulation_service.Mapper.ParcelDetailsKPIPerTickMapper;
import com.example.flexsim_simulation_service.entity.ParcelDetailsKPIPerTick;
import com.example.flexsim_simulation_service.repository.ParcelDetailsKPIPerTickRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ParcelDetailsKPIPerTickService {

    private final ParcelDetailsKPIPerTickRepository parcelDetailsKPIPerTickRepository;
    private final ParcelDetailsKPIPerTickMapper parcelDetailsKPIPerTickMapper;

    public List<ParcelDetailsKPIPerTickDTO> saveParcelInsights
            (List<ParcelDetailsKPIPerTickDTO> dtoList)
    {
        List<ParcelDetailsKPIPerTick> entityList = dtoList.stream()
                .map(parcelDetailsKPIPerTickMapper::fromDTO)
                .collect(Collectors.toList());

       List<ParcelDetailsKPIPerTick> savedEntityList= parcelDetailsKPIPerTickRepository.saveAll(entityList);

       return savedEntityList.stream()
               .map(parcelDetailsKPIPerTickMapper::toDTO)
               .collect(Collectors.toList());
    }

    public List<ParcelDetailsKPIPerTickDTO> getByExpId(Integer simExpId)
    {
        return parcelDetailsKPIPerTickRepository.findBySimExpId(simExpId)
                .stream()
                .map(parcelDetailsKPIPerTickMapper::toDTO)
                .collect(Collectors.toList());
    }
}
