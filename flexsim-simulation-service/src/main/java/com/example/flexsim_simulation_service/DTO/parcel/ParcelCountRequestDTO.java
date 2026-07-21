package com.example.flexsim_simulation_service.DTO.parcel;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.*;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Data
public class ParcelCountRequestDTO {
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private LocalDate parcelDate;
}
