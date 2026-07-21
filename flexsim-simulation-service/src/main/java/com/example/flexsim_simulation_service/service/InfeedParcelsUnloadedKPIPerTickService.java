package com.example.flexsim_simulation_service.service;


import com.example.flexsim_simulation_service.DTO.InfeedParcelsUnloadedKPIPerTickDTO;
import com.example.flexsim_simulation_service.Mapper.InfeedParcelsUnloadedKPIPerTickMapper;
import com.example.flexsim_simulation_service.entity.InfeedParcelsUnloadedKPIPerTick;
import com.example.flexsim_simulation_service.repository.InfeedParcelsUnloadedKPIPerTickRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class InfeedParcelsUnloadedKPIPerTickService {

    private final InfeedParcelsUnloadedKPIPerTickMapper infeedParcelsUnloadedKPIPerTickMapper;

    private final InfeedParcelsUnloadedKPIPerTickRepository infeedParcelsUnloadedKPIPerTickRepository;

    public List<InfeedParcelsUnloadedKPIPerTickDTO> saveInfeedInsights
            (List<InfeedParcelsUnloadedKPIPerTickDTO> dto) {

        List<InfeedParcelsUnloadedKPIPerTick> entityList=dto.stream()
                .map(infeedParcelsUnloadedKPIPerTickMapper::fromDTO)
                .collect(Collectors.toList());

        List<InfeedParcelsUnloadedKPIPerTick> savedList = infeedParcelsUnloadedKPIPerTickRepository.saveAll(entityList);

        return savedList.stream()
                .map(infeedParcelsUnloadedKPIPerTickMapper::toDTO)
                .collect(Collectors.toList());


    }

    public List<InfeedParcelsUnloadedKPIPerTickDTO> getByExpId(Integer simExpId)
    {

        return infeedParcelsUnloadedKPIPerTickRepository.findBySimExpId(simExpId)
                .stream()
                .map(infeedParcelsUnloadedKPIPerTickMapper::toDTO)
                .collect(Collectors.toList());
    }
}
