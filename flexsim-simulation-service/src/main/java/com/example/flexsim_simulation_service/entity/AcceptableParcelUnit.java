package com.example.flexsim_simulation_service.entity;

import com.example.flexsim_simulation_service.entity.experiment.Experiment;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Data
@Table(name = "acceptable_parcel_unit")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AcceptableParcelUnit {

    @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "acceptable_parcel_unit_id")
   private Long id;

   
   private Double minHeight;
   private Double maxHeight;
   private Double minLength;
   private Double maxLength;
   private Double minWidth;
   private Double maxWidth;
   private Double minWeight;
   private Double maxWeight;

    @ManyToOne
    @JoinColumn(name = "exp_id")
    Experiment experiment;



    
}
