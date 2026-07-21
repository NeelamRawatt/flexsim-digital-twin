package com.postnord.simulation_service.repository;



import org.springframework.data.jpa.repository.JpaRepository;

import com.postnord.simulation_service.entity.ChuteDetailsKPI;

import java.util.List;

public interface ChuteDetailsKPIRepository extends JpaRepository<ChuteDetailsKPI, Long> {
    List<ChuteDetailsKPI> findBySimExpId(Integer simExpId);
}