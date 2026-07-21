package com.example.flexsim_simulation_service.entity.parcel;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "parcel_details")
@Getter
@Setter
public class Parcel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(name = "parcel_id")
    private String parcelId;

    @Column(name = "parcel_time_slot")
    private LocalDateTime parcelTimeSlot;

    @Column(name = "parcel_date")
    private LocalDate parcelDate;

    @Column(name = "parcel_timestamp")
    private LocalTime parcelTimestamp;

    @Column(name = "parcel_infeed_name")
    private String parcelInfeedName;

    @Column(name = "sort_name")
    private String sortName;

    @Column(name = "reject_code")
    private String rejectCode;

    @Column(name = "parcel_weight")
    private Double parcelWeight;

    @Column(name = "parcel_length")
    private Double parcelLength;

    @Column(name = "parcel_width")
    private Double parcelWidth;

    @Column(name = "parcel_height")
    private Double parcelHeight;

    @Column(name = "parcel_volume")
    private Double parcelVolume;

    @Column(name = "machine_sorting_direction")
    private String machineSortingDirection;

    @Column(name = "additional_service_code_local")
    private String additionalServiceCodeLocal;

    @Column(name = "postal_code")
    private String postalCode;

    @Column(name = "service_code")
    private String serviceCode;

    @Column(name = "sort_type")
    private String sortType;

    @Column(name = "parcel_edt_date")
    private LocalDate parcelEdtDate;

    @Column(name = "terminal_id")
    private Integer terminalId;

    @Column(name = "parcel_date_time")
    private LocalDateTime parcelDateTime;

//    @Column(name = "flexsim_time_slot")
//    private Integer flexsimTimeSlot;
}
