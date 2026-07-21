package com.postnord.file_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.postnord.file_service.entity.FileUploaded;

@Repository
public interface FileUploadedRepository extends JpaRepository<FileUploaded,Long>
 {
    
}
