package com.postnord.simulation_service.controller;



import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.postnord.simulation_service.dto.ExperimentProgressEventDto;
import com.postnord.simulation_service.dto.FlexsimParcelDto;
import com.postnord.simulation_service.dto.ParcelCountRequestDto;
import com.postnord.simulation_service.dto.ParcelCountResponseDto;
import com.postnord.simulation_service.enums.ExperimentRunStage;
import com.postnord.simulation_service.service.ExperimentProgressSseService;
import com.postnord.simulation_service.service.ExperimentRunProgressService;
import com.postnord.simulation_service.service.ParcelService;

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
    public ResponseEntity<ParcelCountResponseDto> getParcelCount(@RequestBody ParcelCountRequestDto parcelCountRequestDTO){

        ParcelCountResponseDto parcelCount = parcelService.getParcelCount(parcelCountRequestDTO);

        return ResponseEntity.ok(parcelCount);
    }
}
