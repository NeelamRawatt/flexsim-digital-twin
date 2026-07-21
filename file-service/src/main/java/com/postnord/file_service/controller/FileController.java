package com.postnord.file_service.controller;

import java.io.IOException;
import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.postnord.file_service.dto.FileUploadedResponseDTO;
import com.postnord.file_service.dto.FileValidationResponseDTO;
import com.postnord.file_service.service.FileStorageService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class FileController {
    
    private final FileStorageService fileStorageService;

    @PostMapping("/validate/infeed")
    public ResponseEntity<FileValidationResponseDTO> uploadInfeedFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("simExpId") Integer simExpId,
            @RequestParam("fileUploadDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fileUploadDate,
            Authentication authentication
    ) throws IOException {
        FileValidationResponseDTO response = fileStorageService.uploadInfeedFile(file, simExpId, fileUploadDate,authentication.getName());
        return response.isValid()
                ? ResponseEntity.status(HttpStatus.CREATED).body(response)
                : ResponseEntity.badRequest().body(response);
    }

    @PostMapping("/validate/zone-resource")
    public ResponseEntity<FileValidationResponseDTO> uploadZoneResourceFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("simExpId") Integer simExpId,
            @RequestParam("fileUploadDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fileUploadDate,
            Authentication authentication
    
        ) throws IOException {
        FileValidationResponseDTO response = fileStorageService.uploadZoneResourceFile(file, simExpId, fileUploadDate,authentication.getName());
        return response.isValid()
                ? ResponseEntity.status(HttpStatus.CREATED).body(response)
                : ResponseEntity.badRequest().body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FileUploadedResponseDTO> getFileMetadata(@PathVariable Long id) {
        return ResponseEntity.ok(fileStorageService.getMetadata(id));
    }


}
