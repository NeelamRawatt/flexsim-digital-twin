package com.postnord.simulation_service.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "parcel_details")
@Getter @Setter
public class ParcelDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "parcel_id")
    private String parcelId;

    @Column(name = "parcel_time_slot")
    private LocalDateTime parcelTimeSlot;

    @Column(name = "parcel_date")
    private LocalDate parcelDate;

    @Column(name = "parcel_infeed_name")
    private String parcelInfeedName;

    @Column(name = "sort_name")
    private String sortName;

    @Column(name = "reject_code")
    private String rejectCode;

    private Double parcelWeight;
    private Double parcelLength;
    private Double parcelWidth;
    private Double parcelHeight;
    private Double parcelVolume;

    @Column(name = "parcel_edt_date")
    private LocalDate parcelEdtDate;

    @Column(name = "terminal_id")
    private Integer terminalId;
}