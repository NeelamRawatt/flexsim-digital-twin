package com.postnord.experiment_service.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.postnord.experiment_service.enums.ExperimentStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="experiment")
@Getter@Setter@Builder@NoArgsConstructor@AllArgsConstructor
public class Experiment {
    
    @Id
    @SequenceGenerator(
        name = "exp_seq",
        sequenceName = "exp_sequence",
        initialValue = 1001,
        allocationSize = 1
    )
    @GeneratedValue(strategy=GenerationType.SEQUENCE,generator="exp_seq")
    private Long experimentId;

    private String experimentName;
    private String terminal;
    private String sortingType;
    private int useCaseId;

    private LocalDate selectedDate;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    private Integer parcelCount;
    private Integer newParcelCount;
    private Integer parcelChangeValue;
    private String parcelChangeMode;
    private Integer maxRecirculationCount;

    private String username;

    @Enumerated(EnumType.STRING)
    private ExperimentStatus status;

    private LocalDateTime createdAt;


    @PrePersist
    public void onCreate()
    {
        this.createdAt=LocalDateTime.now();
        if(this.status == null)
        {
            this.status=ExperimentStatus.CREATED;
        }
    }
}
