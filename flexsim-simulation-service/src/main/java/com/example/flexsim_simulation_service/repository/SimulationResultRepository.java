package com.example.flexsim_simulation_service.repository;

import com.example.flexsim_simulation_service.entity.SimulationResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SimulationResultRepository extends JpaRepository<SimulationResult,Long> {


}
