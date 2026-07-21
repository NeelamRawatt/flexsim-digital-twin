package com.example.flexsim_simulation_service.repository.experiment;

import com.example.flexsim_simulation_service.entity.experiment.ExperimentRunProgress;
import com.example.flexsim_simulation_service.enums.ExperimentRunStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ExperimentRunProgressRepository extends JpaRepository<ExperimentRunProgress, Long> {

    Optional<ExperimentRunProgress> findTopByExperimentExperimentIdOrderByRunIdDesc(Long experimentId);

    Optional<ExperimentRunProgress> findTopByExperimentExperimentIdAndStatusOrderByRunIdDesc(
            Long experimentId, ExperimentRunStatus status
    );
}
