package com.postnord.simulation_service.repository;



import org.springframework.data.jpa.repository.JpaRepository;

import com.postnord.simulation_service.entity.AcceptableParcelUnit;

import java.util.Optional;

public interface AcceptableParcelUnitRepository extends JpaRepository<AcceptableParcelUnit, Long> {
    Optional<AcceptableParcelUnit> findByExperimentId(Long experimentId); // new — original had no way to read this back at all
}