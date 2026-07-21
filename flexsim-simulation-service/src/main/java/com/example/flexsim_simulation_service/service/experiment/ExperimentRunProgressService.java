package com.example.flexsim_simulation_service.service.experiment;

import com.example.flexsim_simulation_service.DTO.experiment.ExperimentRunProgressDto;
import com.example.flexsim_simulation_service.entity.experiment.Experiment;
import com.example.flexsim_simulation_service.entity.experiment.ExperimentRunProgress;
import com.example.flexsim_simulation_service.enums.ExperimentRunStage;
import com.example.flexsim_simulation_service.enums.ExperimentRunStatus;
import com.example.flexsim_simulation_service.repository.experiment.ExperimentRepository;
import com.example.flexsim_simulation_service.repository.experiment.ExperimentRunProgressRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ExperimentRunProgressService {

    private final ExperimentRunProgressRepository progressRepository;
    private final ExperimentRepository experimentRepository;

    @Transactional
    public ExperimentRunProgress startRun(Long experimentId, ExperimentRunStage stage, String message) {
        Experiment experiment = experimentRepository.findById(experimentId)
                .orElseThrow(() -> new RuntimeException("Experiment not found: " + experimentId));

        ExperimentRunProgress run = ExperimentRunProgress.builder()
                .experiment(experiment)
                .status(ExperimentRunStatus.RUNNING)
                .stage(stage)
                .message(message)
                .startedAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        return progressRepository.save(run);
    }

    @Transactional
    public ExperimentRunProgress updateStage(Long experimentId, ExperimentRunStage stage, String message) {
        ExperimentRunProgress run = progressRepository
                .findTopByExperimentExperimentIdAndStatusOrderByRunIdDesc(experimentId, ExperimentRunStatus.RUNNING)
                .orElseThrow(() -> new RuntimeException("No active simulation run found for experiment: " + experimentId));

        run.setStage(stage);
        run.setMessage(message);
        run.setUpdatedAt(LocalDateTime.now());

        return progressRepository.save(run);
    }

    @Transactional
    public ExperimentRunProgress markCompleted(Long experimentId, String message) {
        ExperimentRunProgress run = progressRepository
                .findTopByExperimentExperimentIdAndStatusOrderByRunIdDesc(experimentId, ExperimentRunStatus.RUNNING)
                .orElseThrow(() -> new RuntimeException("No active simulation run found for experiment: " + experimentId));

        run.setStatus(ExperimentRunStatus.COMPLETED);
        run.setStage(ExperimentRunStage.FINISHED);
        run.setMessage(message);
        run.setCompletedAt(LocalDateTime.now());
        run.setUpdatedAt(LocalDateTime.now());

        return progressRepository.save(run);
    }

    @Transactional
    public ExperimentRunProgress markFailed(Long experimentId, String errorMessage) {
        ExperimentRunProgress run = progressRepository
                .findTopByExperimentExperimentIdOrderByRunIdDesc(experimentId)
                .orElseThrow(() -> new RuntimeException("No simulation run found for experiment: " + experimentId));

        run.setStatus(ExperimentRunStatus.FAILED);
        run.setErrorMessage(errorMessage);
        run.setUpdatedAt(LocalDateTime.now());

        return progressRepository.save(run);
    }


    @Transactional
    public ExperimentRunProgressDto getLatestProgress(Long experimentId) {
        ExperimentRunProgress run = progressRepository
                .findTopByExperimentExperimentIdOrderByRunIdDesc(experimentId)
                .orElseThrow(() -> new RuntimeException("No simulation progress found for experiment: " + experimentId));

        return ExperimentRunProgressDto.builder()
                .runId(run.getRunId())
                .experimentId(run.getExperiment().getExperimentId())
                .status(run.getStatus().name())
                .stage(run.getStage().name())
                .message(run.getMessage())
                .errorMessage(run.getErrorMessage())
                .startedAt(run.getStartedAt())
                .completedAt(run.getCompletedAt())
                .updatedAt(run.getUpdatedAt())
                .build();
    }
}