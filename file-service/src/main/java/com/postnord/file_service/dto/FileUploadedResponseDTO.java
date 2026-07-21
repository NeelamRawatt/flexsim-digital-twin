package com.postnord.file_service.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.postnord.file_service.enums.FileCategory;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter@Setter@Builder
public class FileUploadedResponseDTO {

    private Long id;
    private Integer simExpId;
    private String fileName;
    private FileCategory fileCategory;
    private LocalDate uploadDate;
    private LocalDateTime uploadedAt;
    
}
