package com.example.flexsim_simulation_service.service.parcel;

import com.example.flexsim_simulation_service.DTO.parcel.FlexsimParcelDto;
import com.example.flexsim_simulation_service.DTO.parcel.ParcelCountRequestDTO;
import com.example.flexsim_simulation_service.DTO.parcel.ParcelCountResponseDTO;
import com.example.flexsim_simulation_service.Mapper.Parcel.ParcelMapper;
import com.example.flexsim_simulation_service.entity.experiment.ExperimentRunProgress;
import com.example.flexsim_simulation_service.entity.parcel.Parcel;
import com.example.flexsim_simulation_service.entity.experiment.Experiment;
import com.example.flexsim_simulation_service.enums.ExperimentRunStage;
import com.example.flexsim_simulation_service.repository.parcel.ParcelRepository;
import com.example.flexsim_simulation_service.repository.experiment.ExperimentRepository;
import com.example.flexsim_simulation_service.service.experiment.ExperimentRunProgressService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class ParcelService {
    final ParcelRepository parcelRepository;

    final ExperimentRepository experimentRepository;

    final ParcelMapper parcelMapper;

    public List<FlexsimParcelDto> getParcelsForExperiment(Long experimentId){

        Experiment experiment = experimentRepository.findById(experimentId)
                .orElseThrow(() -> new RuntimeException("Experiment with id " + experimentId + " not found"));

        List<Parcel> parcels = parcelRepository.findByParcelEdtDateAndParcelTimeSlotBetween(
                experiment.getSelectedDate(),
                experiment.getStartTime(),
                experiment.getEndTime(),
                Sort.by("parcelTimeSlot").ascending()
        );

        List<FlexsimParcelDto> flexsimParcelDtos = parcelMapper.toFlexsimParcelDtos(parcels, experiment);

        return flexsimParcelDtos;
    }

    public ParcelCountResponseDTO getParcelCount(ParcelCountRequestDTO parcelCountRequestDTO){
        Long parcelCount = parcelRepository.countByParcelTimeSlotBetween(
                parcelCountRequestDTO.getStartTime(),
                parcelCountRequestDTO.getEndTime()
        );

        return ParcelCountResponseDTO.builder()
                .parcelCount(parcelCount)
                .build();
    }
}
