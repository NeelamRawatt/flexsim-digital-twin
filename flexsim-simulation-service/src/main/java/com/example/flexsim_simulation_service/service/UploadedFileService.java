package com.example.flexsim_simulation_service.service;


import com.example.flexsim_simulation_service.DTO.FileValidationResponseDTO;
import com.example.flexsim_simulation_service.entity.FileUploaded;
import com.example.flexsim_simulation_service.repository.FileUploadedRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class UploadedFileService {

    private final FileUploadedRepository uploadedFileRepository;

   private final InfeedFileValidationService infeedFileValidationService;

    public FileValidationResponseDTO uploadInfeedFile(MultipartFile file, Integer simExpId, LocalDate fileUploadDate) throws IOException {

        FileValidationResponseDTO validationResponse = infeedFileValidationService.validateInfeedFile(file);

        if (!validationResponse.isValid()) {

            return validationResponse;

        }

        FileUploaded uploadedFile = FileUploaded.builder()
                .simExpId(simExpId)
                .uploaded_file_name(file.getOriginalFilename())
                .uploaded_file_type(getFileExtension(file.getOriginalFilename()))
                .file_upload_date(fileUploadDate)
                .build();

        uploadedFileRepository.save(uploadedFile);

        return FileValidationResponseDTO.builder()
                .valid(true)
                .message("File uploaded and validated successfully")
                .errors(null)
                .build();

    }

    private String getFileExtension(String fileName) {

        if (fileName == null || !fileName.contains(".")) {

            return "";

        }

        return fileName.substring(fileName.lastIndexOf('.') + 1);

    }

}
