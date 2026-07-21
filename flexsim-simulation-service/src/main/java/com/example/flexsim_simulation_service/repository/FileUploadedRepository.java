package com.example.flexsim_simulation_service.repository;

import com.example.flexsim_simulation_service.entity.FileUploaded;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface FileUploadedRepository extends JpaRepository<FileUploaded,Long> {
}
