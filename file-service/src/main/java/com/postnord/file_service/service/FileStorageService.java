package com.postnord.file_service.service;

import java.io.IOException;
import java.time.LocalDate;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.postnord.file_service.dto.FileUploadedResponseDTO;
import com.postnord.file_service.dto.FileValidationResponseDTO;
import com.postnord.file_service.entity.FileUploaded;
import com.postnord.file_service.enums.FileCategory;
import com.postnord.file_service.exception.FileNotFoundException;
import com.postnord.file_service.repository.FileUploadedRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FileStorageService {

     private final FileUploadedRepository fileUploadedRepository;
    private final InfeedFileValidationService infeedFileValidationService;
    private final ZoneFileValidationService zoneResourceFileValidationService;
    


    public FileValidationResponseDTO uploadInfeedFile(MultipartFile file,
        Integer simExpId, LocalDate uploadDate , String uploadedBy
    ) throws IOException
    {
        FileValidationResponseDTO validation = infeedFileValidationService.validateInfeedFile(file);
        if(validation.isValid())
        {
            saveFile(file,simExpId,FileCategory.INFEED,uploadDate,uploadedBy);

        }

        return validation;
    
    
    
    }

    public FileValidationResponseDTO uploadZoneResourceFile(MultipartFile file, Integer simExpId, LocalDate uploadDate,String uploadedBy) throws IOException {
        FileValidationResponseDTO validation = zoneResourceFileValidationService.validateZoneResourceFile(file);
        if (validation.isValid()) {
            saveFile(file, simExpId, FileCategory.ZONE_RESOURCE, uploadDate,uploadedBy);
        }
        return validation;
    }

    public FileUploadedResponseDTO getMetadata(Long id) {
        FileUploaded entity = fileUploadedRepository.findById(id)
                .orElseThrow(() -> new FileNotFoundException(id));
        return FileUploadedResponseDTO.builder()
                .id(entity.getId())
                .simExpId(entity.getSimExpId())
                .fileName(entity.getFileName())
                .fileCategory(entity.getFileCategory())
                .uploadDate(entity.getUploadDate())
                .uploadedAt(entity.getUploadedAt())
                .build();
    }

    private void saveFile(MultipartFile file, Integer simExpId, FileCategory category, LocalDate uploadDate, String uploadedBy) throws IOException {
        FileUploaded entity = FileUploaded.builder()
                .simExpId(simExpId)
                .fileName(file.getOriginalFilename())
                .fileExtension(getFileExtension(file.getOriginalFilename()))
                .fileCategory(category)
                .fileContent(file.getBytes())
                .uploadDate(uploadDate)
                .uploadedBy(uploadedBy)
                .build();
        fileUploadedRepository.save(entity);
    }

    public boolean areRequiredFilesReady(Integer simExpId) {
    boolean hasInfeed = fileUploadedRepository.existsBySimExpIdAndFileCategory(simExpId, FileCategory.INFEED);
    boolean hasZoneResource = fileUploadedRepository.existsBySimExpIdAndFileCategory(simExpId, FileCategory.ZONE_RESOURCE);
    return hasInfeed && hasZoneResource;
}
    private String getFileExtension(String fileName) {
        if (fileName == null || !fileName.contains(".")) return "";
        return fileName.substring(fileName.lastIndexOf('.') + 1);
    }

}
