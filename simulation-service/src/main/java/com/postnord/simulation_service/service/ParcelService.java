package com.postnord.simulation_service.service;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.postnord.simulation_service.dto.FlexsimParcelDto;
import com.postnord.simulation_service.dto.ParcelCountRequestDto;
import com.postnord.simulation_service.dto.ParcelCountResponseDto;
import com.postnord.simulation_service.entity.ParcelDetails;
import com.postnord.simulation_service.entity.SimulationContext;
import com.postnord.simulation_service.exception.SimulationContextNotFoundException;
import com.postnord.simulation_service.mapper.ParcelMapper;
import com.postnord.simulation_service.repository.ParcelRepository;
import com.postnord.simulation_service.repository.SimulationContextRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ParcelService {

    private final ParcelRepository parcelRepository;
    private final SimulationContextRepository simulationContextRepository;
    private final ParcelMapper parcelMapper;

    public List<FlexsimParcelDto> getParcelsForExperiment(Long experimentId) {
        SimulationContext context = simulationContextRepository.findById(experimentId)
                .orElseThrow(() -> new SimulationContextNotFoundException(experimentId));

        List<ParcelDetails> parcels = parcelRepository
                .findByParcelEdtDateAndParcelTimeSlotBetween(
                        context.getSelectedDate(),
                        context.getStartTime(),
                        context.getEndTime(),
                        Sort.by("parcelTimeSlot").ascending()
                );

        return parcelMapper.toFlexsimParcelDtos(parcels, context);
    }

    public ParcelCountResponseDto getParcelCount(ParcelCountRequestDto request) {
        Long count = parcelRepository.countByParcelTimeSlotBetween(request.getStartTime(), request.getEndTime());
        return ParcelCountResponseDto.builder().parcelCount(count).build();
    }
}