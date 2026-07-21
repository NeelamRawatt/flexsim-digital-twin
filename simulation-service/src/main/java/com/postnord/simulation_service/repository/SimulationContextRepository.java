package com.postnord.simulation_service.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.postnord.simulation_service.entity.SimulationContext;

public interface SimulationContextRepository extends JpaRepository<SimulationContext, Long> {
}