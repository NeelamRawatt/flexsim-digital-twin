package com.example.flexsim_simulation_service.entity.experiment;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import com.example.flexsim_simulation_service.entity.resourceDetails.InfeedResourceDetail;
import com.example.flexsim_simulation_service.entity.resourceDetails.ZoneResourceDetail;
import com.example.flexsim_simulation_service.entity.user.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Data
@Table(name = "experiment")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Experiment {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "exp_seq")
    @SequenceGenerator(
            name = "exp_seq",
            sequenceName = "exp_sequence",
            initialValue = 1001,
            allocationSize = 1
    )
    private Long experimentId;

    private String experimentName;
    private String terminal;
    private String sortingType;
    private int useCaseId;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate selectedDate;

    @JsonFormat(pattern = "HH:mm:ss")
    private LocalDateTime startTime;

    @JsonFormat(pattern = "HH:mm:ss")
    private LocalDateTime endTime;
    private Integer parcelCount;
    private Integer newParcelCount;
    private Integer parcelChangeValue;
    private String parcelChangeMode; // increase / decrease
    private Integer maxRecirculationCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    private LocalDateTime createdAt;

    @JsonIgnore
    @OneToMany(mappedBy = "experiment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InfeedResourceDetail> infeedResources = new ArrayList<>();
    @JsonIgnore
    @OneToMany(mappedBy = "experiment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ZoneResourceDetail> zoneResources = new ArrayList<>();

    @OneToMany(mappedBy = "experiment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ExperimentRunProgress> simulationRuns = new ArrayList<>();

    @PrePersist
    public void createdAt(){
        this.createdAt = LocalDateTime.now();
    }

}
