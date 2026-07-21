package com.postnord.simulation_service.repository;



import org.springframework.data.jpa.repository.JpaRepository;

import com.postnord.simulation_service.entity.ExpInsights;

import java.util.Optional;

public interface ExpInsightsRepository extends JpaRepository<ExpInsights, Long> {
    Optional<ExpInsights> findBySimExpId(Integer simExpId);
}
