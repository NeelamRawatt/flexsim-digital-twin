package com.example.flexsim_simulation_service.entity.resourceDetails;

import com.example.flexsim_simulation_service.entity.experiment.Experiment;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Entity
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InfeedResourceDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    Integer shiftId;
    String tc;
    String infeed;
    String zoneId;
    Integer noOfResources;
    Boolean active;

    @ManyToOne
    @JoinColumn(name = "exp_id")
    Experiment experiment;
}
