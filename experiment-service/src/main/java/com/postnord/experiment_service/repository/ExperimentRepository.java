package com.postnord.experiment_service.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.postnord.experiment_service.entity.Experiment;

public interface ExperimentRepository extends JpaRepository<Experiment,Long>
{
     Page<Experiment> findByUsernameIgnoreCase(String username, Pageable pageable);
}
