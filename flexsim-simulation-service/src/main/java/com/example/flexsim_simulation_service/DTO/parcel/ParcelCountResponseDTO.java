package com.example.flexsim_simulation_service.DTO.parcel;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ParcelCountResponseDTO {
    private Long parcelCount;
}
