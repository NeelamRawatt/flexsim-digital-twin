package com.example.flexsim_simulation_service.DTO;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParcelDetailsKPIPerTickDTO {

    private Integer simExpId;
    private Integer tick_value;
    private Integer parcel_scanned;
    private Integer parcel_throughput;
    private Integer parcel_rejected;
    private Integer parcel_throughput_without_rejected;
    private Integer parcel_blocked;
    private Integer variable_value;
    private Integer shift_id;

}
