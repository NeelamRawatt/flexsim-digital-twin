package com.example.flexsim_simulation_service.entity.experiment;

import com.example.flexsim_simulation_service.enums.ExperimentRunStage;
import com.example.flexsim_simulation_service.enums.ExperimentRunStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "experiment_run_progress",
        indexes = {
                @Index(name = "idx_sim_run_experiment", columnList = "experiment_id"),
                @Index(name = "idx_sim_run_status", columnList = "status")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExperimentRunProgress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long runId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "experiment_id", nullable = false)
    private Experiment experiment;

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