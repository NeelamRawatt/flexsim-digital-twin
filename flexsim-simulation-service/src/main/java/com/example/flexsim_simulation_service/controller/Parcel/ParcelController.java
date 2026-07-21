package com.example.flexsim_simulation_service.controller.Parcel;

import com.example.flexsim_simulation_service.DTO.experiment.ExperimentProgressEventDto;
import com.example.flexsim_simulation_service.DTO.parcel.FlexsimParcelDto;
import com.example.flexsim_simulation_service.DTO.parcel.ParcelCountRequestDTO;
import com.example.flexsim_simulation_service.DTO.parcel.ParcelCountResponseDTO;
import com.example.flexsim_simulation_service.enums.ExperimentRunStage;
import com.example.flexsim_simulation_service.service.experiment.ExperimentProgressSseService;
import com.example.flexsim_simulation_service.service.experiment.ExperimentRunProgressService;
import com.example.flexsim_simulation_service.service.parcel.ParcelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/parcels")
public class ParcelController {

    private final ParcelService parcelService;

    private final ExperimentRunProgressService experimentRunProgressService;

    private final ExperimentProgressSseService experimentProgressSseService;

    @GetMapping("/getParcels/{exp_id}")
    public ResponseEntity<List<FlexsimParcelDto>> getParcelsForExperiment(@PathVariable("exp_id") Long experimentId)
    {
        experimentRunProgressService.updateStage(
                experimentId,
                ExperimentRunStage.IMPORTING_PARCEL_DATA,
                "Importing Parcel Data"
        );

        experimentProgressSseService.send(
                experimentId,
                ExperimentProgressEventDto.builder()
                        .experimentId(experimentId)
                        .status("RUNNING")
                        .stage(ExperimentRunStage.IMPORTING_PARCEL_DATA.name())
                        .message("Importing Parcel Data")
                        .build()
        );

        List<FlexsimParcelDto> parcels = parcelService.getParcelsForExperiment(experimentId);
        return ResponseEntity.ok(parcels);
    }

    @PostMapping("/getParcelCount")
    public ResponseEntity<ParcelCountResponseDTO> getParcelCount(@RequestBody ParcelCountRequestDTO parcelCountRequestDTO){

        ParcelCountResponseDTO parcelCount = parcelService.getParcelCount(parcelCountRequestDTO);

        return ResponseEntity.ok(parcelCount);
    }
}
