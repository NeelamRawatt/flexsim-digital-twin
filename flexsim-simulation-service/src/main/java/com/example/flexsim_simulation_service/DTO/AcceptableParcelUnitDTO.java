package com.example.flexsim_simulation_service.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AcceptableParcelUnitDTO {


   private Long experimentId; 
   private Double minHeight;
   private Double maxHeight;
   private Double minLength;
   private Double maxLength;
   private Double minWidth;
   private Double maxWidth;
   private Double minWeight;
   private Double maxWeight;
    
}
