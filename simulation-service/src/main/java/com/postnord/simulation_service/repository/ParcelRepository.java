package com.postnord.simulation_service.repository;


import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.postnord.simulation_service.entity.ParcelDetails;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ParcelRepository extends JpaRepository<ParcelDetails, Long> {
    public List<ParcelDetails> findByParcelEdtDateAndParcelTimeSlotBetween(
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
