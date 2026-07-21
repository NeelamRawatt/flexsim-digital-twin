package com.example.flexsim_simulation_service.DTO.parcel;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FlexsimParcelDto {
    LocalDateTime parcelTimeSlot;
    String parcelId;
    String parcelInfeedName;
    String sortName;
    String rejectCode;
    Double parcelWeight;
    Double parcelWidth;
    Double parcelLength;
    Double parcelHeight;
    Double parcelVolume;
    Integer flexsimTimeSlot;
}
