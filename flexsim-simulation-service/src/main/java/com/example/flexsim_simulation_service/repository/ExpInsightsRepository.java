package com.example.flexsim_simulation_service.repository;


import com.example.flexsim_simulation_service.entity.ExpInsights;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ExpInsightsRepository extends JpaRepository<ExpInsights,Long> {


    Optional<ExpInsights> findBySimExpId(Integer expId);
}
