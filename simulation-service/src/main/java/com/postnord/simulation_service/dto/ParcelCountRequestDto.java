package com.postnord.simulation_service.dto;


import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.*;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Data
public class ParcelCountRequestDto {
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private LocalDate parcelDate;
}
