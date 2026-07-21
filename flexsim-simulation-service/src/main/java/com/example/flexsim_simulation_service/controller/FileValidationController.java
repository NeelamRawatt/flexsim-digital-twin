package com.example.flexsim_simulation_service.controller;


import com.example.flexsim_simulation_service.DTO.FileValidationResponseDTO;
import com.example.flexsim_simulation_service.service.InfeedFileValidationService;
import com.example.flexsim_simulation_service.service.UploadedFileService;
//import com.example.flexsim_simulation_service.service.ZoneFileValidationService;
import com.example.flexsim_simulation_service.service.ZoneResourceFileValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/upload-file")
@RequiredArgsConstructor
public class FileValidationController {

    private final InfeedFileValidationService infeedFileValidationService;
    private final UploadedFileService uploadedFileService;
    private final ZoneResourceFileValidationService zoneResourceFileValidationService;

    @PostMapping("/validate/infeed")
    public ResponseEntity<FileValidationResponseDTO> uploadInfeedFile(
            @RequestParam("file")MultipartFile file,
            @RequestParam("simExpId") Integer simExpId,
            @RequestParam("fileUploadDate")
            @DateTimeFormat(iso= DateTimeFormat.ISO.DATE)LocalDate fileUploadDate
            ) throws IOException {
        FileValidationResponseDTO response = uploadedFileService.uploadInfeedFile(file,simExpId,fileUploadDate);

        if(response.isValid())
        {
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @PostMapping("/validate/zone-resource")
    public ResponseEntity<FileValidationResponseDTO> validateZoneResourceFile(
            @RequestParam("file") MultipartFile file) {
        FileValidationResponseDTO response = zoneResourceFileValidationService.validateZoneResourceFile(file);
        return response.isValid()
                ? ResponseEntity.ok(response)
                : ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

}
