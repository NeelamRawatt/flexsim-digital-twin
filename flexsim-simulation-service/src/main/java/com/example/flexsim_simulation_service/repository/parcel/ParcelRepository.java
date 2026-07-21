package com.example.flexsim_simulation_service.repository.parcel;

import com.example.flexsim_simulation_service.entity.parcel.Parcel;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ParcelRepository extends JpaRepository<Parcel, Long> {
    public List<Parcel> findByParcelEdtDateAndParcelTimeSlotBetween(
            LocalDate parcelEdtDate,
            LocalDateTime startTimeSlot,
            LocalDateTime endTimeSlot,
            Sort sort
    );

    public Long countByParcelTimeSlotBetween(
            LocalDateTime startTimeSlot,
            LocalDateTime endTimeSlot
    );
}
