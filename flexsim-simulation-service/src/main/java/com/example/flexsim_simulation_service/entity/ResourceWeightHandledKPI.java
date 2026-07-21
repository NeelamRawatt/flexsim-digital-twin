package com.example.flexsim_simulation_service.entity;


import jakarta.persistence.*;
import lombok.*;
import org.springframework.web.bind.annotation.RequestBody;

@Entity
@Table(name="resource_weight_handled_kpi")
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class ResourceWeightHandledKPI {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq_id;

    @Column(name="sim_exp_id")
    private Integer simExpId;
    private Integer resource_id;
    private String zone_name;
    private String chute_id;
    private Integer total_parcels_handled;
    private Integer total_weight_handled;
    private Integer tick_value;
    private Integer shift_id;




}
