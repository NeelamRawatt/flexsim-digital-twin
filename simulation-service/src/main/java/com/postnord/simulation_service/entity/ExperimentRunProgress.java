package com.postnord.simulation_service.entity;

import java.time.LocalDateTime;

import com.postnord.simulation_service.enums.ExperimentRunStage;
import com.postnord.simulation_service.enums.ExperimentRunStatus;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "experiment_run_progress")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ExperimentRunProgress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long runId;

    @Column(name = "experiment_id", nullable = false)
    private Long experimentId; // plain field now — no relationship to Experiment Service's table

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private ExperimentRunStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private ExperimentRunStage stage;

    @Column(length = 300)
    private String message;

    @Column(length = 1000)
    private String errorMessage;

    @Column(nullable = false)
    private LocalDateTime startedAt;

    private LocalDateTime completedAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        this.startedAt = (this.startedAt == null) ? now : this.startedAt;
        this.updatedAt = now;
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
