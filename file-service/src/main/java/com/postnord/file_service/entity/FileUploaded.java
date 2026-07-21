package com.postnord.file_service.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.postnord.file_service.enums.FileCategory;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="uploaded_file")
@Getter@Setter@Builder
@NoArgsConstructor@AllArgsConstructor
public class FileUploaded {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="sim_exp_id")
    private Integer simExpId;

    private String fileName;
    private String fileExtension;

    @Enumerated(EnumType.STRING)
    private FileCategory fileCategory;


    
    @Column(name="file_content")
    private byte[] fileContent;

    private LocalDate uploadDate;
    private LocalDateTime uploadedAt;

    private String uploadedBy;


    @PrePersist
    public void onCreate()
    {
        this.uploadedAt= LocalDateTime.now();
    }
    
}
