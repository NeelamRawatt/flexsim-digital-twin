package com.postnord.simulation_service.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.postnord.simulation_service.dto.ExperimentRunProgressDto;
import com.postnord.simulation_service.entity.ExperimentRunProgress;
import com.postnord.simulation_service.enums.ExperimentRunStage;
import com.postnord.simulation_service.enums.ExperimentRunStatus;
import com.postnord.simulation_service.exception.SimulationRunNotFoundException;
import com.postnord.simulation_service.repository.ExperimentRunProgressRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ExperimentRunProgressService {

    private final ExperimentRunProgressRepository progressRepository;

    @Transactional
    public ExperimentRunProgress startRun(Long experimentId, ExperimentRunStage stage, String message) {
        ExperimentRunProgress run = ExperimentRunProgress.builder()
                .experimentId(experimentId)
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
                .findTopByExperimentIdAndStatusOrderByRunIdDesc(experimentId, ExperimentRunStatus.RUNNING)
                .orElseThrow(() -> new SimulationRunNotFoundException(experimentId));

        run.setStage(stage);
        run.setMessage(message);
        run.setUpdatedAt(LocalDateTime.now());
        return progressRepository.save(run);
    }

    @Transactional
    public ExperimentRunProgress markCompleted(Long experimentId, String message) {
        ExperimentRunProgress run = progressRepository
                .findTopByExperimentIdAndStatusOrderByRunIdDesc(experimentId, ExperimentRunStatus.RUNNING)
                .orElseThrow(() -> new SimulationRunNotFoundException(experimentId));

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
                .findTopByExperimentIdOrderByRunIdDesc(experimentId)
                .orElseThrow(() -> new SimulationRunNotFoundException(experimentId));

        run.setStatus(ExperimentRunStatus.FAILED);
        run.setErrorMessage(errorMessage);
        run.setUpdatedAt(LocalDateTime.now());
        return progressRepository.save(run);
    }

    @Transactional
    public ExperimentRunProgressDto getLatestProgress(Long experimentId) {
        ExperimentRunProgress run = progressRepository
                .findTopByExperimentIdOrderByRunIdDesc(experimentId)
                .orElseThrow(() -> new SimulationRunNotFoundException(experimentId));

        return ExperimentRunProgressDto.builder()
                .runId(run.getRunId())
                .experimentId(run.getExperimentId())
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