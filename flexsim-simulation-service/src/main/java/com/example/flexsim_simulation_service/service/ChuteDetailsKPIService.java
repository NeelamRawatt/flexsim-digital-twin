package com.example.flexsim_simulation_service.service;


import com.example.flexsim_simulation_service.DTO.ChuteDetailsKPIDTO;
import com.example.flexsim_simulation_service.Mapper.ChuteDetailsKPIMapper;
import com.example.flexsim_simulation_service.entity.ChuteDetailsKPI;
import com.example.flexsim_simulation_service.repository.ChuteDetailsKPIRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChuteDetailsKPIService {

    private final ChuteDetailsKPIRepository chuteDetailsKPIRepository;
    private final ChuteDetailsKPIMapper chuteDetailsKPIMapper;

    public List<ChuteDetailsKPIDTO> saveChuteDetails(List<ChuteDetailsKPIDTO> dtoList)
    {
        List<ChuteDetailsKPI> entityList= dtoList.stream()
                .map(chuteDetailsKPIMapper::fromDTO)
                .collect(Collectors.toList());

        List<ChuteDetailsKPI> savedEntities= chuteDetailsKPIRepository.saveAll(entityList);

        return savedEntities.stream()
                .map(chuteDetailsKPIMapper::toDto)
                .collect(Collectors.toList());

    }

    public List<ChuteDetailsKPIDTO> getBySimExpId(Integer simExpId)
    {

        return chuteDetailsKPIRepository.findBySimExpId(simExpId)
                .stream()
                .map(chuteDetailsKPIMapper::toDto)
                .collect(Collectors.toList());

    }
}
