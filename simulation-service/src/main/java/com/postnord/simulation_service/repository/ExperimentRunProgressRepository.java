package com.postnord.simulation_service.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.postnord.simulation_service.enums.ExperimentRunStatus;

import com.postnord.simulation_service.entity.ExperimentRunProgress;


@Repository
public interface ExperimentRunProgressRepository extends JpaRepository<ExperimentRunProgress, Long> {

    Optional<ExperimentRunProgress> findTopByExperimentIdOrderByRunIdDesc(Long experimentId);

    Optional<ExperimentRunProgress> findTopByExperimentIdAndStatusOrderByRunIdDesc(
            Long experimentId, ExperimentRunStatus status
    );
}
